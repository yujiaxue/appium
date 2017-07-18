package com.autotest.assertion;

import org.testng.Assert;

import com.android.tools.Common;
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

	/**
	 * 判断视图存在
	 */
	public static void assertViewExist(String log) {

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

	/**
	 * 判断expect 包含actual为真，否则用例失败
	 * 
	 * @param expect
	 * @param actual
	 */
	public static void assertContains(String actual, String expect, String log) {

		if (actual.contains(expect) || expect.contains(actual)) {
			ExecuteDetail.save(sessionId,
					String.format("assertContains Success: %s,{contain for: %s vs %s}", log, actual, expect), "",
					System.getProperty(sessionId), deviceId);
		} else {
			ExecuteDetail.save(sessionId,
					String.format("assertContains Fail: %s,{contain for: %s vs %s}", log, actual, expect), "",
					System.getProperty(sessionId), deviceId);
			failCase();
		}
	}

	public static void assertEquals(String expect, String actual) {
		assertEquals(expect, actual, "");
	}

	public static void assertEquals(String actual, String expect, String log) {

		if (expect.equals(actual)) {
			ExecuteDetail.save(sessionId,
					String.format("assertEquals Success: %s, {%s vs %s}", log, actual, expect, actual), "",
					System.getProperty(sessionId), deviceId);
		} else {
			ExecuteDetail.save(sessionId,
					String.format("assertEquals Fail: %s, {%s vs %s}", log, actual, expect, actual), "",
					System.getProperty(sessionId), deviceId);
			failCase();
		}
	}

	public static void assertEquals(double expect, double actual) {
		assertEquals(expect, actual, "");
	}

	public static void assertEquals(Double actual, Double expect, String log) {

		if (expect.equals(actual)) {
			ExecuteDetail.save(sessionId,
					String.format("assertEquals Success: %s, {%s vs %s}", log, actual, expect, actual), "",
					System.getProperty(sessionId), deviceId);
		} else {
			ExecuteDetail.save(sessionId,
					String.format("assertEquals Fail: %s, {%s vs %s}", log, actual, expect, actual), "",
					System.getProperty(sessionId), deviceId);
			failCase();
		}
	}

	public static void assertEquals(int expect, int actual) {
		assertEquals(expect, actual, "");
	}

	public static void assertEquals(Integer actual, Integer expect, String log) {

		if (expect.equals(actual)) {
			ExecuteDetail.save(sessionId,
					String.format("assertEquals Success: %s, {%s vs %s}", log, actual, expect, actual), "",
					System.getProperty(sessionId), deviceId);
		} else {
			ExecuteDetail.save(sessionId,
					String.format("assertEquals Fail: %s, {%s vs %s}", log, actual, expect, actual), "",
					System.getProperty(sessionId), deviceId);
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
		ExecuteDetail.save(sessionId, String.format("元素没有被找到 { %s },%s", locator, log), "", caseid, deviceId);
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

	/**
	 * actual包含containedText数组中的任何一个返回true，否则false
	 * 
	 * @param actual
	 * @param containedText
	 */
	public static void assertContainsOr(String actual, String... containedText) {
		boolean temp = false;
		for (String expect : containedText) {
			if (actual.contains(expect)) {
				temp = true;
			}
		}
		if (temp) {
			ExecuteDetail.save(sessionId,
					"Success:assertContainsOr " + actual + " contains " + Common.ArrayToString(containedText), "",
					System.getProperty(sessionId), deviceId);
		} else {
			ExecuteDetail.save(sessionId,
					"Fail:assertContainsOr " + actual + " contains " + Common.ArrayToString(containedText), "",
					System.getProperty(sessionId), deviceId);
			failCase();
		}
	}

	/**
	 * actual相等于containedText数组中的任何一个返回true，否则false
	 * 
	 * @param actual
	 * @param containedText
	 */
	public static void assertEqualsOr(String actual, String... equalsedText) {
		boolean temp = false;
		for (String expect : equalsedText) {
			if (actual.equals(expect)) {
				temp = true;
			}
		}
		if (temp) {
			ExecuteDetail.save(sessionId,
					"Success:assertEqualsOr " + actual + " equals " + Common.ArrayToString(equalsedText), "",
					System.getProperty(sessionId), deviceId);
		} else {
			ExecuteDetail.save(sessionId,
					"Fail:assertEqualsOr " + actual + " equals " + Common.ArrayToString(equalsedText), "",
					System.getProperty(sessionId), deviceId);
			failCase();
		}
	}

	/**
	 * actual包含所有containedText数组中的值返回true，否则false
	 * 
	 * @param actual
	 * @param containedText
	 */
	public static void assertContainsAnd(String actual, String... containedText) {
		boolean temp = true;
		for (String expect : containedText) {
			if (!actual.contains(expect)) {
				temp = false;
			}
		}
		if (temp) {
			ExecuteDetail.save(sessionId,
					"Success:assertContainsAnd " + actual + " contains " + Common.ArrayToString(containedText), "",
					System.getProperty(sessionId), deviceId);
		} else {
			ExecuteDetail.save(sessionId,
					"Fail:assertContainsAnd " + actual + " contains " + Common.ArrayToString(containedText), "",
					System.getProperty(sessionId), deviceId);
			failCase();
		}
	}

	/**
	 * sort==true 则pass 否则fail
	 * 
	 * @param sort
	 */
	public static void assertTrue(boolean sort) {
		assertTrue(sort,"布尔验证");

	}

	/**
	 * sort==true 则pass 否则fail
	 * 
	 * @param sort
	 */
	public static void assertTrue(boolean sort, String log) {
		if (sort) {
			ExecuteDetail.save(sessionId, "Success:assertTrue " + log, "", System.getProperty(sessionId), deviceId);
		} else {
			ExecuteDetail.save(sessionId, "Fail:assertTrue " + log, "", System.getProperty(sessionId), deviceId);
			failCase();
		}

	}
}
