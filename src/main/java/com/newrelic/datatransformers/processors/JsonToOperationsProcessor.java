package com.newrelic.datatransformers.processors;

import java.io.IOException;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newrelic.datatransformers.model.DataTransforms;
import com.newrelic.datatransformers.model.Operation;
import com.newrelic.datatransformers.model.TransformCommand;

/**
 * Process a json file and extract commands that should be performed.
 * @author Ganesh
 *
 */
@Component
public class JsonToOperationsProcessor implements ExtractOperations{

	private ObjectMapper objectMapper = new ObjectMapper();
	private Logger logger = Logger.getLogger(JsonToOperationsProcessor.class);
	
	@Override
	public LinkedHashMap<Operation, String> extractOperations(String input) {
		DataTransforms inputOperations = null;
		LinkedHashMap<Operation, String> dataOperations = new LinkedHashMap<>();
		try {
			inputOperations = objectMapper.readValue(input, DataTransforms.class);
		} catch (IOException e) {
			logger.error("Error while parsing json");
			e.printStackTrace();
			return dataOperations;
		}
		
		for(TransformCommand transform : inputOperations.getTransforms()) {
			Operation operation = Operation.find(transform.getOperation());
			if(operation == null) {
				logger.error("Could not find matching operation for : " + transform.getOperation());
				continue;
			}
			if(dataOperations.get(operation) != null) {
				// Remove if operation was previously specified & insert again so that the order of operations is maintained.
				dataOperations.remove(operation);
			}
			dataOperations.put(operation, transform.getColumn().toLowerCase());
		}
		
		return dataOperations;
	}

}
