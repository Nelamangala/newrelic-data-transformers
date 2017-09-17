package com.newrelic.datatransformers.transformers;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class HawaiiTimeToUnixTimeTransformer implements DataTransformer<String > {

	/**
	 * Operates on datetimes. Assumes the source unit is Hawaii Standard Time (UTC-10). 
	 * Converts into the UTC time zone and into a UNIX timestamp format.
	 * Input format should be MM/dd/yy HH:mm:ss Example "09/29/16 18:33:00"
	 */
	private static String DATE_TIME_FORMAT = "MM/dd/yy HH:mm:ss";
	private DateTimeFormatter hawaiiTimeFormatter;
	
	@PostConstruct
	public void initialize() {
		hawaiiTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT).withZone(DateTimeZone.forID("US/Hawaii"));
	}
	
	@Override
	public String transform(String input) {
		if(input == null || input.isEmpty()) {
			return null;
		}
		DateTime parseDateTime = hawaiiTimeFormatter.parseDateTime(input).withZone(DateTimeZone.UTC);
		
		return parseDateTime.toString(DATE_TIME_FORMAT);
	}
	
	/**
	 * Get time formatter being used.
	 * @return
	 */
	public DateTimeFormatter getDateFormatter() {
		return hawaiiTimeFormatter;
	}

}
