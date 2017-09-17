package com.newrelic.datatransformers.model;

public enum Operation {

	SLUGIFY("slugify"), F_TO_C("f-to-c"), HST_TO_UNIX("hst-to-unix");
	
	private String operation;
	
	Operation(String operation) {
		this.setOperation(operation);
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	/**
	 * Performs case insensitive match to the string representation of operation.
	 * @param keyOperation
	 * @return
	 * 		NULL if matching enum for given string is not found. Operation value if match found.
	 */
	public static Operation find(String keyOperation) {
		Operation searchResult = null;
		
		for(Operation op : Operation.values()) {
			if(op.operation.equalsIgnoreCase(keyOperation)) {
				searchResult = op;
				break;
			}
		}
		
		return searchResult;
	}
	
}
