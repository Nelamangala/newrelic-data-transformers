package com.newrelic.datatransformers.transformers;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SlugifyTransformer.class})
public class SlugifyTransformerTest {

	@Autowired
	private SlugifyTransformer slugifyTransformer;
	
	@Test
	public void testRemoveSpecialChars() {
		String input = "new, '#relic how are u?!";
		String expectedAfterSpecialCharRemoval = "new relic how are u";
		String result = slugifyTransformer.removeSpecialCharacters(input);
		Assert.assertThat(result, is(expectedAfterSpecialCharRemoval));
	}
	
	@Test
	public void testSpaceToHyphen() {
		String input = "new relic	 how 				are  u";
		String expectedAfterSpecialCharRemoval = "new-relic-how-are-u";
		String result = slugifyTransformer.replaceSpaceWithHyphen(input);
		Assert.assertThat(result, is(expectedAfterSpecialCharRemoval));
	}
	
	@Test
	public void testSlugify() {
		String input = "Hello 	new, '#RELIC How  are 			u?!";
		String expectedAfterSpecialCharRemoval = "hello-new-relic-how-are-u";
		String result = slugifyTransformer.transform(input);
		Assert.assertThat(result, is(expectedAfterSpecialCharRemoval));
	}
}
