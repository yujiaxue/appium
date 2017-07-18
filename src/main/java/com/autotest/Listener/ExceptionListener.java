package com.autotest.Listener;

import org.openqa.selenium.WebDriver;

import com.android.uitest.config.Status;
import com.persist.api.Operation;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.events.api.general.ListensToException;

public class ExceptionListener implements ListensToException {

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println("onException............................start");
		String st = throwable.getCause().getMessage();
		//StringBuffer sb = new StringBuffer();
		System.out.println(st);
		// for(int i=0;i<st.length;i++){
		// System.out.println(st[i]);
		// sb.append(st[i]);
		// }
		System.out.println("onException............................end");
		// String id = ((AppiumDriver<?>) driver).getSessionId().toString();
		// String device = ((AppiumDriver<?>)
		// driver).getCapabilities().getCapability("udid").toString();
		// String caseId = System.getProperty(id);
		//
		// String fileName = TakeScreen.takeSreen(driver);
		// Operation.insertData("insert into
		// executeDetail(sessionId,stepName,imageName,caseId,deviceName)
		// values(?,?,?,?,?)",
		// id, "异常中断:"+sb.toString(), fileName,caseId,device);
		// Operation.insertData("insert into case_result(caseid,deviceid,status)
		// values(?,?,?)", caseId,device,Status.FAIL.getArgument());
	}

}
