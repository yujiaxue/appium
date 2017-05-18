package com.android.uitest.config;

public enum LocatorType {

	ID(":id/"),
	
	CLASS("class"),
	
	

	XPATH("//");

	private final String args;
	LocatorType(String args){
		this.args = args;
	}

	public String getArgument() {
		return args;
	}
}
