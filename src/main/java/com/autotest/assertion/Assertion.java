package com.autotest.assertion;

import org.testng.Assert;

import com.android.uitest.config.Status;
import com.persist.CaseResult;
import com.persist.ExecuteDetail;
import com.persist.api.Operation;

import io.appium.java_client.AppiumDriver;

public class Assertion {

	static AppiumDriver<?> driver = null;
	static String sessionId = null;
	static String deviceId = null;

	public static void setAttr(AppiumDriver<?> entry, String id, String device) {
		driver = entry;
		sessionId = id;
		deviceId = device;
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
		ExecuteDetail.save(sessionId, "assertContains: " + actual + " contains " + expect, "",
				System.getProperty(sessionId), deviceId);
		if (actual.contains(expect)) {

		} else {
			failCase();
		}
	}

	public static void assertEquals(String expect, String actual) {
		ExecuteDetail.save(sessionId, "assertEquals: " + expect + " vs " + actual, "", System.getProperty(sessionId),
				deviceId);
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
		ExecuteDetail.save(sessionId, String.format("element not found { %s },%s", locator, log), "", caseid, deviceId);
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
		CaseResult.save(caseid, devicename, Status.FAIL.getArgument());
		fail();
	}
}
