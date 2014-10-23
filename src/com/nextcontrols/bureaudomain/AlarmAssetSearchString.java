package com.nextcontrols.bureaudomain;

import java.io.Serializable;

public class AlarmAssetSearchString implements Serializable{
	
	private long searchStringId;
	private int alarmAssetId;
	private String controllerSearch;
	private String alarmSearch;
	private String subfixture;
	
	public AlarmAssetSearchString(){}
	
	public AlarmAssetSearchString(long searchStringId,int alarmAssetId,String controllerSearch, String alarmSearch,String subfixture){
		this.searchStringId=searchStringId;
		this.alarmAssetId=alarmAssetId;
		this.controllerSearch=controllerSearch;
		this.alarmSearch=alarmSearch;
		this.subfixture=subfixture;
	}
	
	public void setSearchStringId(long searchStringId) {
		this.searchStringId = searchStringId;
	}
	public long getSearchStringId() {
		return searchStringId;
	}
	
	public void setAlarmAssetId(int alarmAssetId) {
		this.alarmAssetId = alarmAssetId;
	}
	public int getAlarmAssetId() {
		return alarmAssetId;
	}
	
	public void setControllerSearch(String controllerSearch) {
		this.controllerSearch = controllerSearch;
	}
	public String getControllerSearch() {
		return controllerSearch;
	}
	
	public void setAlarmSearch(String alarmSearch) {
		this.alarmSearch = alarmSearch;
	}
	public String getAlarmSearch() {
		return alarmSearch;
	}

	public void setSubfixture(String subfixture) {
		this.subfixture = subfixture;
	}

	public String getSubfixture() {
		return subfixture;
	}

}
