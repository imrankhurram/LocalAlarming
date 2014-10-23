package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import com.nextcontrols.utility.ServiceProperties;

public class UserActivityDAO implements IUserActivityDAO, Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void userLogin(String username, Timestamp login_time,
			Timestamp last_check, String userType){
		
		
		String deleteQuery="DELETE FROM `bureauv2alarms`.`loggedin_users` WHERE username='" + username + "';";
		String query="INSERT INTO `bureauv2alarms`.`loggedin_users` (`username`,`login_time`,`last_update`,`user_type`) " +
				"VALUES ('"+ username + "','" + login_time + "','" + last_check +"','" + userType + "');" ;
		try{
			Connection connect=ConnectionBean.getInstance().getMYSQLConnection();
			Statement stmnt=connect.createStatement();
			stmnt.executeUpdate(deleteQuery);
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function userLogin in class UserActivityDAO");
		}
	}

	@Override
	public void userLogout(String username) {
		String query="DELETE FROM `bureauv2alarms`.`loggedin_users` WHERE username='" + username + "';";
		try{
			Connection connect=ConnectionBean.getInstance().getMYSQLConnection();
			Statement stmnt=connect.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function userLogout in class UserActivityDAO");
		}
	}

	@Override
	public List<String> userUpdateCheck(String username, Timestamp last_update) {
		String query="UPDATE `bureauv2alarms`.`loggedin_users` SET last_update='" + last_update + "' WHERE username='" + username + "';";
		try{
			Connection connect=ConnectionBean.getInstance().getMYSQLConnection();
			Statement stmnt=connect.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function userUpdateCheck in class UserActivityDAO");
		}
		
	
		Calendar last_check=Calendar.getInstance();
		Date new_date=new Date(last_update.getTime());
		last_check.setTime(new_date);
		last_check.setTimeInMillis(last_check.getTimeInMillis() - 120000); //setting the time 2 minutes back
		new_date=last_check.getTime();
		query="DELETE FROM `bureauv2alarms`.`loggedin_users` WHERE last_update<'" + new Timestamp(new_date.getTime()) + "';";
		try{
			Connection connect=ConnectionBean.getInstance().getMYSQLConnection();
			Statement stmnt=connect.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function userUpdateCheck in class UserActivityDAO");
		}
		
		List<String> usersLoggedIn=new ArrayList<String>();
		query="SELECT username FROM `bureauv2alarms`.`loggedin_users`";
		try{
			Connection connect=ServiceProperties.getInstance().getConnectionMQSQLDB();
			Statement stmnt=connect.createStatement();
			ResultSet results=stmnt.executeQuery(query);
			while (results.next()){
				String userName=results.getString("username");
				usersLoggedIn.add(userName);
			}
			connect.close();
		}catch (SQLException e) {
			System.out.println("SQL Exception in function userUpdateCheck in class UserActivityDAO");
		}
		return usersLoggedIn;
	}
	
	public int getNumberUsersOnline(){
		int number=0;
		String query="SELECT COUNT(DISTINCT username) FROM `bureauv2alarms`.`loggedin_users`;";
		try{
			Connection connect=ConnectionBean.getInstance().getMYSQLConnection();
			Statement stmnt=connect.createStatement();
			ResultSet result = stmnt.executeQuery(query);
			while (result.next()){
				number=result.getInt(1);
			}
		}catch (SQLException e) {
			System.out.println("SQL Exception in function getNumberUsersOnline in class UserActivityDAO");
		}
		return number;
	}

}
