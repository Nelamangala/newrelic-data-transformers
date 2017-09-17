package com.newrelic.datatransformers.transformers;

import org.joda.time.DateTime;

public class HawaiiTimeToUnixTimeTransformer implements DataTransformer<DateTime> {

	/**
	 * Operates on datetimes. Assumes the source unit is Hawaii Standard Time (UTC-10). 
	 * Converts into the UTC time zone and into a UNIX timestamp format.
	 */
	@Override
	public DateTime transform(DateTime input) {
		// TODO Auto-generated method stub
		return null;
	}

}
