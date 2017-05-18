package com.android.uitest.config;

public enum AutoMation {

	ANDROID("android"),
	
	IOS("ios"),
	
	CHROME("chrome"),
	
	SAFARI("safari");
	
	
	private final String arg;

	AutoMation(String arg) {
        this.arg = arg;
    }

    public String getArgument() {
        return arg;
    }
}
