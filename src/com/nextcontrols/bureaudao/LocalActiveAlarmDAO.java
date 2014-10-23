package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.nextcontrols.bureaudomain.AlarmAsset;
import com.nextcontrols.bureaudomain.HistoricAlarm;
import com.nextcontrols.bureaudomain.LocalActiveAlarm;


public class LocalActiveAlarmDAO implements ILocalActiveAlarmDAO, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ILocalActiveAlarmDAO instance;
	private Connection dbBureauConn=null;
	private AlarmAssetDAO assetDB=new AlarmAssetDAO();
	//private AlarmTrapDAO almTrapDB=new AlarmTrapDAO();
	private AlarmAssetSearchStringDAO almAssetSearch=new AlarmAssetSearchStringDAO();
	/*@ManagedProperty(value="#{serviceProperties}")
	   private ServiceProperties service;*/

	/** *************** Factory Methods ************************* */
	
	public static ILocalActiveAlarmDAO getInstance() { 
        if (instance != null) {
            return instance; 
        } else {
            return new LocalActiveAlarmDAO();
        }
    }
    
	public static void setInstance(ILocalActiveAlarmDAO inst) {
		instance = inst;
	}
	
	@Override
	public List<LocalActiveAlarm> getActiveAlarms(int user_id) {
		List<LocalActiveAlarm> activeAlarms  = new ArrayList <LocalActiveAlarm> ();
		Calendar cal=Calendar.getInstance();
		Date now=cal.getTime();
		Connection dbConn;
		try{
			ConnectionBean.getInstance().closeMYSQLConnection();
			dbConn=ConnectionBean.getInstance().getAlarmMYSQLConnection();
			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
			//String query="SELECT * FROM `bureauv2alarms`.`alarming_activealarm` WHERE (user_id is null or user_id=" + user_id + ") and (hold_end_time is null or hold_end_time<=NOW()) or (time_taken<='" + new java.sql.Timestamp(now.getTime()) + 
			//"') ORDER BY priority,receive_time LIMIT 0,200;";
			
			String query="SELECT laa.alarm_id,laa.alarm_type,laa.receive_time,laa.site_code,laa.alarm_string,laa.controller_type,laa.controller_name,laa.site_name,laa.alarmasset_name,aasset.fixture_type FROM `bureauv2alarms`.`alarming_localactivealarm`  as laa"+
			    	  "Inner Join `bureauv2alarms`.`alarming_alarm` as alarm on laa.alarm_id = alarm.alarm_id"+
			    	  "Inner Join `bureauv2alarms`.`alarming_alarmassets` as aasset on alarm.alarmasset_id = aasset.alarmasset_id"+
			    	  "WHERE (user_id is null or user_id=" + user_id + ") and (hold_end_time is null or hold_end_time<=NOW()) or (time_taken<='" + new java.sql.Timestamp(now.getTime()) + 
						"') ORDER BY priority,receive_time LIMIT 0,200;";	
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				LocalActiveAlarm activealarm=new LocalActiveAlarm(result.getLong("alarm_id"),result.getString("alarm_type"),result.getTimestamp("receive_time"),result.getString("site_code"),result.getString("alarm_string")
						,result.getString("controller_type"),result.getString("controller_name"),result.getString("fixtureType"),false,false,result.getString("site_name"));
				
				AlarmAsset asset=assetDB.getAlarmAsset(dbBureauConn,result.getInt("alarmasset_id"));
				activealarm.setFixtureType(asset.getFixtureType());
				asset=null;
				activealarm.setSubfixture(almAssetSearch.getSubfixture(dbBureauConn, result.getInt("alarmasset_id"), activealarm.getControllerName(), activealarm.getAlarmString()));
			    activeAlarms.add(activealarm);
			}
			result.getStatement().close();
		}catch (SQLException e){
			System.out.println("SQL Exception in function getActiveAlarms(int user_id) in class ActiveAlarmDAO");
			//e.printStackTrace();
		}
		
		return activeAlarms;
	}
	
	public List<LocalActiveAlarm> getActiveAlarms() {
		List<LocalActiveAlarm> activeAlarms  = new ArrayList <LocalActiveAlarm> ();
		Calendar cal=Calendar.getInstance();
		Date now=cal.getTime();
		Connection dbConn;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
		//	String query="SELECT * FROM `bureauv2alarms`.`alarming_activealarm` WHERE user_id is null and (hold_end_time is null or hold_end_time<=NOW()) or (time_taken<='" + new java.sql.Timestamp(now.getTime()) + 
		//	"') ORDER BY priority,receive_time LIMIT 0,200;";
			
String query="SELECT laa.alarm_id,laa.alarm_type,laa.receive_time,laa.site_code,laa.alarm_string,laa.controller_type,laa.controller_name,laa.site_name,laa.alarmasset_name,aasset.fixture_type FROM `bureauv2alarms`.`alarming_localactivealarm`  as laa Inner Join `bureauv2alarms`.`alarming_alarm` as alarm on laa.alarm_id = alarm.alarm_id Inner Join `bureauv2alarms`.`alarming_alarmassets` as aasset on alarm.alarmasset_id = aasset.alarmasset_id WHERE user_id is null and (hold_end_time is null or hold_end_time<=NOW()) or (time_taken<='" + new java.sql.Timestamp(now.getTime()) + 
			"') ORDER BY priority,receive_time LIMIT 0,200;";			   
			
			
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				LocalActiveAlarm activealarm=new LocalActiveAlarm(result.getLong("alarm_id"),result.getString("alarm_type"),result.getTimestamp("receive_time"),result.getString("site_code"),result.getString("alarm_string")
						,result.getString("controller_type"),result.getString("controller_name"),result.getString("fixture_type"),false,false,result.getString("site_name"));
				AlarmAsset asset=assetDB.getAlarmAsset(dbBureauConn,result.getInt("alarmasset_id"));
				activealarm.setFixtureType(asset.getFixtureType());
				asset=null;
				
			 	activealarm.setSubfixture(almAssetSearch.getSubfixture(dbBureauConn, result.getInt("alarmasset_id"), activealarm.getControllerName(), activealarm.getAlarmString()));
				activeAlarms.add(activealarm);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getActiveAlarms in class ActiveAlarmDAO");
		}
		return activeAlarms;
	}
	
	@Override
	public List<LocalActiveAlarm> getActiveAlarmsFromSite(String siteCode,int user_id){
		
		List<LocalActiveAlarm> siteActiveAlarms  = new ArrayList <LocalActiveAlarm> ();
		Calendar cal=Calendar.getInstance();
		Date now=cal.getTime();
		Connection dbConn;
		try{
			dbConn=ConnectionBean.getInstance().getAlarmMYSQLConnection();
			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
			//String query="SELECT * FROM `bureauv2alarms`.`alarming_activealarm` WHERE site_code='" + siteCode + "' and (user_id is null or user_id=" + user_id +
		//	") and (hold_end_time is null or hold_end_time<=NOW()) or (time_taken<='" + new java.sql.Timestamp(now.getTime()) + 
		//	"') ORDER BY priority,receive_time;";
			
String query="SELECT laa.alarm_id,laa.alarm_type,laa.receive_time,laa.site_code,laa.alarm_string,laa.controller_type,laa.controller_name,laa.site_name,laa.alarmasset_name,aasset.fixture_type FROM `bureauv2alarms`.`alarming_localactivealarm`  as laa Inner Join `bureauv2alarms`.`alarming_alarm` as alarm on laa.alarm_id = alarm.alarm_id Inner Join `bureauv2alarms`.`alarming_alarmassets` as aasset on alarm.alarmasset_id = aasset.alarmasset_id WHERE site_code='" + siteCode + "' and (user_id is null or user_id=" + user_id + ") and (hold_end_time is null or hold_end_time<=NOW()) or (time_taken<='" + new java.sql.Timestamp(now.getTime()) + 
			"') ORDER BY priority,receive_time;";	   
			
			
			Statement stmnt=dbConn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
		              java.sql.ResultSet.CONCUR_READ_ONLY);
			stmnt.setFetchSize(Integer.MIN_VALUE);
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				LocalActiveAlarm activealarm=new LocalActiveAlarm(result.getLong("alarm_id"),result.getString("alarm_type"),result.getTimestamp("receive_time"),result.getString("site_code"),result.getString("alarm_string")
						,result.getString("controller_type"),result.getString("controller_name"),result.getString("fixture_type"),false,false,result.getString("site_name"));
				AlarmAsset asset=assetDB.getAlarmAsset(dbBureauConn,result.getInt("alarmasset_id"));
				activealarm.setFixtureType(asset.getFixtureType());
				asset=null;
			   	activealarm.setSubfixture(almAssetSearch.getSubfixture(dbBureauConn, result.getInt("alarmasset_id"), activealarm.getControllerName(), activealarm.getAlarmString()));
			    siteActiveAlarms.add(activealarm);
			}
			result.getStatement().close();
		}catch (SQLException e){
			System.out.println("SQL Exception in function getActiveAlarmsFromSite in class ActiveAlarmDAO");
		}
		return siteActiveAlarms;
	}
	
	public String getAlarmType(LocalActiveAlarm pActiveAlarm) {
		String alarmType = null;
	    Statement statement;
	    ResultSet result = null;
	    Connection connection;
		try {
			connection = ConnectionBean.getInstance().getMYSQLConnection();
			String queryAlarmType ="SELECT Alarm_Type FROM alarming.AlarmTypeSearchStrings WHERE [Controller_Type]='"+pActiveAlarm.getControllerType()+"' AND '"+ pActiveAlarm.getAlarmString().split("\\|")[7].trim()+"' LIKE Alarm_Search";
	        statement = connection.createStatement();
	        result = statement.executeQuery(queryAlarmType);
	          while(result.next()){
	               alarmType = result.getString("Alarm_Type");
	           }
	       
		} catch (SQLException e1) {
			System.out.println("SQL Exception in function getAlarmType in class ActiveAlarmDAO");
		}
	    return alarmType;
	}

	
	public HistoricAlarm getLastAlarmFromSite(String site_code){
		HistoricAlarm lastAlarm = null;
		Connection connection;
		try {
			connection=ConnectionBean.getInstance().getMYSQLConnection();
			String query="SELECT `alarming_alarm`.alarm_id,`alarming_alarm`.alarm_type,`alarming_alarm`.receive_time,`alarming_alarm`.finish_time,`alarming_alarm`.state,`alarming_alarm`.alarm_trap_id,`alarming_alarm`.alarmasset_id,alarming_alarm`.subfixture," +
			"`alarming_originalalarm`.site_code,`alarming_originalalarm`.alarm_string,`alarming_originalalarm`.file_type,`alarming_originalalarm`.controller_type," +
			"`alarming_originalalarm`.alarm_time,`alarming_originalalarm`.alarm_string,`alarming_originalalarm`.controller_name " +
			"FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`alarming_originalalarm`  ON `bureauv2alarms`.`alarming_alarm`.alarm_id = `bureauv2alarms`.`alarming_originalalarm`.alarm_id " +
			"WHERE `alarming_originalalarm`.site_code='" + site_code + "' ORDER BY receive_time DESC LIMIT 0,1;";
			Statement statement = connection.createStatement();
		    ResultSet result = statement.executeQuery(query);
		        while (result.next()){
		        	/*lastAlarm=new HistoricAlarm(result.getInt("alarm_id"),result.getString("alarm_type"),result.getDate("receive_time"),result.getDate("finish_time"),
							result.getString("state"),result.getInt("alarm_trap_id"),null,result.getString("subfixture"),result.getString("site_code"),
							result.getString("alarm_string"),result.getByte("file_type"),result.getString("controller_type"),
							result.getDate("alarm_time"),result.getString("controller_name"),result.getString("alarm_string"));
					
					
					lastAlarm.setCalled(auditDB.alarmCalled(lastAlarm.getAlarmId()));
					lastAlarm.setEmail(auditDB.alarmEmail(lastAlarm.getAlarmId()));
					lastAlarm.setSms(auditDB.alarmSMS(lastAlarm.getAlarmId()));
					
		    */    }
			}catch (SQLException e) {
				System.out.println("SQL Exception in function getLastAlarmFromSite in class ActiveAlarmDAO");
		    
			}
		return lastAlarm;
	}


	@Override
	public List<LocalActiveAlarm> getAlarmstoCall(String countryCode, String siteCode) {
		List<LocalActiveAlarm> activeAlarms  = new ArrayList <LocalActiveAlarm> ();
		Calendar cal=Calendar.getInstance();
		Date now=cal.getTime();
		Connection dbConn;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
			//String query="SELECT * FROM `bureauv2alarms`.`alarming_activealarm` (hold_end_time is null or hold_end_time<=NOW()) or (time_taken<='" + new java.sql.Timestamp(now.getTime()) + 
			//"') ORDER BY priority,receive_time;";
			
String query="SELECT laa.alarm_id,laa.alarm_type,laa.receive_time,laa.site_code,laa.alarm_string,laa.controller_type,laa.controller_name,laa.site_name,laa.alarmasset_name,aasset.fixture_type FROM `bureauv2alarms`.`alarming_localactivealarm`  as laa Inner Join `bureauv2alarms`.`alarming_alarm` as alarm on laa.alarm_id = alarm.alarm_id Inner Join `bureauv2alarms`.`alarming_alarmassets` as aasset on alarm.alarmasset_id = aasset.alarmasset_id (hold_end_time is null or hold_end_time<=NOW()) or (time_taken<='" + new java.sql.Timestamp(now.getTime()) +"') ORDER BY priority,receive_time;";
			
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				LocalActiveAlarm activealarm=new LocalActiveAlarm(result.getLong("alarm_id"),result.getString("alarm_type"),result.getTimestamp("receive_time"),result.getString("site_code"),result.getString("alarm_string")
						,result.getString("controller_type"),result.getString("controller_name"),result.getString("fixture_type"),false,false,result.getString("site_name"));
				AlarmAsset asset=assetDB.getAlarmAsset(dbBureauConn,result.getInt("alarmasset_id"));
				activealarm.setFixtureType(asset.getFixtureType());
				asset=null;
				activealarm.setSubfixture(almAssetSearch.getSubfixture(dbBureauConn, result.getInt("alarmasset_id"), activealarm.getControllerName(), activealarm.getAlarmString()));
			    activeAlarms.add(activealarm);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getAlarmsToCall in class ActiveAlarmDAO");
		}
		return activeAlarms;
	}
	
	public int receivedAlmsPerHour(){
		int almsCount=0;
		Calendar hourBack=Calendar.getInstance();
		hourBack.set(Calendar.HOUR_OF_DAY, hourBack.get(Calendar.HOUR_OF_DAY)-1);
		Date prior=hourBack.getTime();
		Connection dbConn;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			String query="SELECT alarm_id FROM `bureauv2alarms`.`alarming_originalalarm` WHERE receive_time>'" + new Timestamp(prior.getTime()) + "';";
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				almsCount++;
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function receivedAlmsPerHour in class ActiveAlarmDAO");
		}
		return almsCount;
	}
	
	public int receivedAlmsLastYear(){
		int almsCount=0;
		Calendar yearBack=Calendar.getInstance();
		yearBack.set(Calendar.YEAR, yearBack.get(Calendar.YEAR)-1);
		Date prior=yearBack.getTime();
		Connection dbConn;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			String query="SELECT COUNT(alarm_id) FROM `bureauv2alarms`.`alarming_originalalarm` WHERE receive_time>'" + new Timestamp(prior.getTime()) + "';";
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				almsCount=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function receivedAlmsLastYear in class ActiveAlarmDAO");
		}
		return almsCount;
	}

	@Override
	public void setAlarmUser(long alarm_id, int user_id) {
		Connection dbConn;
		Calendar time=Calendar.getInstance();
		time.add(Calendar.MINUTE, 10);
		Date dateTime=time.getTime();
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			String query="UPDATE `bureauv2alarms`.`alarming_activealarm` SET user_id=" + user_id + "," +
					"time_taken = '" + new java.sql.Timestamp(dateTime.getTime()) +  "' WHERE alarm_id=" + alarm_id + ";";
			Statement stmnt=dbConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e){
			System.out.println("SQL Exception in function setAlarmUser in class ActiveAlarmDAO");
		}
	}

	@Override
	public List<HistoricAlarm> getHistoricAlarms(String siteCode, Date dateFrom,
			Date dateTo) {
		List<HistoricAlarm> historicAlarms = new ArrayList<HistoricAlarm> ();
		String query="";
		if ((dateFrom==null) && (dateTo!=null)){
			
			query="SELECT `alarming_alarm`.alarm_id,`alarming_alarm`.alarm_type," +
			"`alarming_alarm`.receive_time,`alarming_alarm`.alarm_trap_id," +
			"`alarming_alarm`.user_id,`alarming_alarm`.hold_end_time," +
			"`alarming_alarm`.site_code,`alarming_alarm`.alarm_string," +
			"`alarming_alarm`.controller_type,`alarming_alarm`.controller_name," +
			"`alarming_alarm`.priority,`alarming_alarm`.alarmasset_id," +
			"`alarming_alarm`.time_taken, `alarming_alarm`.subfixture," +
			"`audits_incident_audit`.problem_identified_by," +
			"`audits_incident_audit`.problem_identified_timestamp,"+
			"`audits_incident_audit`.incident_report,`audits_incident_audit`.problem_identified_detail " +
			"FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_incident_audit` " +
			" ON `bureauv2alarms`.`alarming_alarm`.alarm_id = `bureauv2alarms`.`audits_incident_audit`.alarm_id " +
			"WHERE `alarming_alarm`.site_code='" + siteCode + 
			"' and `alarming_alarm`.receive_time<='" + new java.sql.Date(dateTo.getTime()) + 
			"' ORDER BY `alarming_alarm`.receive_time DESC;";
			
			
			
			
			
			
		
		/*	query="SELECT `alarming_alarm`.alarm_id,`alarming_alarm`.alarm_type,`alarming_alarm`.receive_time,`alarming_alarm`.finish_time,`alarming_alarm`.handle_type,`alarming_alarm`.alarm_trap_id,`alarming_alarm`.alarmasset_id,`alarming_alarm`.subfixture," +
			"`alarming_alarm`.site_code,`alarming_originalalarm`.alarm_string,`alarming_originalalarm`.file_type,`alarming_originalalarm`.controller_type," +
			"`alarming_originalalarm`.alarm_time,`alarming_originalalarm`.alarm_string,`alarming_originalalarm`.controller_name " +
			"FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`alarming_originalalarm`  ON `bureauv2alarms`.`alarming_alarm`.alarm_id = `bureauv2alarms`.`alarming_originalalarm`.alarm_id " +
			"WHERE `alarming_originalalarm`.site_code='" + siteCode + "' and `alarming_alarm`.receive_time<='" + new java.sql.Date(dateTo.getTime()) + "' ORDER BY `alarming_alarm`.receive_time DESC;";*/
		
		
		}else if ((dateFrom!=null) && (dateTo==null)){
		
			
			query="SELECT `alarming_alarm`.alarm_id,`alarming_alarm`.alarm_type," +
			"`alarming_alarm`.receive_time,`alarming_alarm`.alarm_trap_id," +
			"`alarming_alarm`.user_id,`alarming_alarm`.hold_end_time," +
			"`alarming_alarm`.site_code,`alarming_alarm`.alarm_string," +
			"`alarming_alarm`.controller_type,`alarming_alarm`.controller_name," +
			"`alarming_alarm`.priority,`alarming_alarm`.alarmasset_id," +
			"`alarming_alarm`.time_taken, `alarming_alarm`.subfixture," +
			"`audits_incident_audit`.problem_identified_by," +
			"`audits_incident_audit`.problem_identified_timestamp,"+
			"`audits_incident_audit`.incident_report,`audits_incident_audit`.problem_identified_detail " +
			"FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_incident_audit` " +
			" ON `bureauv2alarms`.`alarming_alarm`.alarm_id = `bureauv2alarms`.`audits_incident_audit`.alarm_id " +
			"WHERE `alarming_alarm`.site_code='" + siteCode + "' " +
	    	"and `alarming_alarm`.receive_time>='" + new java.sql.Date(dateFrom.getTime()) + 
			"' ORDER BY `alarming_alarm`.receive_time DESC;";
			
			
			
			
			
			
			/*query="SELECT `alarming_alarm`.alarm_id,`alarming_alarm`.alarm_type,`alarming_alarm`.receive_time,`alarming_alarm`.finish_time,`alarming_alarm`.handle_type,`alarming_alarm`.alarm_trap_id,`alarming_alarm`.alarmasset_id,`alarming_alarm`.subfixture," +
			"`alarming_alarm`.site_code,`alarming_originalalarm`.alarm_string,`alarming_originalalarm`.file_type,`alarming_originalalarm`.controller_type," +
			"`alarming_originalalarm`.alarm_time,`alarming_originalalarm`.alarm_string,`alarming_originalalarm`.controller_name " +
			"FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`alarming_originalalarm`  ON `bureauv2alarms`.`alarming_alarm`.alarm_id = `bureauv2alarms`.`alarming_originalalarm`.alarm_id " +
			"WHERE `alarming_originalalarm`.site_code='" + siteCode + "' and `alarming_alarm`.receive_time>='" + new java.sql.Date(dateFrom.getTime()) + "' ORDER BY `alarming_alarm`.receive_time DESC;";*/
		}else if ((dateFrom!=null) && (dateTo!=null)){
		
			
		//	System.out.print(" Query: ----------------------------------");
		    
			query="SELECT `alarming_alarm`.alarm_id,`alarming_alarm`.alarm_type," +
			"`alarming_alarm`.receive_time,`alarming_alarm`.alarm_trap_id," +
			"`alarming_alarm`.user_id,`alarming_alarm`.hold_end_time," +
			"`alarming_alarm`.site_code,`alarming_alarm`.alarm_string," +
			"`alarming_alarm`.controller_type,`alarming_alarm`.controller_name," +
			"`alarming_alarm`.priority,`alarming_alarm`.alarmasset_id," +
			"`alarming_alarm`.time_taken, `alarming_alarm`.subfixture," +
			"`audits_incident_audit`.problem_identified_by," +
			"`audits_incident_audit`.problem_identified_timestamp,"+
			"`audits_incident_audit`.incident_report,`audits_incident_audit`.problem_identified_detail " +
			"FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_incident_audit` " +
			" ON `bureauv2alarms`.`alarming_alarm`.alarm_id = `bureauv2alarms`.`audits_incident_audit`.alarm_id " +
			"WHERE `alarming_alarm`.site_code='" + siteCode + "' " +
	    	"and `alarming_alarm`.receive_time>='" + new java.sql.Date(dateFrom.getTime()) + 
			"' and `alarming_alarm`.receive_time<='" + new java.sql.Date(dateTo.getTime()) + 
			"' ORDER BY `alarming_alarm`.receive_time DESC;";
			
				
			
			/*query="SELECT `alarming_alarm`.alarm_id,`alarming_alarm`.alarm_type,`alarming_alarm`.receive_time,`alarming_alarm`.finish_time,`alarming_alarm`.handle_type,`alarming_alarm`.alarm_trap_id,`alarming_alarm`.alarmasset_id,`alarming_alarm`.subfixture," +
			"`alarming_alarm`.site_code,`alarming_originalalarm`.alarm_string,`alarming_originalalarm`.file_type,`alarming_originalalarm`.controller_type," +
			"`alarming_originalalarm`.alarm_time,`alarming_originalalarm`.alarm_string,`alarming_originalalarm`.controller_name " +
			"FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`alarming_originalalarm`  ON `bureauv2alarms`.`alarming_alarm`.alarm_id = `bureauv2alarms`.`alarming_originalalarm`.alarm_id " +
			"WHERE `alarming_originalalarm`.site_code='" + siteCode + "' and `alarming_alarm`.receive_time>='" + new java.sql.Date(dateFrom.getTime()) + 
			"' and `alarming_alarm`.receive_time<='" + new java.sql.Date(dateTo.getTime()) + "' ORDER BY `alarming_alarm`.receive_time DESC;";*/
		}
		
		else{
			
			query="SELECT `alarming_alarm`.alarm_id,`alarming_alarm`.alarm_type," +
			"`alarming_alarm`.receive_time,`alarming_alarm`.alarm_trap_id," +
			"`alarming_alarm`.user_id,`alarming_alarm`.hold_end_time," +
			"`alarming_alarm`.site_code,`alarming_alarm`.alarm_string," +
			"`alarming_alarm`.controller_type,`alarming_alarm`.controller_name," +
			"`alarming_alarm`.priority,`alarming_alarm`.alarmasset_id," +
			"`alarming_alarm`.time_taken, `alarming_alarm`.subfixture," +
			"`audits_incident_audit`.problem_identified_by," +
			"`audits_incident_audit`.problem_identified_timestamp,"+
			"`audits_incident_audit`.incident_report,`audits_incident_audit`.problem_identified_detail " +
			"FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_incident_audit` " +
			" ON `bureauv2alarms`.`alarming_alarm`.alarm_id = `bureauv2alarms`.`audits_incident_audit`.alarm_id " +
			"WHERE `alarming_alarm`.site_code='" + siteCode + 
			"' ORDER BY `alarming_alarm`.receive_time DESC;";
			
				
			
		
			
		/*	query="SELECT `alarming_alarm`.alarm_id,`alarming_alarm`.alarm_type,`alarming_alarm`.receive_time,`alarming_alarm`.finish_time,`alarming_alarm`.handle_type,`alarming_alarm`.alarm_trap_id,`alarming_alarm`.alarmasset_id,`alarming_alarm`.subfixture," +
					"`alarming_alarm`.site_code,`alarming_originalalarm`.alarm_string,`alarming_originalalarm`.file_type,`alarming_originalalarm`.controller_type," +
					"`alarming_originalalarm`.alarm_time,`alarming_originalalarm`.alarm_string,`alarming_originalalarm`.controller_name " +
					"FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`alarming_originalalarm`  ON `bureauv2alarms`.`alarming_alarm`.alarm_id = `bureauv2alarms`.`alarming_originalalarm`.alarm_id " +
					"WHERE `alarming_originalalarm`.site_code='" + siteCode + "' ORDER BY `alarming_alarm`.receive_time DESC;";*/
		}
		
		
		
		Connection dbConn;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			//dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
	//			System.out.print(" in result set----------------------------------");
	       /*HistoricAlarm temp=new HistoricAlarm(result.getInt("alarm_id"),result.getString("alarm_type"),
						result.getDate("receive_time"),result.getInt("alarm_trap_id"),
						result.getInt("user_id"),result.getDate("hold_end_time"),
						result.getString("site_code"),result.getString("alarm_string"),
						result.getString("controller_type"),result.getString("controller_name"),
						result.getInt("priority"),result.getInt("alarmasset_id"),
						result.getDate("time_taken"),result.getString("subfixture"),
						result.getString("problem_identified_by"),result.getDate("problem_identified_timestamp"),
						result.getString("problem_identified_detail"),result.getString("incident_report"));				
				
				//  AlarmAsset asset=assetDB.getAlarmAsset(dbBureauConn,result.getInt("alarmasset_id"));
				//  temp.setFixture(asset.getFixtureName());
			   //	asset=null;
				temp.setCalled(auditDB.alarmCalled(temp.getAlarmId()));
				temp.setEmail(auditDB.alarmEmail(temp.getAlarmId()));
				temp.setSms(auditDB.alarmSMS(temp.getAlarmId()));*/
				
				//historicAlarms.add(temp);
			}
		}catch (SQLException e){
	e.printStackTrace();
		
		}
		return historicAlarms ;
	}

	@Override
	public List<HistoricAlarm> getDepartmentHistoricAlarms(int alarm_trap_id,
			Date histDate) {
		List<HistoricAlarm> historicAlarms = new ArrayList<HistoricAlarm> ();
		Calendar daysAfter=Calendar.getInstance();
		daysAfter.setTime(histDate);
		daysAfter.add(Calendar.DAY_OF_YEAR,7);
		Date after=daysAfter.getTime();
		String query="SELECT `alarming_alarm`.alarm_id,`alarming_alarm`.alarm_type,`alarming_alarm`.receive_time,`alarming_alarm`.finish_time,`alarming_alarm`.alarm_trap_id,`alarming_alarm`.alarmasset_id,`alarming_alarm`.subfixture," +
					"`alarming_alarm`.site_code,`alarming_originalalarm`.alarm_string,`alarming_originalalarm`.file_type,`alarming_originalalarm`.controller_type," +
					"`alarming_originalalarm`.alarm_time,`alarming_originalalarm`.alarm_string,`alarming_originalalarm`.controller_name " +
					"FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`alarming_originalalarm`  ON `bureauv2alarms`.`alarming_alarm`.alarm_id = `bureauv2alarms`.`alarming_originalalarm`.alarm_id " +
					"WHERE alarm_trap_id=" + alarm_trap_id + " and `alarming_originalalarm`.receive_time>='" + new java.sql.Date(histDate.getTime()) 
		+ "' and `alarming_originalalarm`.receive_time<='" + new java.sql.Date(after.getTime()) + "';";
		Connection dbConn;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
/*				HistoricAlarm temp=new HistoricAlarm(result.getInt("alarm_id"),result.getString("alarm_type"),result.getTimestamp("receive_time"),result.getTimestamp("finish_time"),
						"",result.getInt("alarm_trap_id"),null,result.getString("subfixture"),result.getString("site_code"),
						result.getString("alarm_string"),result.getByte("file_type"),result.getString("controller_type"),
						result.getDate("alarm_time"),result.getString("controller_name"),result.getString("alarm_string"));
				temp.setCalled(auditDB.alarmCalled(temp.getAlarmId()));
				temp.setEmail(auditDB.alarmEmail(temp.getAlarmId()));
				temp.setSms(auditDB.alarmSMS(temp.getAlarmId()));
				
				historicAlarms.add(temp);*/
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getDepartmentHistoricAlarms in class ActiveAlarmDAO");
		}
		return historicAlarms ;
	}

	@Override
	public void holdAlarm(long alarm_id, Timestamp holdUntil) {
		Connection dbConn;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			String query="UPDATE `bureauv2alarms`.`alarming_activealarm` SET hold_end_time='" + holdUntil + "' WHERE alarm_id=" + alarm_id + ";";
			Statement stmnt=dbConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e){
			System.out.println("SQL Exception in function holdAlarm in class ActiveAlarmDAO");
		}
	}

	@Override
	public void delLocalActiveAlarm(long alarm_id) {
		Connection dbConn;
		Statement stmnt = null;
		PreparedStatement stmnt_insert =null;
		java.util.Date d= new java.util.Date();
		
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
	
			ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
			HttpSession session = (HttpSession)ectx.getSession(false);
			String query_insert="INSERT INTO `bureauv2alarms`.`audits_localalarmaudit` (`alarm_id`,`audit_time`,`action_taken`,`comment`,`username`" +
					") VALUES (?,?,?,?,?);";
			stmnt_insert=dbConn.prepareStatement(query_insert);
			stmnt_insert.setLong(1, alarm_id);
			stmnt_insert.setTimestamp(2, new java.sql.Timestamp(d.getTime()));
			stmnt_insert.setString(3, "Alarm Printed");
			stmnt_insert.setString(4, "Refrigration Alarm record form has been printed");
			stmnt_insert.setString(5, (String)session.getAttribute("name"));
			stmnt_insert.executeUpdate();
			
			String query="DELETE FROM `bureauv2alarms`.`alarming_localactivealarm` WHERE alarm_id=" + alarm_id + ";";
			stmnt=dbConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e){
			e.printStackTrace();
			System.out.println("SQL Exception in function delLocalActiveAlarm in class LocalActiveAlarmDAO");
		}finally{
		  try {
			stmnt_insert.close();
			stmnt.close();
		  } catch (SQLException e) {
			e.printStackTrace();
		  }
		stmnt_insert=null;
		stmnt=null;
		dbConn=null;
	  }
	}

	@Override
	public void releaseActiveAlarm(int userId) {
		Connection dbConn;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			String query="UPDATE `bureauv2alarms`.`alarming_activealarm` SET user_id=null,time_taken=NULL WHERE user_id=" + userId + ";";
			Statement stmnt=dbConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e){
			System.out.println("SQL Exception in function releaseActiveAlarm in class ActiveAlarmDAO");
		}
		
	}

	@Override
	public int actionedAlmCount() {
		int count=0;
		Calendar before=Calendar.getInstance();
		before.add(Calendar.HOUR_OF_DAY, -1);
		Date hourAgo=before.getTime();
		Connection dbConn;
		String query="SELECT COUNT(alarm_id) FROM `bureauv2alarms`.`alarming_alarm` WHERE (finish_time is not null) and (receive_time>='" + new java.sql.Timestamp(hourAgo.getTime()) + "');";
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				count=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function actionedAlmCount in class ActiveAlarmDAO");
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int averageResponseTime() {
		int time = 0;
		Calendar before=Calendar.getInstance();
		before.add(Calendar.YEAR, -1);
		Date yearAgo=before.getTime();
		Connection dbConn;
		String query="SELECT AVG(timestampdiff(SECOND,receive_time,finish_time)) FROM `bureauv2alarms`.`alarming_alarm` WHERE (finish_time is not null) and (receive_time>='" + new java.sql.Timestamp(yearAgo.getTime()) + "');";
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				time=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function averageResponseTime in class ActiveAlarmDAO");
		}
		return time;
	}

	@Override
	public void finishAlarm(long alarm_id, Date finishTime) {
		Connection dbConn;
		String query="SELECT hold_end_time,time_taken FROM `bureauv2alarms`.`alarming_activealarm` WHERE alarm_id=" + alarm_id +";";
		Timestamp hold=null,taken=null;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			Statement stmnt=dbConn.createStatement();
			ResultSet result= stmnt.executeQuery(query);
			while (result.next()){
				hold=result.getTimestamp("hold_end_time");
				taken=result.getTimestamp("time_taken");
			}
			if (taken!=null){
			query="UPDATE `bureauv2alarms`.`alarming_alarm` SET `finish_time` = ?,`time_taken` = timestampdiff(MINUTE,DATE_ADD(?,INTERVAL -10 MINUTE),NOW())," +
				 "`time_held` = ? WHERE `alarm_id` = ?;";
			}else{
				query="UPDATE `bureauv2alarms`.`alarming_alarm` SET `finish_time` = ?,`time_taken` = ?," +
				 "`time_held` = ? WHERE `alarm_id` = ?;";
			}
			PreparedStatement statement = dbConn.prepareStatement(query);
			statement.setTimestamp(1, (java.sql.Timestamp) finishTime);
			if (taken!=null){
				statement.setTimestamp(2, taken);
			}else{
				statement.setInt(2,0);
			}
			if (hold!=null){
				statement.setInt(3, 15);
			}else{
				statement.setInt(3,0);
			}
			statement.setLong(4,alarm_id);
			statement.executeUpdate();
		}catch (SQLException e){
			System.out.println("SQL Exception in function finishAlarm in class ActiveAlarmDAO");
		}
		
	}

	@Override
	public Timestamp getReceiveTime(long alarmId) {
		Timestamp receiveTime=null;
		Connection dbConn;
		String query="SELECT receive_time FROM `bureauv2alarms`.`alarming_alarm` WHERE alarm_id=?";
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			PreparedStatement getRecvTime=dbConn.prepareStatement(query);
			getRecvTime.setLong(1, alarmId);
			ResultSet result=getRecvTime.executeQuery();
			while (result.next()){
				receiveTime=result.getTimestamp("receive_time");
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getReceiveTime in class ActiveAlarmDAO");
		}
		return receiveTime;
	}

	@Override
	public List<LocalActiveAlarm> getAllActiveAlarms() {
		List<LocalActiveAlarm> activeAlarms  = new ArrayList <LocalActiveAlarm> ();
		Connection dbConn;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
			//String query="SELECT * FROM `bureauv2alarms`.`alarming_activealarm`;";

String query="SELECT laa.alarm_id,laa.alarm_type,laa.receive_time,laa.site_code,laa.alarm_string,laa.controller_type,laa.controller_name,laa.site_name,laa.alarmasset_name,aasset.fixture_type FROM `bureauv2alarms`.`alarming_localactivealarm`  as laa Inner Join `bureauv2alarms`.`alarming_alarm` as alarm on laa.alarm_id = alarm.alarm_id Inner Join `bureauv2alarms`.`alarming_alarmassets` as aasset on alarm.alarmasset_id = aasset.alarmasset_id;"; 
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				LocalActiveAlarm activealarm=new LocalActiveAlarm(result.getLong("alarm_id"),result.getString("alarm_type"),result.getTimestamp("receive_time"),result.getString("site_code"),result.getString("alarm_string")
						,result.getString("controller_type"),result.getString("controller_name"),result.getString("fixture_type"),false,false,result.getString("site_name"));
				AlarmAsset asset=assetDB.getAlarmAsset(dbBureauConn,result.getInt("alarmasset_id"));
				activealarm.setFixtureType(asset.getFixtureType());
				asset=null;
				
				activeAlarms.add(activealarm);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getAllActiveAlarms in class ActiveAlarmDAO");
		}
		return activeAlarms;
	}

	
	@Override
	public void clearAllAlarms(String username) {
		Connection dbConn;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			CallableStatement cs=dbConn.prepareCall("{ call clear_all_alarms(?) }");
			cs.setString(1, username);
			cs.execute();
		}catch (SQLException e){
			System.out.println("SQL Exception in function clearAllAlarms in class ActiveAlarmDAO");
		}
		
	}
	
	@Override
	public List<LocalActiveAlarm> getUnknownSiteAlarms(int userId) {
		List<LocalActiveAlarm> activeAlarms  = new ArrayList <LocalActiveAlarm> ();
		Connection dbConn;
		try{
			ConnectionBean.getInstance().closeMYSQLConnection();
			dbConn=ConnectionBean.getInstance().getAlarmMYSQLConnection();
			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
			//String query="SELECT * FROM `bureauv2alarms`.`alarming_activealarm` WHERE (user_id is null or user_id=" + userId + ") and site_code='[Unknown]' ORDER BY priority,receive_time;";
			
String query="SELECT laa.alarm_id,laa.alarm_type,laa.receive_time,laa.site_code,laa.alarm_string,laa.controller_type,laa.controller_name,laa.site_name,laa.alarmasset_name,aasset.fixture_type FROM `bureauv2alarms`.`alarming_localactivealarm`  as laa Inner Join `bureauv2alarms`.`alarming_alarm` as alarm on laa.alarm_id = alarm.alarm_id Inner Join `bureauv2alarms`.`alarming_alarmassets` as aasset on alarm.alarmasset_id = aasset.alarmasset_id WHERE (user_id is null or user_id=" + userId + ") and site_code='[Unknown]' ORDER BY priority,receive_time;";
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				LocalActiveAlarm activealarm=new LocalActiveAlarm(result.getLong("alarm_id"),result.getString("alarm_type"),result.getTimestamp("receive_time"),result.getString("site_code"),result.getString("alarm_string")
						,result.getString("controller_type"),result.getString("controller_name"),result.getString("fixture_type"),false,false,result.getString("site_name"));
				AlarmAsset asset=assetDB.getAlarmAsset(dbBureauConn,result.getInt("alarmasset_id"));
				activealarm.setFixtureType(asset.getFixtureType());
				activealarm.setFixtureType(asset.getFixtureType());
				asset=null;
	
				activealarm.setSubfixture(almAssetSearch.getSubfixture(dbBureauConn, result.getInt("alarmasset_id"), activealarm.getControllerName(), activealarm.getAlarmString()));
				activeAlarms.add(activealarm);
				setAlarmUser(activealarm.getAlarmId(), userId);
			}
			result.getStatement().close();
		}catch (SQLException e){
			System.out.println("SQL Exception in function getUnknownSiteAlarms in class ActiveAlarmDAO");
		}
		return activeAlarms;
	}

	@Override
	public void setAlarmsSites(List<LocalActiveAlarm> alarms) {
		Connection dbConn;
		try{
			ConnectionBean.getInstance().closeMYSQLConnection();
			dbConn=ConnectionBean.getInstance().getAlarmMYSQLConnection();
			for (int i=0;i<=alarms.size()-1;i++){
				String query="INSERT INTO `bureauv2alarms`.`alarming_rawalarm` (`alarm_id`,`controller_type`,`receive_time`,`site_code`,`alarm_string`," +
						"`controller_name`,`legacy_alarm_id`,`call_complete`,`alarm_time`) VALUES (?,?,?,?,?,?,?,?,?);";
				PreparedStatement stmnt=dbConn.prepareStatement(query);
				stmnt.setLong(1, alarms.get(i).getAlarmId());
				stmnt.setString(2, alarms.get(i).getControllerType());
				stmnt.setTimestamp(3, alarms.get(i).getReceiveTime());
				stmnt.setString(4, alarms.get(i).getSiteCode());
				stmnt.setString(5, alarms.get(i).getAlarmString());
				stmnt.setString(6, alarms.get(i).getControllerName());
				stmnt.setNull(7, java.sql.Types.BIGINT);
				stmnt.setNull(8, java.sql.Types.BIGINT);
				stmnt.setTimestamp(9, alarms.get(i).getReceiveTime());
				stmnt.executeUpdate();
				
				query="DELETE FROM `bureauv2alarms`.`alarming_activealarm` WHERE alarm_id=?";
				stmnt=dbConn.prepareStatement(query);
				stmnt.setLong(1, alarms.get(i).getAlarmId());
				stmnt.executeUpdate();	
			}
			
		}catch(SQLException e){
			System.out.println("SQL Exception in function setAlarmsSites in class ActiveAlarmDAO");
		}
		
	}

	@Override
	public List<LocalActiveAlarm> getDeptActiveAlarms(int depId,int userId) {
		String assetIds=assetDB.getDeptAlarmAssetIds(depId);
		List<LocalActiveAlarm> activeAlarms  = new ArrayList <LocalActiveAlarm> ();
		Connection dbConn;
		try{
			ConnectionBean.getInstance().closeMYSQLConnection();
			dbConn=ConnectionBean.getInstance().getAlarmMYSQLConnection();
			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
		//	String query="SELECT * FROM `bureauv2alarms`.`alarming_activealarm` WHERE alarmasset_id IN (" + assetIds +") and (user_id is null or user_id=" + userId +") and ((hold_end_time is null or hold_end_time<=NOW()) or (time_taken<=NOW())) ORDER BY priority,receive_time;";
			
String query="SELECT laa.alarm_id,laa.alarm_type,laa.receive_time,laa.site_code,laa.alarm_string,laa.controller_type,laa.controller_name,laa.site_name,laa.alarmasset_name,aasset.fixture_type FROM `bureauv2alarms`.`alarming_localactivealarm`  as laa Inner Join `bureauv2alarms`.`alarming_alarm` as alarm on laa.alarm_id = alarm.alarm_id Inner Join `bureauv2alarms`.`alarming_alarmassets` as aasset on alarm.alarmasset_id = aasset.alarmasset_id WHERE alarmasset_id IN (" + assetIds +") and (user_id is null or user_id=" + userId +") and ((hold_end_time is null or hold_end_time<=NOW()) or (time_taken<=NOW())) ORDER BY priority,receive_time;";
			Statement statement=dbConn.createStatement();
			ResultSet result=statement.executeQuery(query);
			while(result.next()){
				LocalActiveAlarm activealarm=new LocalActiveAlarm(result.getLong("alarm_id"),result.getString("alarm_type"),result.getTimestamp("receive_time"),result.getString("site_code"),result.getString("alarm_string")
						,result.getString("controller_type"),result.getString("controller_name"),result.getString("fixture_type"),false,false,result.getString("site_name"));
				AlarmAsset asset=assetDB.getAlarmAsset(dbBureauConn,result.getInt("alarmasset_id"));
				activealarm.setFixtureType(asset.getFixtureType());
				asset=null;
			    activealarm.setSubfixture(almAssetSearch.getSubfixture(dbBureauConn, result.getInt("alarmasset_id"), activealarm.getControllerName(), activealarm.getAlarmString()));
				setAlarmUser(activealarm.getAlarmId(), userId);
				activeAlarms.add(activealarm);
			}
			result.getStatement().close();
		}catch (SQLException e){
			System.out.println("SQL Exception in function getDeptActiveAlarms(int depId) in class ActiveAlarmDAO");
			e.printStackTrace();
		}
		return activeAlarms;
	}

	@Override
	public int getOverdueAlarmsCount() {
		int overdueCount=0;
		String query="SELECT COUNT(alarm_id) as tbl1 FROM (SELECT * FROM `bureauv2alarms`.`alarming_activealarm` WHERE receive_time<DATE_ADD(NOW(),INTERVAL -30 MINUTE) ORDER BY priority,receive_time LIMIT 0,200) as tbl2;";
		Connection dbConn;
		try{
			ConnectionBean.getInstance().closeMYSQLConnection();
			dbConn=ConnectionBean.getInstance().getAlarmMYSQLConnection();
			PreparedStatement stmnt=dbConn.prepareStatement(query);
			ResultSet result=stmnt.executeQuery();
			while (result.next()){
				overdueCount=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getOverdueAlarmsCount in class ActiveAlarmDAO");
		}
		return overdueCount;
	}

	@Override
	public int allActiveAlarmsCount() {
		int alarmsCount=0;
		String query="SELECT COUNT(alarm_id) FROM `bureauv2alarms`.`alarming_activealarm`;";
		Connection dbConn;
		try{
			ConnectionBean.getInstance().closeMYSQLConnection();
			dbConn=ConnectionBean.getInstance().getAlarmMYSQLConnection();
			PreparedStatement stmnt=dbConn.prepareStatement(query);
			ResultSet result=stmnt.executeQuery();
			while (result.next()){
				 alarmsCount=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function allActiveAlarmsCount in class ActiveAlarmDAO");
		}
		return  alarmsCount;
	}

	@Override
	public int getDeptAlarmsOnHold(int depId) {
		int onHoldCount=0;
		String assetIds=assetDB.getDeptAlarmAssetIds(depId);
		Connection dbConn;
		try{
			ConnectionBean.getInstance().closeMYSQLConnection();
			dbConn=ConnectionBean.getInstance().getAlarmMYSQLConnection();
			String query="SELECT COUNT(alarm_Id) FROM `bureauv2alarms`.`alarming_activealarm` WHERE alarmasset_id IN ("+assetIds +") and hold_end_time>NOW();";
			PreparedStatement statement=dbConn.prepareStatement(query);
			ResultSet result=statement.executeQuery();
			while(result.next()){
				onHoldCount=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getDeptAlarmsOnHold(int depId) in class ActiveAlarmDAO");
			e.printStackTrace();
		}
		return onHoldCount;
	}
}
