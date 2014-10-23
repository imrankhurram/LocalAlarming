package com.nextcontrols.bureaudomain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class UserAudit implements Serializable{

	
	private int userId;
	private Timestamp auditDate;
	private String actionType;
	private String actionDescription;
	private String siteCode;
	private String userName="";
	
	
	public UserAudit(int puserId, Timestamp pauditDate, String pactionType, String pactionDescription,String siteCode) {
		this.userId = puserId;
		this.auditDate = pauditDate;
		this.actionType = pactionType;
		this.actionDescription = pactionDescription;
		this.siteCode=siteCode;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Timestamp getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
