package com.newrelic.datatransformers.integration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.newrelic.datatransformers.processors.DataFileProcessor;
import com.newrelic.datatransformers.processors.JsonToOperationsProcessor;
import com.newrelic.datatransformers.transformers.FarenheitToCelsiusTransformer;
import com.newrelic.datatransformers.transformers.HawaiiTimeToUnixTimeTransformer;
import com.newrelic.datatransformers.transformers.SlugifyTransformer;

/**
 * Tests overall functionality of the application. Reads transformationCommands from json file and applies to a data file.
 * Verifies if the output matches to expected output file.
 * @author Ganesh
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataFileProcessor.class, JsonToOperationsProcessor.class, FarenheitToCelsiusTransformer.class,
		HawaiiTimeToUnixTimeTransformer.class, SlugifyTransformer.class})
public class NewrelicDataTransformersApplicationIntegrationTest {

	@Autowired
	private DataFileProcessor dataFileProcessor;
	
	@Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Test
	public void testDataTransformerApplication() throws IOException {
		ClassPathResource testTransformCommands = new ClassPathResource("TransformCommands.json");
		ClassPathResource testDataFile = new ClassPathResource("testDataFile.csv");
		ClassPathResource testExpectedOutputFile = new ClassPathResource("testExpectedOutput.csv");
	
		File tmpFile = tempFolder.newFile();
		File resourcesDirectory = new File("src/test/resources");
	    String testDirPath = resourcesDirectory.getAbsolutePath() + "/";
		dataFileProcessor.processDataFile(testDirPath + testTransformCommands.getPath(), testDirPath + testDataFile.getPath(), tmpFile.getAbsolutePath());
		Assert.assertTrue(FileUtils.contentEquals(tmpFile, testExpectedOutputFile.getFile()));
		
	}
	
}
