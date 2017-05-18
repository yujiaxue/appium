package com.autotest.entrypoint;

import java.util.Map;

import io.appium.java_client.AppiumDriver;

public interface IDriver {
	public AppiumDriver<?> genDriver(Map<String,String> config);
}
