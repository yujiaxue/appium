package com.autotest.entrypoint;

import org.openqa.selenium.WebElement;

import com.autotest.assertion.Assertion;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class EntryIOS implements IEntryPoint {
	AppiumDriver<?> driver = null;
	String sessionId = null;
	String caseId = null;
	TouchAction touchAction = null;
	String device = null;
	int casePass = 0;
	int caseFail = 0;

	public EntryIOS(AppiumDriver<WebElement> driver,String device) {
		this.driver = driver;
		this.device = device;
		touchAction = new TouchAction(this.driver);
		this.sessionId = this.driver.getSessionId().toString();
		Assertion.setAttr(this.driver, this.sessionId, device);
	}

}
