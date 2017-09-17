package com.newrelic.datatransformers.model;

public class TransformCommand {

	private String operation;
	
	private String column;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
	
	
}
