package com.newrelic.datatransformers.processors;

import java.util.Map;

import com.newrelic.datatransformers.model.Operation;

public interface ExtractOperations {

	/**
	 * Parses input string and extracts set of operations that should be performed on the data columns.
	 * @param input
	 * @return
	 */
	public Map<Operation, String> extractOperations(String input);
}
