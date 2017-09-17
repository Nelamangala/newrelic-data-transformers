package com.newrelic.datatransformers.transformers;

import org.springframework.stereotype.Component;

@Component
public class FarenheitToCelsiusTransformer implements DataTransformer<Number> {

	/**
	 * Operates on numerics. Assumes the source unit is degrees Fahrenheit. 
	 * Converts into degrees Celsius. The formula for this is: T(°C) = (T(°F) - 32) / 1.8
	 */
	@Override
	public Number transform(Number farenheit) {
		if(farenheit == null) {
			return farenheit;
		}
		return (farenheit.doubleValue() - 32) / 1.8d;
	}

}
