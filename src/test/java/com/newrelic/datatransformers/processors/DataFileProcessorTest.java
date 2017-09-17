package com.newrelic.datatransformers.processors;

import static org.hamcrest.CoreMatchers.is;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.newrelic.datatransformers.model.Operation;
import com.newrelic.datatransformers.transformers.FarenheitToCelsiusTransformer;
import com.newrelic.datatransformers.transformers.HawaiiTimeToUnixTimeTransformer;
import com.newrelic.datatransformers.transformers.SlugifyTransformer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataFileProcessor.class, JsonToOperationsProcessor.class, FarenheitToCelsiusTransformer.class,
		HawaiiTimeToUnixTimeTransformer.class, SlugifyTransformer.class})
public class DataFileProcessorTest {

	@Autowired
	private DataFileProcessor dataFileProcessor;
	
	@Test
	public void testApplyTransformations() {
		String inputLine = "9/29/16,23:15:22,Browsebug Npath,1.23,49,30.46,80,101.18,4.5,6:13:00,18:13:00";
		String expectedAfterTransformation = "09/30/16,23:15:22,browsebug-npath,1.23,9.444444444444445,30.46,80,101.18,4.5,6:13:00,04:13:00";
		Map<String, Integer> columnHeadersAndPositions = dataFileProcessor.getColumnHeadersAndPositions("RecordedDate,RecordedTime,RecordLocation,Radiation,Temperature,Pressure,Humidity,WindDirection,WindSpeed,TimeSunRise,TimeSunSet");
		LinkedHashMap<Operation, String> operations = new LinkedHashMap<>();
		operations.put(Operation.SLUGIFY, "recordlocation");
		operations.put(Operation.F_TO_C, "temperature");
		operations.put(Operation.HST_TO_UNIX, "recordeddate,timesunset");
		String applyDataTransformations = dataFileProcessor.applyDataTransformations(operations, columnHeadersAndPositions, inputLine);
		Assert.assertThat(applyDataTransformations, is(expectedAfterTransformation));
	}
}
