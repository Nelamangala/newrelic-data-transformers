package com.newrelic.datatransformers.processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.newrelic.datatransformers.model.Operation;
import com.newrelic.datatransformers.transformers.FarenheitToCelsiusTransformer;
import com.newrelic.datatransformers.transformers.HawaiiTimeToUnixTimeTransformer;
import com.newrelic.datatransformers.transformers.SlugifyTransformer;

@Component
public class DataFileProcessor {
	
	private Logger logger = Logger.getLogger(DataFileProcessor.class);

	@Autowired
	private JsonToOperationsProcessor jsonFileProcessor;
	
	@Autowired
	private FarenheitToCelsiusTransformer farenheitToCelsiusTransformer;
	
	@Autowired
	private SlugifyTransformer slugifyTransformer;
	
	@Autowired
	private HawaiiTimeToUnixTimeTransformer htcToUtcTransformer;
	
	private FileOutputStream outputFile = null;
	
	private FileInputStream dataFileInputStream = null;
	
	private BufferedReader dataFileBufferedReader = null;
	
	public void processDataFile(String pathToCommandsJsonFile, String pathToDataFile, String pathToOutputFile) {
	
		byte[] jsonCommandsBytes = null;
		try {
			jsonCommandsBytes = Files.readAllBytes(Paths.get(pathToCommandsJsonFile));
		} catch (IOException e1) {
			logger.error("Error reading commands json file at " + pathToCommandsJsonFile);
			e1.printStackTrace();
			return;
		}
		String dataTransformCommandsJson = new String(jsonCommandsBytes);
		
		LinkedHashMap<Operation, String> extractOperations = jsonFileProcessor.extractOperations(dataTransformCommandsJson);
		
		if(extractOperations.isEmpty()) {
			logger.error("No data transformations found in given input file at " + pathToCommandsJsonFile);
			return;
		}
				
		// Handle to output file
		try { 
			outputFile = new FileOutputStream(new File(pathToOutputFile));
		} catch (FileNotFoundException e) {
			logger.error("Error: output file could not be created at " + pathToOutputFile);
			e.printStackTrace();
			return;
		}
		
		try {
		    dataFileInputStream = new FileInputStream(pathToDataFile);
		} catch (FileNotFoundException e) {
		    logger.error("Data file not found at " + pathToDataFile);
		    return;
		} 
		

	    try {
			dataFileBufferedReader = Files.newBufferedReader(Paths.get(pathToDataFile),StandardCharsets.UTF_8);
		} catch (IOException e1) {
			logger.error("Error getting buffered reader to data file at " + pathToDataFile);
			e1.printStackTrace();
		}
		
		// Get first line of data file and read the column names
	    String line = null;
		try {
			line = dataFileBufferedReader.readLine();
		} catch (IOException e) {
			logger.error("Error reading data file at " + pathToDataFile);
		    return;
		}
		
		Map<String, Integer> columnHeadersAndPosition = getColumnHeadersAndPositions(line);
		if(columnHeadersAndPosition.isEmpty()) {
			logger.error("Could not obtain header information of data file :" + pathToDataFile);
			return;
		}
		// Write header to output file
		try {
			outputFile.write(line.getBytes());
			outputFile.write(System.getProperty("line.separator").getBytes());
		} catch (IOException e1) {
			logger.error("Error writing header into output file :" + pathToOutputFile );
			e1.printStackTrace();
		}
		
		try {
			line = dataFileBufferedReader.readLine(); // Skip header line and move to first data line
			while (line != null) {
				processFileDataLine(extractOperations, columnHeadersAndPosition, line);
		        line = dataFileBufferedReader.readLine();
		    }
		} catch (IOException e) {
			logger.error("Error processing data line " + e.getMessage());
			e.printStackTrace();
		}
	    

	}
	
	private Map<String, Integer> getColumnHeadersAndPositions(String line) {
		Map<String, Integer> headerPositions = new HashMap<>();
		if(line == null || line.isEmpty()) {
			return headerPositions;
		}
		String[] headers = line.split(",");
		for(int position = 0;position<headers.length;position++) {
			headerPositions.put(headers[position].toLowerCase(), position);
		}
		return headerPositions;
	}

	protected void processFileDataLine(LinkedHashMap<Operation, String> operations, Map<String, Integer> columnHeadersAndPosition, String dataFileLine) {
		String applyDataTransformations = applyDataTransformations(operations, columnHeadersAndPosition, dataFileLine);
		try {
			outputFile.write(applyDataTransformations.getBytes());
			outputFile.write(System.getProperty("line.separator").getBytes());
		} catch (IOException e) {
			logger.error("Error writing to output file");
			e.printStackTrace();
		}
	}
	protected String applyDataTransformations(LinkedHashMap<Operation, String> operations, Map<String, Integer> columnHeadersAndPosition, String dataFileLine) {
		String[] inputLineSplits = dataFileLine.split(",");
		for(Entry<Operation, String> operationAndColumn: operations.entrySet()) {
			Operation dataTransformationOperation = operationAndColumn.getKey();
			String columnName = operationAndColumn.getValue();
			Integer columnPosition = columnHeadersAndPosition.get(columnName.toLowerCase());
			switch(dataTransformationOperation) {
			case SLUGIFY:
				inputLineSplits[columnPosition] = slugifyTransformer.transform(inputLineSplits[columnPosition]);
				break;
			case F_TO_C:
				inputLineSplits[columnPosition] = farenheitToCelsiusTransformer.transform(Double.parseDouble(inputLineSplits[columnPosition])).toString();
				break;
			case HST_TO_UNIX:
				String[] dateAndTimeColumns = columnName.split(",");
				Integer dateColumnPosition = columnHeadersAndPosition.get(dateAndTimeColumns[0].toLowerCase());
				Integer timeColumnPosition = columnHeadersAndPosition.get(dateAndTimeColumns[1].toLowerCase());
				String utcTime = htcToUtcTransformer.transform(inputLineSplits[dateColumnPosition] + " " + inputLineSplits[timeColumnPosition]);
				String[] utcTimeAndDate = utcTime.split(" ");
				inputLineSplits[dateColumnPosition] = utcTimeAndDate[0];
				inputLineSplits[timeColumnPosition] = utcTimeAndDate[1];
				break;
			default:
				logger.error("Encountered operation that is not supported -> " + dataTransformationOperation);
			}
			
		}
		return StringUtils.join(inputLineSplits, ",");
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		if(outputFile != null) {
			outputFile.flush();
			outputFile.close();
		}
		if(dataFileInputStream != null) {
			dataFileInputStream.close();
		}
		if(dataFileBufferedReader != null) {
			dataFileBufferedReader.close();
		}
	}
	
}
