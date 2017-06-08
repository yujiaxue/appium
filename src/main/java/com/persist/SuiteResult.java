package com.persist;

import com.persist.api.Operation;

public class SuiteResult {

	private static String tablename = "suiteResult";
	public int id;
	public static long executeId;
	public static String device;
	public static int casePass;
	public static int caseFail;
	public static int caseNumber;
	private static String insertSql = String
			.format("insert into %s(executeId,device,casePass,caseFail,caseNumber) values(?,?,?,?,?)", tablename);
	private static String updateSql = String
			.format("update %s set casePass=?,caseFail=?,caseNumber=? where executeId=? and device=?", tablename);

	public static void save() {
		Operation.insertData(insertSql, executeId, device, casePass, caseFail, caseNumber);
	}

	public static void update() {
		Operation.insertData(updateSql, casePass, caseFail, caseNumber, executeId, device);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getExecuteId() {
		return executeId;
	}

	public static void setExecuteId(long executeId) {
		SuiteResult.executeId = executeId;
	}

	public String getDevice() {
		return device;
	}

	public static void setDevice(String device) {
		SuiteResult.device = device;
	}

	public int getCasePass() {
		return casePass;
	}

	public static void setCasePass(int casePass) {
		SuiteResult.casePass = casePass;
	}

	public int getCaseFail() {
		return caseFail;
	}

	public static void setCaseFail(int caseFail) {
		SuiteResult.caseFail = caseFail;
	}

	public int getCaseNumber() {
		return caseNumber;
	}

	public static void setCaseNumber(int caseNumber) {
		SuiteResult.caseNumber = caseNumber;
	}

}
