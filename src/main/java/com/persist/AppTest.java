package com.persist;

import com.persist.api.Operation;

public class AppTest {

	public static int id;
	public static long executeid;
	public static String status;
	public static String devices;
	public static String appName;
	public static int caseNumber;
	public static int getCaseNumber() {
		return caseNumber;
	}

	public static void setCaseNumber(int caseNumber) {
		AppTest.caseNumber = caseNumber;
	}

	public static int getPassNumber() {
		return passNumber;
	}

	public static void setPassNumber(int passNumber) {
		AppTest.passNumber = passNumber;
	}

	public static int getFailNumber() {
		return failNumber;
	}

	public static void setFailNumber(int failNumber) {
		AppTest.failNumber = failNumber;
	}

	public static int passNumber;
	public static int failNumber;
	
	public static String getAppName() {
		return appName;
	}

	public static void setAppName(String appName) {
		AppTest.appName = appName;
	}

	public static String getDevices() {
		return devices;
	}

	public static void setDevices(String devices) {
		if (devices.startsWith("[") & devices.endsWith("]")) {
			AppTest.devices = devices.substring(1, devices.length() - 1);
		} else {
			AppTest.devices = devices;
		}
	}

	public static int save() {
		return Operation.insertData("insert into apptest(executeId,devices,appName,caseNumber) value(?,?,?,?)", getExecuteid(),
				getDevices(), getAppName(),getCaseNumber());
	}

	public static void updateStatus() {
		Operation.insertData("update apptest set status=?,passNumber=?,failNumber=? where executeid=?",
				getStatus(), getExecuteid(),getPassNumber(),getFailNumber());
	}

	/**
	 * 当前是否有测试在执行
	 * 
	 * @return
	 */
	public String getRun() {
		return "run";
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		AppTest.id = id;
	}

	public static long getExecuteid() {
		return executeid;
	}

	public static void setExecuteid(long executeid) {
		AppTest.executeid = executeid;
	}

	public static String getStatus() {
		return status;
	}

	public static void setStatus(String status) {
		AppTest.status = status;
	}

}
