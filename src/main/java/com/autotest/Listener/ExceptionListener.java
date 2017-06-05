package com.autotest.Listener;

import org.openqa.selenium.WebDriver;

import com.persist.api.Operation;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.events.api.general.ListensToException;

public class ExceptionListener implements ListensToException{

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("onException............................start");
		java.lang.StackTraceElement[] st = throwable.getStackTrace();
		for(int i=0;i<st.length;i++){
			System.out.println(st[i]);
		}
		System.out.println("onException............................end");
		String id = ((AppiumDriver<?>) driver).getSessionId().toString();
		String device = ((AppiumDriver<?>) driver).getCapabilities().getCapability("udid").toString();
		String caseId = System.getProperty(id);
		
		String fileName = TakeScreen.takeSreen(driver);
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId,deviceName) values(?,?,?,?,?)",
				id, "异常中断", fileName,caseId,device);
	}

}
