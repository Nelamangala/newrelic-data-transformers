package com.newrelic.datatransformers.transformers;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {FarenheitToCelsiusTransformer.class})
public class FarenheitToCelsiusTransformerTest {

	@Autowired
	private FarenheitToCelsiusTransformer celsiusToFarenheitTransformer;
	
	@Test
	public void testFarenheitToCelsiusConversion() {
		Number transform = celsiusToFarenheitTransformer.transform(32);
		Assert.assertNotNull(transform);
		Assert.assertThat(transform, is(0.0));
	}
	
	@Test
	public void testZeroFarenheitToCelsiusConversion() {
		Number transform = celsiusToFarenheitTransformer.transform(0);
		Assert.assertNotNull(transform);
		Assert.assertEquals(-17.777d, transform.doubleValue(), 0.001d);
	}
}
