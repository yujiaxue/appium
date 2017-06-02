package com.persist;

import com.persist.api.Operation;

public class AppTest {

	public static int id;
	public static long executeid;
	public static String status;
	public static String devices;
	public static String appName;
	
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
		AppTest.devices = devices;
	}

	public static int save() {
		return Operation.insertData("insert into apptest(executeId,devices,appName) value(?,?,?)", getExecuteid(),getDevices(),getAppName());
	}

	public static void updateStatus() {
		Operation.insertData("update apptest set status=? where executeid=?", getStatus(), getExecuteid());
	}
	/**
	 * 当前是否有测试在执行
	 * @return
	 */
	public String getRun(){
		return "run";
	}

	public static  int getId() {
		return id;
	}

	public static  void setId(int id) {
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
