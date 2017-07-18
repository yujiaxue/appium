package com.autotest.entrypoint;

import org.openqa.selenium.By;

import com.android.uitest.config.LocatorType;

import io.appium.java_client.MobileBy;

public class TypeCheck {

	public static By getLocatorType(String locator) {
		if (locator.startsWith(LocatorType.XPATH.getArgument()) || locator.startsWith(LocatorType.XPATH2.getArgument())) {
			return By.xpath(locator);
		}
		if (locator.contains(LocatorType.ID.getArgument())) {
			return MobileBy.id(locator);
		}
		
		if (locator.startsWith("android")) {
			return By.className(locator);
		}
		if (locator.startsWith("~")) {
			String temp = locator.substring(1);
			return MobileBy.AccessibilityId(temp);
		}
		return By.id(locator);
	}
}
