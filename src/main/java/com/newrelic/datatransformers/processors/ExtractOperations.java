package com.newrelic.datatransformers.processors;

import java.util.LinkedHashMap;
import com.newrelic.datatransformers.model.Operation;

public interface ExtractOperations {

	/**
	 * Parses input string and extracts set of operations that should be performed on the data columns.
	 * Note: Order of the operations to be performed is maintained using a linked map.
	 * Also column name is lower cased.
	 * @param input
	 * @return
	 * 		A key,value pair of Operation to be performed, name of data(Column name) on which it should be applied.
	 * 		Empty map is returned in case of error.
	 */
	public LinkedHashMap<Operation, String> extractOperations(String input);
}
