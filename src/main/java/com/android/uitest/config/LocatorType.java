package com.android.uitest.config;

public enum LocatorType {

	ID(":id/"),
	
	CLASS("class"),
	
	

	XPATH("//"),
	XPATH2("xpath=");

	private final String args;
	LocatorType(String args){
		this.args = args;
	}

	public String getArgument() {
		return args;
	}
}
