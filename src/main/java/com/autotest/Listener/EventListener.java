package com.autotest.Listener;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.persist.api.Operation;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.events.api.general.ElementEventListener;

public class EventListener implements ElementEventListener {

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("beforeClickOn");
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		String fileName = TakeScreen.takeSreen(driver);
		String id = ((AppiumDriver<?>) driver).getSessionId().toString();
		String caseId = System.getProperty(id);
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)", id,
				String.format("After Click Element { %s }", element.toString()), fileName, caseId);
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("beforeChangeValueOf");
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub
		System.out.println("beforeChangeValueOf");
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("afterChangeValueOf");
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub
		System.out.println("afterChangeValueOf");
	}
}
