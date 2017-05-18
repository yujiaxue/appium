package com.persist;

import com.persist.api.Operation;

public class AppTest {

	public static int id;
	public static long executeid;
	public static String status;

	public static void save() {
		Operation.insertData("insert into apptest(executeId) value(?)", getExecuteid());
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
