package com.newrelic.datatransformers.transformers;

public interface DataTransformer<T> {

	/**
	 * Applies data transformation to the input and returns it.
	 * @param input
	 * @return
	 * 		Applies a data transformation to input and returns the transformed value. null if input is null or invalid.
	 */
	public T transform(T input);
}
