package com.autotest.assertion;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.android.uitest.config.Status;
import com.persist.api.Operation;

import io.appium.java_client.AppiumDriver;

public class Assertion {

	static AppiumDriver<?> driver = null;
	static String sessionId = null;

	public static void setAttr(AppiumDriver<?> entry, String id) {
		driver = entry;
		sessionId = id;
	}

	public static void assertViewExist() {

	}

	public static void fail() {
		Assert.fail();
	}

	/**
	 * 判断expect 包含actual为真，否则用例失败
	 * 
	 * @param expect
	 * @param actual
	 */
	public static void assertContains(String actual, String expect) {
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
				sessionId, "assertContains: " + actual + " contains " + expect, "", System.getProperty(sessionId));
		if (actual.contains(expect)) {

		} else {
			failCase();
		}
	}

	public static void assertEquals(String expect, String actual) {
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
				sessionId, "assertEquals: "+expect + " vs " + actual, "", System.getProperty(sessionId));
		if (expect.equals(actual)) {
			
		} else {
			failCase();
		}
	}

	/**
	 * 没有找到定位器相应的元素
	 * 
	 * @param locator
	 * @param log
	 * @param caseid
	 */
	public static void noElementFail(String locator, String log, String caseid) {
		// 用例失败标记，
		// 脚本中断退出，
		Operation.insertData("insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)",
				sessionId, String.format("element not found { %s },%s", locator, log), "", caseid);
		failCase();
	}

	/**
	 * 标记用例结果
	 * 
	 * @param string
	 *            用例ID
	 * @param deviceid
	 *            设备名称
	 * @param status
	 *            用例结果
	 */
	public static void failCase() {
		String devicename = driver.getCapabilities().getCapability("udid").toString();
		String caseid = System.getProperty(driver.getSessionId().toString());
		// Map<String, String> r = Operation.getData("select id from case_result
		// where caseid=? and deviceid=?", caseid,
		// devicename);

		Operation.insertData("insert into case_result(caseid,deviceid,status) values(?,?,?)", caseid, devicename,
				Status.FAIL.getArgument());
		fail();
	}
}
