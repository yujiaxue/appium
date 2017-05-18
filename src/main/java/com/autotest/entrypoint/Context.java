package com.autotest.entrypoint;

import java.util.HashMap;

import com.driver.manage.FileUtils;

public class Context {
	public static HashMap<String, String> config = FileUtils.load().loadProperties();;
	
}
