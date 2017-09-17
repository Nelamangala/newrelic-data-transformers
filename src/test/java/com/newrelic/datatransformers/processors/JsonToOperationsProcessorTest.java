package com.newrelic.datatransformers.processors;

import static org.hamcrest.CoreMatchers.is;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newrelic.datatransformers.model.Operation;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JsonToOperationsProcessor.class})
public class JsonToOperationsProcessorTest {

	@Autowired
	private JsonToOperationsProcessor jsonToOperationsProcessor;
	
	@Test
	public void testLatestOperationOnlyIsRetained() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
		ClassPathResource sourceData = new ClassPathResource("TransformCommands.json");
		
		String jsonCommands = mapper.readTree(sourceData.getInputStream()).toString();
		LinkedHashMap<Operation, String> extractOperations = jsonToOperationsProcessor.extractOperations(jsonCommands);
		
		Assert.assertNotNull(extractOperations);
		Assert.assertTrue(extractOperations.size() == 3);
		Assert.assertThat(extractOperations.get(Operation.HST_TO_UNIX), is("recordeddate,timesunset"));
	}
	
	@Test
	public void testOrderOfOperationsParsedFromJson() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
		ClassPathResource sourceData = new ClassPathResource("TransformCommands.json");
		
		String jsonCommands = mapper.readTree(sourceData.getInputStream()).toString();
		LinkedHashMap<Operation, String> extractOperations = jsonToOperationsProcessor.extractOperations(jsonCommands);
		
		Assert.assertNotNull(extractOperations);
		Iterator<Entry<Operation, String>> iterator = extractOperations.entrySet().iterator();
		Assert.assertThat(iterator.next().getKey(), is(Operation.SLUGIFY));
		Assert.assertThat(iterator.next().getKey(), is(Operation.F_TO_C));
		Assert.assertThat(iterator.next().getKey(), is(Operation.HST_TO_UNIX));
	}
}
