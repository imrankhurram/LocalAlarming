package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.AlarmAssetSearchString;

public class AlarmAssetSearchStringDAO implements IAlarmAssetSearchStringDAO, Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Connection dbBureauConn=null;
	
	public void dbBureauConnect(){
		try {
			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public List<AlarmAssetSearchString> getAssetSearchStrings(int alarmAssetId) {
		List<AlarmAssetSearchString> assetSearchStrings = new ArrayList<AlarmAssetSearchString> ();
		dbBureauConnect();
		String query="SELECT * FROM [AlarmAssetsSearchString] WHERE [alarmasset_id]=" + alarmAssetId;
		try{
			Statement stmnt=dbBureauConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while (result.next()){
				AlarmAssetSearchString newSearchString = new AlarmAssetSearchString(result.getLong("Search_String_ID"),result.getInt("alarmasset_id"),
						result.getString("Controller_Search"),result.getString("Alarm_Search"),result.getString("Sub_Fixture"));
				assetSearchStrings.add(newSearchString);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getAssetSearchStrings in class AlarmAssetSearchStringDAO");
		}
		return assetSearchStrings;
	}

	@Override
	public void addAssetSearchString(int alarmAssetId, String controllerSearch,
			String alarmSearch,String subfixture) {
		
		dbBureauConnect();
		String query="INSERT INTO [AlarmAssetsSearchString] ([alarmasset_id],[Controller_Search],[Alarm_Search],[Sub_Fixture]) " +
				"VALUES ("+ alarmAssetId +",'" + controllerSearch + "','" + alarmSearch + "','" + subfixture+ "')";
		try{
			Statement stmnt=dbBureauConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function addAssetSearchString in class AlarmAssetSearchStringDAO");
		}
	}

	@Override
	public void delAssetSearchString(long searchStringId) {
		dbBureauConnect();
		String query="DELETE FROM [AlarmAssetsSearchString] WHERE [Search_String_ID]=" + searchStringId;
		try{
			Statement stmnt=dbBureauConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function delAssetSearchString in class AlarmAssetSearchStringDAO");
		}
	}

	@Override
	public void modAssetSearchString(long searchStringId, int alarmAssetId,
			String controllerSearch, String alarmSearch,String subfixture) {
		
		dbBureauConnect();
		String query="UPDATE [AlarmAssetsSearchString] " +
				"SET [alarmasset_id] = " + alarmAssetId + "," +
				"[Controller_Search] = '" + controllerSearch + "'," +
				"[Alarm_Search] = '" + alarmSearch+ "'," +
				"[Sub_Fixture] = '" + subfixture + "' WHERE [Search_String_ID]= " + searchStringId;
		System.out.println(query);
		try{
			Statement stmnt=dbBureauConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function modAssetSearchString in class AlarmAssetSearchStringDAO");
		}
	}
	
	public String getSubfixture(Connection dbBureauConn,int alarmassetId,String controllerSearch,String alarmString){
		String subfixture="";
		String query="SELECT [Sub_Fixture] FROM [AlarmAssetsSearchString] WHERE [alarmasset_id]=? AND ? LIKE  [Controller_Search] AND ? LIKE [Alarm_Search]";
		try {
			PreparedStatement prepStatement=dbBureauConn.prepareStatement(query);
			prepStatement.setInt(1, alarmassetId);
			prepStatement.setString(2, controllerSearch);
			prepStatement.setString(3, alarmString);
			ResultSet result=prepStatement.executeQuery();
			while (result.next()){
				subfixture=result.getString("Sub_Fixture");
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception in function getSubfixture in class AlarmAssetSearchStringDAO");
		}
		
		return subfixture;
	}

}
