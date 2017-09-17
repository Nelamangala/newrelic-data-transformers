package com.newrelic.datatransformers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.newrelic.datatransformers.processors.DataFileProcessor;

@SpringBootApplication
public class NewrelicDataTransformersApplication implements CommandLineRunner{

	private static Logger logger = Logger.getLogger(NewrelicDataTransformersApplication.class);
		
	@Autowired
	DataFileProcessor dataFileProcessor;
	
	public static void main(String[] args) {
		SpringApplication.run(NewrelicDataTransformersApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(args.length != 3) {
			logger.info("Usage : java <jar> com.newrelic.datatransformers.NewrelicDataTransformersApplication <pathToCommandsJson> <pathToInputCsv> <pathToOutput>");
			return;
		}
		dataFileProcessor.processDataFile(args[0], args[1], args[2]);
		
	}
}
