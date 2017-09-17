package com.newrelic.datatransformers.transformers;

import static org.hamcrest.CoreMatchers.is;

import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HawaiiTimeToUnixTimeTransformer.class})
public class HawaiiTimeToUnixTimeTransformerTest {

	@Autowired
	private HawaiiTimeToUnixTimeTransformer htcToUtcTransformer;
	
	@Test
	public void testTimeFormatterCreation() {
		Assert.assertNotNull(htcToUtcTransformer.getDateFormatter());
		Assert.assertThat(htcToUtcTransformer.getDateFormatter().getZone(), is(DateTimeZone.forID("US/Hawaii")));
	}
	
	@Test
	public void testHtcToUtcTransformMorning() {
		String inputHtc = "09/17/17 07:30:00";
		String expectedUtc = "09/17/17 17:30:00";
		Assert.assertThat(htcToUtcTransformer.transform(inputHtc), is(expectedUtc));
	}
	
	@Test
	public void testHtcToUtcTransformNight() {
		String inputHtc = "09/17/17 23:33:02";
		String expectedUtc = "09/18/17 09:33:02";
		Assert.assertThat(htcToUtcTransformer.transform(inputHtc), is(expectedUtc));
	}
}
