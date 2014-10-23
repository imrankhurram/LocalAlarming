package com.nextcontrols.bureaudao;

import java.util.List;

import com.nextcontrols.bureaudomain.AlarmAssetSearchString;

public interface IAlarmAssetSearchStringDAO {
	
	public List<AlarmAssetSearchString> getAssetSearchStrings(int alarmAssetId);
	public void addAssetSearchString(int alarmAssetId,String controllerSearch, String alarmSearch,String subfixture);
	public void delAssetSearchString(long searchStringId);
	public void modAssetSearchString(long searchStringId,int alarmAssetId,String controllerSearch, String alarmSearch,String subfixture);
}
