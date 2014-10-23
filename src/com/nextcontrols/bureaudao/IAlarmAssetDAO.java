package com.nextcontrols.bureaudao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.nextcontrols.bureaudomain.AlarmAsset;

public interface IAlarmAssetDAO {

	public List<AlarmAsset> getDeptAlarmAssets(int departmentId);
	public List<AlarmAsset> getSiteAlarmAssets(String siteCode);
	public void addAlarmAsset(int departmentId, String fixtureName, String fixtureType, BigDecimal stockValue, float lowTemp,
			float highTemp,float lowTempOffset,float highTempOffset, Date installationDate,String currency);
	public void deleteAlarmAsset(int alarmAssetId);
	public void modifyAlarmAsset(int alarmAssetId,int departmentId, String fixtureName, String fixtureType, BigDecimal stockValue, float lowTemp,
			float highTemp,float lowTempOffset,float highTempOffset, Date installationDate,String currency);
	public int copyAlarmAsset(int departmentId, String fixtureName, String fixtureType, BigDecimal stockValue, float lowTemp,
			float highTemp,float lowTempOffset,float highTempOffset, Date installationDate,String currency);
	public AlarmAsset getAlarmAsset(Connection dbBureauConn,int alarmAssetId);
	public void moveAlarmAsset(int alarmAssetId, int depId);
	public String getDeptAlarmAssetIds(int departmentId);
	public List<String> getFixtureTypes(); 
}
