package com.nextcontrols.bureaudomain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class HistoricAlarm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long alarmId;
	private String alarmType;
	private String recvTime;
	private String probIdentiTime;
	private Timestamp receiveTime;
	private String controllerName;
	String  problem_identified_detail;
	String  incident_report;
	private Timestamp problemIdentifiedTimestamp;		
	private String problemIdentifiedBy;
	private String partnersName;
	private String actionTaken;
	private String verisaeNumber;
	public HistoricAlarm(){}
	
	public HistoricAlarm(long alarmId,String alarmType,Timestamp receiveTime,String controllerName,
		String  problem_identified_detail,String  incident_report, Timestamp pProblemIdentifiedTimestamp,String pProblemIdentifiedBy)
	{		
		this.alarmId=alarmId;
		this.alarmType=alarmType;
		this.receiveTime=receiveTime;
		this.setRecvTime(receiveTime);
		this.controllerName=controllerName;
		this.problem_identified_detail=problem_identified_detail;
	    this.incident_report=incident_report;
		this.problemIdentifiedTimestamp = pProblemIdentifiedTimestamp;
		this.problemIdentifiedBy = pProblemIdentifiedBy;
		this.setProbIdentiTime(pProblemIdentifiedTimestamp.toString());
	}
	public Timestamp getReceivetime() {
		return this.receiveTime;
	}

	public void setRecvTime(Timestamp recvTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String rectime=dateFormat.format(this.receiveTime);
		this.recvTime=rectime;
	}

	public String getRecvTime() {
		return this.recvTime;
	}

	public String getProbIdentiTime() {
		return probIdentiTime;
	}

	public void setProbIdentiTime(String probIdentiTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		this.probIdentiTime = dateFormat.format(this.problemIdentifiedTimestamp);
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getProblem_identified_detail() {
		return problem_identified_detail;
	}

	public void setProblem_identified_detail(String problem_identified_detail) {
		this.problem_identified_detail = problem_identified_detail;
	}

	public String getIncident_report() {
		return incident_report;
	}

	public void setIncident_report(String incident_report) {
		this.incident_report = incident_report;
	}
	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}
	public long getAlarmId() {
		return alarmId;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public String getAlarmType() {
		return alarmType;
	}
	
	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}
	public String getControllerName() {
		return controllerName;
	}

	public Timestamp getProblemIdentifiedTimestamp() {
		return problemIdentifiedTimestamp;
	}

	public void setProblemIdentifiedTimestamp(Timestamp problemIdentifiedTimestamp) {
		this.problemIdentifiedTimestamp = problemIdentifiedTimestamp;
	}

	public String getProblemIdentifiedBy() {
		return problemIdentifiedBy;
	}

	public void setProblemIdentifiedBy(String problemIdentifiedBy) {
		this.problemIdentifiedBy = problemIdentifiedBy;
	}
	public void setPartnersName(String partnersName) {
		this.partnersName=partnersName;
	}
	public String getPartnersName() {
		return partnersName;
	}
	public void setActionTaken(String actionTaken) {
		this.actionTaken=actionTaken;
	}
	public String getActionTaken() {
		return actionTaken;
	}
	public String getVerisaeNumber() {
		return verisaeNumber;
	}
	public void setVerisaeNumber(String verisaeNumber) {
		this.verisaeNumber=verisaeNumber;
	}
	
	/*
	 
	 
	 private int alarmTrapId;
	
	private int user_id;

	private Date hold_end_time;
	
	private String siteCode;
	private String alarmString;
	private String controllerType;

	private int priority;
	private int alarm_asset_id;
	private Date  time_taken;
	private String subfixture;
	
   String  problem_identified_by;
	Date  problem_identified_timestamp;
	 
		public String getTimetaken() {
		return timetaken;
	}

	public void setTimetaken(String timetaken) {
		this.timetaken = timetaken;
	}

	
	public String getHoldendtime() {
		return holdendtime;
	}

	public void setHoldendtime(String holdendtime) {
		this.holdendtime = holdendtime;
	}

	String timetaken;
	String receivetime;
	String holdendtime;
	
	
	
	private boolean called=false;
	private boolean sms=false;
	private boolean email=false;
	
	 
	 	public HistoricAlarm(int alarmId,String alarmType,Date receiveTime,int alarmTrapId,int user_id,
        Date hold_end_time, String siteCode, String alarmString, String controllerType,
		String controllerName,int priority,	int alarm_asset_id,Date  time_taken,String subfixture,
		String  problem_identified_by,Date  problem_identified_timestamp,
	String  problem_identified_detail,String  incident_report)
	{		
		
		this.alarmId=alarmId;
		this.alarmType=alarmType;
		this.receiveTime=receiveTime;
		this.siteCode=siteCode;
		this.alarmTrapId=alarmTrapId;
         this.alarm_asset_id=alarmTrapId;     
         this.user_id=user_id;
         this.hold_end_time=hold_end_time;
         this.siteCode=siteCode;
         this.alarmString=alarmString;
         this.controllerType=controllerType;
		this.controllerName=controllerName;
		this.priority=priority;
		this.alarm_asset_id=alarm_asset_id;
		this.time_taken=time_taken;
		this.subfixture=subfixture;
		this.problem_identified_by=problem_identified_by;
		this.problem_identified_timestamp=problem_identified_timestamp;
	     this.problem_identified_detail=problem_identified_detail;
	     this.incident_report=incident_report;
		
	    
	 //    this.receivetime=dateFormat.format(this.receiveTime);
	//	this.holdendtime=dateFormat.format(this.hold_end_time);
	//	this.timetaken=dateFormat.format(this.time_taken);
	}
	
	public HistoricAlarm(int alarmId,String alarmType,Date receiveTime,Date finishTime,String state,int alarmTrapId,String fixture,String subfixture,String siteCode,
	String alarmText,byte fileType,String controllerType,Date alarmTime,String controllerName,String alarmString){
		this.alarmId=alarmId;
		this.alarmType=alarmType;
		this.receiveTime=receiveTime;
		//this.finishTime=finishTime;
		//this.state=state;
		this.alarmTrapId=alarmTrapId;
	//	this.fixture=fixture;
		this.subfixture=subfixture;
		this.siteCode=siteCode;
	//	this.setAlarmText(alarmText);
	//	this.fileType=fileType;
		this.controllerType=controllerType;
	//	this.alarmTime=alarmTime;
		this.controllerName=controllerName;
		this.alarmString=alarmString;
		
		
	}
	 (public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getAlarm_asset_id() {
		return alarm_asset_id;
	}

	public void setAlarm_asset_id(int alarm_asset_id) {
		this.alarm_asset_id = alarm_asset_id;
	}

	public Date getTime_taken() {
		return time_taken;
	}

	public void setTime_taken(Date time_taken) {
		this.time_taken = time_taken;
	}
	
	public Date getProblem_identified_timestamp() {
		return problem_identified_timestamp;
	}

	public void setProblem_identified_timestamp(Date problem_identified_timestamp) {
		this.problem_identified_timestamp = problem_identified_timestamp;
	}
public String getProblem_identified_by() {
		return problem_identified_by;
	}

	public void setProblem_identified_by(String problem_identified_by) {
		this.problem_identified_by = problem_identified_by;
	}
	
		public Date getHold_end_time() {
		return hold_end_time;
	}
	public void setHold_end_time(Date hold_end_time) {
		this.hold_end_time = hold_end_time;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public String getSiteCode() {
		return siteCode;
	}
	
	public void setFinishTime(Date finishTime) {
	this.finishTime = finishTime;
	if (this.finishTime!=null){
		setTimeFinished(finishTime);
	}
}

public Date getFinishTime() {
	return finishTime;
	public void setAlarmTrapId(int alarmTrapId) {
		this.alarmTrapId = alarmTrapId;
	}

	public int getAlarmTrapId() {
		return alarmTrapId;
	}

	

	public void setControllerType(String controllerType) {
		this.controllerType = controllerType;
	}

	public String getControllerType() {
		return controllerType;
	}

	

	public void setSubfixture(String subfixture) {
		this.subfixture = subfixture;
	}

	public String getSubfixture() {
		return subfixture;
	}

	public void setCalled(boolean called) {
		this.called = called;
	}

	public boolean isCalled() {
		return called;
	}

	public void setSms(boolean sms) {
		this.sms = sms;
	}

	public boolean isSms() {
		return sms;
	}

	public void setEmail(boolean email) {
		this.email = email;
	}

	public boolean isEmail() {
		return email;
	}

}
	
	public void setAlarmString(String alarmString) {
		this.alarmString = alarmString;
	}

	public String getAlarmString() {
		return alarmString;
	}
*/
	
	
	

	



	
}
