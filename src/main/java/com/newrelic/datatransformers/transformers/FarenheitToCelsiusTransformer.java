package com.newrelic.datatransformers.transformers;

public class FarenheitToCelsiusTransformer implements DataTransformer<Number> {

	/**
	 * Operates on numerics. Assumes the source unit is degrees Fahrenheit. 
	 * Converts into degrees Celsius. The formula for this is: T(°C) = (T(°F) - 32) / 1.8
	 */
	@Override
	public Number transform(Number input) {
		// TODO Auto-generated method stub
		return null;
	}

}
