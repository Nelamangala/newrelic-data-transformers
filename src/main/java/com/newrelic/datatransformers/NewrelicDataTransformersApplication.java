package com.newrelic.datatransformers;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NewrelicDataTransformersApplication {

	private static Logger logger = Logger.getLogger(NewrelicDataTransformersApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(NewrelicDataTransformersApplication.class, args);
		if(args.length != 3) {
			logger.info("Usage : java <jar> <pathToCommandsJson> <pathToInputCsv> <pathToOutput>");
		}
	}
}
