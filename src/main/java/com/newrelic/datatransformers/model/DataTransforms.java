package com.newrelic.datatransformers.model;

import java.util.List;

public class DataTransforms {

	private String spec_version;
	
	private List<TransformCommand> transforms;

	public String getSpec_version() {
		return spec_version;
	}

	public void setSpec_version(String spec_version) {
		this.spec_version = spec_version;
	}

	public List<TransformCommand> getTransforms() {
		return transforms;
	}

	public void setTransforms(List<TransformCommand> transforms) {
		this.transforms = transforms;
	}
	
	
}
