package com.autotest.Listener;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.autotest.entrypoint.Context;
import com.persist.api.Operation;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.events.api.general.SearchingEventListener;

public class ActionListener extends Context implements SearchingEventListener {

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		String fileName = TakeScreen.takeSreen(driver);
		// if (element == null) {
		// System.out.println("null====");
		// } else {
		// System.out.println("------not null " + element.getText());
		// }
		String id = ((AppiumDriver<?>) driver).getSessionId().toString();
		String caseId = System.getProperty(id);
		System.out.println(
				"afterFindBy-------" + (MobileElement) element + "----" + by.toString() + "---" + driver.toString());
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)", id,
				String.format("Before Find Element { %s }", by.toString()), fileName, caseId);
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
	}

}
