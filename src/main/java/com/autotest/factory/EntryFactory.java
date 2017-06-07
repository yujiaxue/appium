package com.autotest.factory;

import java.util.HashMap;

import org.openqa.selenium.WebElement;

import com.android.uitest.config.AutoMation;
import com.android.uitest.driver.AppAndroidDriver;
import com.android.uitest.driver.AppIOSDriver;
import com.android.uitest.driver.UIFlags;
import com.autotest.entrypoint.EntryPoint;

import io.appium.java_client.AppiumDriver;

public class EntryFactory {

	public static EntryPoint genDriver(HashMap<String, String> config) {
		String automation = config.get(UIFlags.AUTOMATION_DIRECT).trim();
		AppiumDriver<WebElement> driver = null;
		if (automation.equalsIgnoreCase(AutoMation.ANDROID.getArgument())) {
			driver = new AppAndroidDriver().genDriver(config);
		} else if (automation.equalsIgnoreCase(AutoMation.IOS.getArgument())) {
			driver = new AppIOSDriver().genDriver(config);
		} else if (automation.equalsIgnoreCase(AutoMation.CHROME.getArgument())) {

		} else if (automation.equalsIgnoreCase(AutoMation.SAFARI.getArgument())) {

		}
		EntryPoint point = new EntryPoint(driver,config.get(UIFlags.UDID));
		return point;
	}

}
