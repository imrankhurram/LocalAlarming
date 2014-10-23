package com.nextcontrols.bureaudomain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AlarmAsset implements Serializable{
	
	private int alarmAssetId;
	private int departmentId;
	private String fixtureName;
	private String fixtureType;
	private BigDecimal stockValue;
	private float lowTemp;
	private float highTemp;
	private float lowTempOffset;
	private float highTempOffset;
	private Date installationDate;
	private String currency;
	
	private boolean chosen;
	private String deptName="";
	
	public AlarmAsset(){}
	
	public AlarmAsset(int alarmAssetId,int departmentId, String fixtureName, String fixtureType, BigDecimal stockValue, float lowTemp,
			float highTemp,float lowTempOffset,float highTempOffset,Date installationDate,String currency){
		
		this.alarmAssetId=alarmAssetId;
		this.departmentId=departmentId;
		this.fixtureName=fixtureName;
		this.fixtureType=fixtureType;
		this.stockValue=stockValue;
		this.lowTemp=lowTemp;
		this.highTemp=highTemp;
		this.lowTempOffset=lowTempOffset;
		this.highTempOffset=highTempOffset;
		this.installationDate=installationDate;
		this.currency = currency;
		this.chosen=false;
	}
	
	public void setAlarmAssetId(int alarmAssetId) {
		this.alarmAssetId = alarmAssetId;
	}
	public int getAlarmAssetId() {
		return alarmAssetId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setFixtureName(String fixtureName) {
		this.fixtureName = fixtureName;
	}
	public String getFixtureName() {
		return fixtureName;
	}
	public void setFixtureType(String fixtureType) {
		this.fixtureType = fixtureType;
	}
	public String getFixtureType() {
		return fixtureType;
	}
	public void setStockValue(BigDecimal stockValue) {
		this.stockValue = stockValue;
	}
	public BigDecimal getStockValue() {
		return stockValue;
	}
	public void setLowTemp(float lowTemp) {
		this.lowTemp = lowTemp;
	}
	public float getLowTemp() {
		return lowTemp;
	}
	public void setHighTemp(float highTemp) {
		this.highTemp = highTemp;
	}
	public float getHighTemp() {
		return highTemp;
	}
	public void setLowTempOffset(float lowTempOffset) {
		this.lowTempOffset = lowTempOffset;
	}
	public float getLowTempOffset() {
		return lowTempOffset;
	}
	public void setHighTempOffset(float highTempOffset) {
		this.highTempOffset = highTempOffset;
	}
	public float getHighTempOffset() {
		return highTempOffset;
	}
	
	public void setInstallationDate(Date installationDate) {
		this.installationDate = installationDate;
	}
	public Date getInstallationDate() {
		return installationDate;
	}
	

	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}

	public boolean isChosen() {
		return chosen;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return currency;
	}

}
