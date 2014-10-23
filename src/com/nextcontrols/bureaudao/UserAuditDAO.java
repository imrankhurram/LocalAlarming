package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.nextcontrols.bureaudomain.UserAudit;
import com.nextcontrols.utility.ServiceProperties;


public class UserAuditDAO implements IUserAuditDAO, Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static IUserAuditDAO instance;
 	private Connection dbConn;

	/** *************** Factory Methods ************************* */
	public static IUserAuditDAO getInstance() { 
        if (instance != null) {
            return instance; 
        } else {
            return new UserAuditDAO();
        }
    }
    
	public static void setInstance(IUserAuditDAO inst) {
		instance = inst;
	}
	
	private void dbConnection(){
		try {
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public List<UserAudit> getUserAudits() {
		List<UserAudit> listOfAudits=new ArrayList<UserAudit>();
		String query="SELECT * FROM `bureauv2alarms`.`audits_useraudit`;";
		dbConnection();
		try{
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				UserAudit newAudit=new UserAudit(result.getInt("user_id"),result.getTimestamp("audit_date"),result.getString("action_type"),result.getString("action_description"),result.getString("site_code"));
				listOfAudits.add(newAudit);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getUserAudits in class UserAuditDAO");
		}
		
		return listOfAudits;
	}
	
	@Override
	public void insertUserAudit(UserAudit userAudit) {
		dbConnection();
		String query="";
		if (userAudit.getSiteCode()!=null){
			query="INSERT INTO `bureauv2alarms`.`audits_useraudit` (`user_id`,`audit_date`,`action_type`,`action_description`,`site_code`) " +
				"VALUES("+ userAudit.getUserId() + ",'" + userAudit.getAuditDate() + "','" + userAudit.getActionType() + 
				"','"+ userAudit.getActionDescription() +  "','" + userAudit.getSiteCode() +"');";
		}else{
			query="INSERT INTO `bureauv2alarms`.`audits_useraudit` (`user_id`,`audit_date`,`action_type`,`action_description`) " +
			"VALUES("+ userAudit.getUserId() + ",'" + userAudit.getAuditDate() + "','" + userAudit.getActionType() + 
			"','"+ userAudit.getActionDescription() +"');";
		}
		try{
			Statement stmnt=dbConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function insertUserAudit in class UserAuditDAO");
		}
	}
	@Override
	public void insertUserAdminAudit(String userId, String eventDesc,
			String branchCode, int websiteId) {
		// working here
		PreparedStatement pstmt = null;
		Connection dbConn = null;
		String query = "";
		query = "INSERT INTO [webGUI_Auditing] ([user_id],[audit_date],[branchCode],[event],[eventDesc],[website_id]) "
				+ "VALUES(?,?,?,?,?,?);";
		try {
			dbConn = ConnectionBean.getInstance().getBureauConnection();
			pstmt = dbConn.prepareStatement(query);
			pstmt.setString(1, userId);
			pstmt.setTimestamp(2, new Timestamp(Calendar.getInstance()
					.getTime().getTime()));// pstmt.setTimestamp(2,
											// userAudit.getAuditDate());
			if (branchCode != null) {
				pstmt.setString(3, branchCode);
			} else {
				pstmt.setNull(3, Types.VARCHAR);
			}
			pstmt.setString(4, "Account admin");
			pstmt.setString(5, eventDesc);
			if (websiteId != -1) {
				pstmt.setInt(6, websiteId);
			} else {
				pstmt.setNull(6, Types.INTEGER);
			}
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			pstmt = null;
			dbConn = null;
		}
	}

	@Override
	public int countUserIdles(int userId) {
		int idles=0;
		String query="SELECT count(*) FROM `bureauv2alarms`.`audits_useraudit` WHERE user_id=? and action_description like 'The user has been inactive%';";
		try{
			dbConn=ServiceProperties.getInstance().getConnectionMQSQLDB();
			PreparedStatement count=dbConn.prepareStatement(query);
			count.setInt(1, userId);
			ResultSet result=count.executeQuery();
			while (result.next()){
				idles=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function countUserIdles in class UserAuditDAO");
		}
		
		return idles;
	}

	@Override
	public List<UserAudit> getSpecificUserAudits(int user_id, String siteCode) {
		UserDAO userDB=new UserDAO();
		List<UserAudit> listOfAudits=new ArrayList<UserAudit>();
		String query="SELECT * FROM `bureauv2alarms`.`audits_useraudit` WHERE user_id=" + user_id + " and site_code='" + siteCode + "';";
		dbConnection();
		try{
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				UserAudit newAudit=new UserAudit(result.getInt("user_id"),result.getTimestamp("audit_date"),result.getString("action_type"),result.getString("action_description"),result.getString("site_code"));
				newAudit.setUserName(userDB.getUserName(newAudit.getUserId()));
				listOfAudits.add(newAudit);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getSpecificUserAudits in class UserAuditDAO");
		}
		
		return listOfAudits;
	}

	@Override
	public List<UserAudit> getDateSpecificUserAudits(int user_id,
			String siteCode, Date dateFrom, Date dateTo) {
		UserDAO userDB=new UserDAO();
		List<UserAudit> listOfAudits=new ArrayList<UserAudit>();
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT * FROM `bureauv2alarms`.`audits_useraudit` WHERE user_id=" + user_id + " and site_code='" + siteCode + "';";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT * FROM `bureauv2alarms`.`audits_useraudit` WHERE user_id=" + user_id + " and site_code='" + siteCode + "' and audit_date>='" + new java.sql.Date(dateFrom.getTime()) + "';";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT * FROM `bureauv2alarms`.`audits_useraudit` WHERE user_id=" + user_id + " and site_code='" + siteCode + "' and audit_date<='" + new java.sql.Date(dateTo.getTime()) + "';";
		}else{
			query="SELECT * FROM `bureauv2alarms`.`audits_useraudit` WHERE user_id=" + user_id + " and site_code='" + siteCode + "' and audit_date>='" + new java.sql.Date(dateFrom.getTime()) + "' and audit_date<='" + new java.sql.Date(dateTo.getTime()) + "';";
		}
		dbConnection();
		try{
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				UserAudit newAudit=new UserAudit(result.getInt("user_id"),result.getTimestamp("audit_date"),result.getString("action_type"),result.getString("action_description"),result.getString("site_code"));
				newAudit.setUserName(userDB.getUserName(newAudit.getUserId()));
				listOfAudits.add(newAudit);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getSpecificUserAudits in class UserAuditDAO");
		}
		
		return listOfAudits;
	}

	@Override
	public int avgAlarmHandlingTime(String username, Date dateFrom, Date dateTo) {
		int avgTime=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT AVG(time_taken) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username + "';";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT AVG(time_taken) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT AVG(time_taken) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT AVG(time_taken) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		dbConnection();
		try{
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				avgTime=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function avgAlarmHandlingTime in class UserAuditDAO");
		}
		return avgTime;
	}

	@Override
	public int maxAlarmHandlingTime(String username, Date dateFrom, Date dateTo) {
		int maxTime=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT MAX(time_taken) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username + "';";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT MAX(time_taken) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT MAX(time_taken) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT MAX(time_taken) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		dbConnection();
		try{
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				maxTime=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function maxAlarmHandlingTime in class UserAuditDAO");
		}
		return maxTime;
	}

	@Override
	public int minAlarmHandlingTime(String username, Date dateFrom, Date dateTo) {
		int minTime=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT MIN(time_taken) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username + "';";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT MIN(time_taken) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT MIN(time_taken) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT MIN(time_taken) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		dbConnection();
		try{
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				minTime=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function minAlarmHandlingTime in class UserAuditDAO");
		}
		return minTime;
	}

	@Override
	public int totalAlmsHandled(String username, Date dateFrom, Date dateTo) {
		int count=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username + "' and (finish_time is not null) and `bureauv2alarms`.`audits_alarmaudit`.action_taken!='Cleared';";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `bureauv2alarms`.`audits_alarmaudit`.action_taken!='Cleared' " +
							"and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `bureauv2alarms`.`audits_alarmaudit`.action_taken!='Cleared' " +
					"and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `bureauv2alarms`.`audits_alarmaudit`.action_taken!='Cleared' and " +
					"(finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		dbConnection();
		try{
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				count=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function totalAlmsHandled in class UserAuditDAO");
		}
		return count;
	}

	@Override
	public int totalAlmsCleared(String username, Date dateFrom, Date dateTo) {
		int count=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username + "' and (finish_time is not null) and `bureauv2alarms`.`audits_alarmaudit`.action_taken='Cleared';";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `bureauv2alarms`.`audits_alarmaudit`.action_taken='Cleared' " +
							"and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `bureauv2alarms`.`audits_alarmaudit`.action_taken='Cleared' " +
					"and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `bureauv2alarms`.`audits_alarmaudit`.action_taken='Cleared' and " +
					"(finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		dbConnection();
		try{
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				count=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function totalAlmsCleared in class UserAuditDAO");
		}
		return count;
	}

	@Override
	public int totalAlmsHeld(String username, Date dateFrom, Date dateTo) {
		int count=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username + "' and (finish_time is not null) and `bureauv2alarms`.`alarming_alarm`.time_held>0;";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `bureauv2alarms`.`alarming_alarm`.time_held>0 " +
							"and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `bureauv2alarms`.`alarming_alarm`.time_held>0 " +
					"and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `bureauv2alarms`.`alarming_alarm`.time_held>0 and " +
					"(finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		dbConnection();
		try{
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				count=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function totalAlmsHeld class UserAuditDAO");
		}
		return count;
	}

	@Override
	public int totalAlmsOverdue(String username, Date dateFrom, Date dateTo) {
		int count=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username + "' and (finish_time is not null) and timestampdiff(MINUTE,`bureauv2alarms`.`alarming_alarm`.receive_time,`bureauv2alarms`.`alarming_alarm`.finish_time)>30;";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
					"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and timestampdiff(MINUTE,`bureauv2alarms`.`alarming_alarm`.receive_time,`bureauv2alarms`.`alarming_alarm`.finish_time)>30 " +
							"and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and timestampdiff(MINUTE,`bureauv2alarms`.`alarming_alarm`.receive_time,`bureauv2alarms`.`alarming_alarm`.finish_time)>30 " +
					"and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT COUNT(*) FROM `bureauv2alarms`.`alarming_alarm` INNER JOIN `bureauv2alarms`.`audits_alarmaudit` on `bureauv2alarms`.`alarming_alarm`.alarm_id=`bureauv2alarms`.`audits_alarmaudit`.alarm_id " +
			"WHERE `bureauv2alarms`.`audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and timestampdiff(MINUTE,`bureauv2alarms`.`alarming_alarm`.receive_time,`bureauv2alarms`.`alarming_alarm`.finish_time)>30 and " +
					"(finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		dbConnection();
		try{
			Statement stmnt=dbConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while(result.next()){
				count=result.getInt(1);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function totalAlmsOverdue class UserAuditDAO");
		}
		return count;
	}

	

	
	     
}
