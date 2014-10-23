package com.nextcontrols.bureaudomain;


import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LocalActiveAlarm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long alarmId;
	private String alarmType;
	private Timestamp receiveTime;
	private String recvTime;
	private String received;
	private SimpleDateFormat receivedFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private String siteCode;
	private String siteName;
	private String alarmString;
	private String controllerType;
	private String controllerName;
	private String fixtureType="";
	private String subfixture="";
	private String alarmStringShort;
	private String alarmInfoString;
	private boolean first;
	private String rowClass;
	DateFormat timezoneConversion=new SimpleDateFormat("dd-MM-yyyy HH:MM:ss");
	private String tempAlarmType=" ";
	private Timestamp ackTime;
	boolean ack;   // flag to decide whether to show ack commandlink or complete
	boolean complete;   // flag to decide whether to show ack commandlink or complete

	
	public LocalActiveAlarm(){}
		
	public LocalActiveAlarm(long alarmId,String alarmType,Timestamp receiveTime,String siteCode,String alarmString,
			String controllerType,String controllerName,String fixtureType,boolean ack,boolean complete,String siteName){
		this.alarmId = alarmId;
		this.alarmType = alarmType;
		this.receiveTime = receiveTime;
		this.siteCode = siteCode;
		this.siteName = siteName;
		this.alarmString = alarmString;
		this.controllerType = controllerType;
		this.controllerName=controllerName;
		this.fixtureType = fixtureType;
		this.setAlarmInfoString();
		// //////////// changes start ///////////////
		this.setRecvTime(receiveTime);
		// //////////// changes end ///////////////
		//this.setRecvTime(receiveTime);
		this.ack = ack;
		this.complete = complete;
		this.first=false;
		this.setAlarmStringShort(this.alarmString);
		this.setRowClass();
		this.setReceived(new java.util.Date(receiveTime.getTime()));
	}
	////////////////////////////     changes start                ////////////////////////////////
	
	public boolean isAck() {
		return ack;
	}

	public void setAck(boolean ack) {
		this.ack = ack;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	//////////////////////////////// changes end  ///////////////////////////////////
	///////////////////////////////    changes start //////////////////////////////
	public String getAckTime() {
		SimpleDateFormat ackFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		String acktime=ackFormat.format(ackTime);
		
		return acktime;
	
	}

	public void setAckTime(Timestamp ackTime) {
		this.ackTime = ackTime;
	}
	////////////////////////////////////   changes end   /////////////////////////////
	
	public long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(long alarmId) {
		this.alarmId = alarmId;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getAlarmString() {
		return alarmString;
	}

	public void setAlarmString(String alarmString) {
		this.alarmString = alarmString;
	}

	public String getControllerType() {
		return controllerType;
	}

	public void setControllerType(String contollerType) {
		this.controllerType = contollerType;
	}
	
	public void setControllerName(String controllerName){
		this.controllerName=controllerName;
	}
	
	public String getControllerName(){
		return controllerName;
	}
	
	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}

	public void setAlarmInfoString() {
		this.alarmInfoString = this.getAlarmString();
	}

	public String getAlarmInfoString() {
		if (alarmInfoString.length()>50){
			return alarmInfoString.substring(0, 49);//returning only the first 50 characters
		}else{
			return alarmInfoString; 
		}
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isFirst() {
		return first;
	}

	public void setAlarmStringShort(String alarmStringShort) {
		this.alarmStringShort = alarmStringShort;
	}

	public String getAlarmStringShort() {
		return alarmStringShort;
	}

	public void setSubfixture(String subfixture) {
		this.subfixture = subfixture;
	}

	public String getSubfixture() {
		return subfixture;
	}

/////////////////////////     changes start    ////////////////////////////
	public void setRowClass() {
/*		if (this.complete){
			this.rowClass="orange";
			
		}else{
			this.rowClass="red";


		}
*/		
	}

	public String getRowClass() {
		this.setRowClass();
		return rowClass;
	}
///////////////////////////     changes end    ////////////////////

	public void setReceived(Date received) {
		this.received = receivedFormat.format(received);
	}

	public String getReceived() {
		return received;
	}

	public void setFixtureType(String fixtureType) {
		this.fixtureType = fixtureType;
	}

	public String getFixtureType() {
		return fixtureType;
	}

	public void setRecvTime(Timestamp recvTime) {
		this.recvTime=receivedFormat.format(recvTime);
		
	}

	public String getRecvTime() {
		return recvTime;
	}

	public void setTempAlarmType() {
		try{
		if ((this.controllerType!=null) && (this.alarmString!=null)){
			String temperature = getTemperature(this.controllerType, this.alarmString);
			this.tempAlarmType = temperature+" ºC"; //this.alarmType + "( Temp: " + temperature + ")";
		}else{
			this.tempAlarmType = " ";
		}
		}catch(Exception e){this.tempAlarmType = " ";}
	}

	public String getTempAlarmType() {
		setTempAlarmType();
		return tempAlarmType;
	}
	
	private String getTemperature(String system, String alarmText){
        String temp = "";
        if(system.compareToIgnoreCase("NCS2000")==0)
        {        Pattern patternncs2000 = Pattern.compile("([\\+|-]{0,1})([0-9]+)((\\.[0-9]+)*)( +)DEG( {0,1})C");
                Matcher matcherncs2000 = patternncs2000.matcher(alarmText);
                //m = Regex.Match(alarmText, @"([\+|-]{0,1})([0-9]+)((\.[0-9]+)*)( +)DEG( {0,1})C", RegexOptions.RightToLeft);
                if (!matcherncs2000.find()){
                    //throw new ScriptException("No temperature match");
                }
                else{
                      temp = matcherncs2000.group();
                }
                temp= temp.trim();
                temp = temp.substring(0, temp.length()-5);
                temp = temp.trim();
        }
        else if(system.compareToIgnoreCase("NCS6500")==0)
        {
                Pattern patternncs6500 = Pattern.compile("= ([\\+|-]{0,1})([0-9]+)");
                Matcher matcherncs6500 = patternncs6500.matcher(alarmText);
                if (!matcherncs6500.find()){
                    //throw new ScriptException("No temperature match");
                }
                else{
                        temp = matcherncs6500.group();
                }
                temp= temp.trim();
                temp = temp.substring(2, temp.length());
                temp = temp.trim();
        }
        else if(system.compareToIgnoreCase("NCS3001")==0)
        {
            Pattern patternncs3001 = Pattern.compile("([\\+|-]{0,1})([0-9]+)((\\.[0-9]+)*)");
                Matcher matcherncs3001 = patternncs3001.matcher(alarmText);
                if (!matcherncs3001.find()){
                    ///throw new ScriptException("No temperature match");
                }
                else{
                	temp = matcherncs3001.group();
                }
              temp = temp.trim();
        }
        else if(system.compareToIgnoreCase("JTL")==0)
        {
                Pattern patternjtl = Pattern.compile("([\\+|-]{0,1})([0-9]{3})(\\.[0-9]) C");
                Matcher matcherjtl = patternjtl.matcher(alarmText);
                                  if (!matcherjtl.find()){
                    //throw new ScriptException("No temperature match");
                }
                else{
                	temp = matcherjtl.group();

                }
                temp= temp.trim();
                temp = temp.substring(0,temp.length()-2);
                temp = temp.trim();
        }
        else{
            //throw new ScriptException("Invalid Controller Type");
        }
        return temp;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (alarmId ^ (alarmId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalActiveAlarm other = (LocalActiveAlarm) obj;
		if (alarmId != other.alarmId)
			return false;
		return true;
	} 



}
