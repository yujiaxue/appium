package com.persist;

import com.persist.api.Operation;

public class CaseResult {

	public String id;
	public String caseid;
	public String deviceid;
	public String status;
	public static String sql = "insert into case_result(caseid,deviceid,status) values(?,?,?)";
	
	public static void save(String caseid,String deviceid,String status){
		Operation.insertData("insert into case_result(caseid,deviceid,status) values(?,?,?)", caseid, deviceid,
				status);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCaseid() {
		return caseid;
	}
	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
