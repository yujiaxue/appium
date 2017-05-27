package com.persist;

import java.util.Date;

import com.persist.api.Operation;

public class ExecuteDetail {

	public int id;
	public String sessionId;
	public String stepName;
	public String imageName;
	public Date createTime;
	public String deviceName;
	public static String sql = "insert into executeDetail(sessionId,stepName,imageName,caseId) values(?,?,?,?)";

	/**
	 * 保存
	 */
	public static void save(String sessionId, String stepName, String imageName, String caseId, String deviceName) {
		Operation.insertData(sql, sessionId, stepName, imageName, System.getProperty(sessionId), deviceName);
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
