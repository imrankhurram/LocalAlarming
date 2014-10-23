package com.nextcontrols.bureaudao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.nextcontrols.bureaudomain.HistoricAlarm;
import com.nextcontrols.bureaudomain.LocalActiveAlarm;

public interface ILocalActiveAlarmDAO {
	public List<LocalActiveAlarm> getActiveAlarms(int user_id);
	public List<LocalActiveAlarm> getActiveAlarmsFromSite(String siteCode,int user_id);
	public List<LocalActiveAlarm> getAlarmstoCall(final String countryCode, final String siteCode);
	public List<LocalActiveAlarm> getAllActiveAlarms();
	public List<LocalActiveAlarm> getUnknownSiteAlarms(int userId);
	public List<LocalActiveAlarm> getDeptActiveAlarms(int depId,int userId);
	public int getDeptAlarmsOnHold(int depId);
	public List<HistoricAlarm> getHistoricAlarms(String siteCode,Date dateFrom,Date dateTo);
	public List<HistoricAlarm> getDepartmentHistoricAlarms(int alarm_trap_id,Date histDate);
	public Timestamp getReceiveTime(long alarmId);
	public void setAlarmUser(long alarm_id,int user_id);
	public void holdAlarm(long alarm_id,Timestamp holdUntil);
	public void delLocalActiveAlarm(long alarm_id);
	public void finishAlarm(long alarm_id,Date finishTime);
	public void clearAllAlarms(String username);
	public void releaseActiveAlarm(int userId);
	public int actionedAlmCount();
	public int averageResponseTime();
	public void setAlarmsSites(List<LocalActiveAlarm> alarms);
	public int allActiveAlarmsCount();
	public int getOverdueAlarmsCount();
}