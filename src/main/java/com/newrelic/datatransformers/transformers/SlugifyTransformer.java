package com.newrelic.datatransformers.transformers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class SlugifyTransformer implements DataTransformer<String> {

	private final Pattern specialCharsMatchingPattern = Pattern.compile("[^\\w\\s]");
	/**
	 * Slugifies input. Converts all whitespace into hyphens, lowercases all letters, and removes all other punctuation.
	 */
	@Override
	public String transform(String input) {
		if(input == null || input.isEmpty()) {
			return input;
		}
		
		String inputWithoutSpecialChars = removeSpecialCharacters(input);
		String inputLowerCase = inputWithoutSpecialChars.toLowerCase();
		return replaceSpaceWithHyphen(inputLowerCase);
	}

	public String replaceSpaceWithHyphen(String input) {
		input = input.trim(); // Remove any leading & trailing spaces first
		input = input.replaceAll("(\\s+)"," ").replaceAll("(\\t+)"," ").replaceAll(" ", "-");
        return input;
	}

	public String removeSpecialCharacters(String input) {
		Matcher match= specialCharsMatchingPattern.matcher(input);
        while(match.find())
        {
            String specialChar = match.group();
            input =  input.replaceAll("\\"+specialChar, "");
        }
        return input;
	}

}
