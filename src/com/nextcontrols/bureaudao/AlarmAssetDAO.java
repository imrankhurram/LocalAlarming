package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nextcontrols.bureaudomain.AlarmAsset;


public class AlarmAssetDAO implements IAlarmAssetDAO, Serializable{

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
	public List<AlarmAsset> getDeptAlarmAssets(int departmentId) {
		List<AlarmAsset> deptAlarmAssets= new ArrayList<AlarmAsset> ();
		String query="SELECT * FROM [AlarmAssets] WHERE [dep_id]=" + departmentId + " ORDER BY [Fixture_Name]";
		dbBureauConnect();
		try{
			Statement stmnt=dbBureauConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while (result.next()){
				AlarmAsset newAsset = new AlarmAsset(result.getInt("alarmasset_id"),result.getInt("dep_id"),result.getString("Fixture_Name"),
						result.getString("Fixture_Type"),result.getBigDecimal("Stock_Value"),result.getFloat("Low_Temp"),result.getFloat("High_Temp"),
						result.getFloat("Low_Temp_Offset"),result.getFloat("High_Temp_Offset"),result.getDate("Installation_Date"),result.getString("Value_Currency"));
				deptAlarmAssets.add(newAsset);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getDeptAlarmAssets in class AlarmAssetDAO");
		}
		return deptAlarmAssets;
	}
	
	@Override
	public List<AlarmAsset> getSiteAlarmAssets(String siteCode) {
		DepartmentDAO deptDB=new DepartmentDAO();
		List<AlarmAsset> siteAlarmAssets= new ArrayList<AlarmAsset> ();
		String query="SELECT * FROM [AlarmAssets] WHERE [dep_id] IN (SELECT dep_id FROM [BureauV2].[dbo].[Departments] WHERE [branch_code]=?) ORDER BY [Fixture_Name]";
		dbBureauConnect();
		try{
			PreparedStatement stmnt=dbBureauConn.prepareStatement(query);
			stmnt.setString(1, siteCode);
			ResultSet result=stmnt.executeQuery();
			while (result.next()){
				AlarmAsset newAsset = new AlarmAsset(result.getInt("alarmasset_id"),result.getInt("dep_id"),result.getString("Fixture_Name"),
						result.getString("Fixture_Type"),result.getBigDecimal("Stock_Value"),result.getFloat("Low_Temp"),result.getFloat("High_Temp"),
						result.getFloat("Low_Temp_Offset"),result.getFloat("High_Temp_Offset"),result.getDate("Installation_Date"),result.getString("Value_Currency"));
				newAsset.setDeptName(deptDB.getDepartmentName(dbBureauConn, result.getInt("dep_id")));
				siteAlarmAssets.add(newAsset);
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getSiteAlarmAssets in class AlarmAssetDAO");
		}
		return siteAlarmAssets;
	}

	@Override
	public void addAlarmAsset(int departmentId, String fixtureName,
			String fixtureType, BigDecimal stockValue, float lowTemp,
			float highTemp, float lowTempOffset, float highTempOffset,Date installationDate,String currency) {
		
		dbBureauConnect();
		String query="INSERT INTO [AlarmAssets] ([dep_id],[Fixture_Name],[Fixture_Type],[Stock_Value],[Low_Temp],[High_Temp]" +
				",[Low_Temp_Offset],[High_Temp_Offset],[Installation_Date],[Value_Currency]) VALUES" +
				"(" + departmentId + ",'" + fixtureName + "','" + fixtureType + "'," + stockValue + "," + lowTemp + "," + highTemp + "," +
				lowTempOffset + "," + highTempOffset + ",'" + new java.sql.Date(installationDate.getTime()) + "','" + currency + "')";
		try{
			Statement stmnt=dbBureauConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function addAlarmAsset in class AlarmAssetDAO");
		}
		
	}

	@Override
	public void deleteAlarmAsset(int alarmAssetId) {
		String query="DELETE FROM [AlarmAssets] WHERE [alarmasset_id]=" + alarmAssetId;
		dbBureauConnect();
		try{
			Statement stmnt=dbBureauConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function deleteAlarmAsset in class AlarmAssetDAO");
		}
	}

	@Override
	public void modifyAlarmAsset(int alarmAssetId, int departmentId,
			String fixtureName, String fixtureType, BigDecimal stockValue,
			float lowTemp, float highTemp, float lowTempOffset,
			float highTempOffset, Date installationDate,String currency) {
		
		String query="UPDATE [AlarmAssets] " +
				"SET [dep_id] =" + departmentId + ", " +
				"[Fixture_Name] = '" + fixtureName + "', " +  
				"[Fixture_Type] = '" + fixtureType + "', " +
				"[Stock_Value] = " + stockValue + ", " +
				"[Low_Temp] = " + lowTemp + ", " +
				"[High_Temp] = " + highTemp + ", " +
				"[Low_Temp_Offset] = " + lowTempOffset + ", " +
				"[High_Temp_Offset] = " + highTempOffset + ", " +
				"[Installation_Date] = '" + new java.sql.Date(installationDate.getTime()) + "', " +
				"[Value_Currency]='" + currency + "' WHERE [alarmasset_id]=" + alarmAssetId;
		dbBureauConnect();
		try{
			Statement stmnt=dbBureauConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function modifyAlarmAsset in class AlarmAssetDAO");
		}
	}


	@Override
	public int copyAlarmAsset(int departmentId, String fixtureName,
			String fixtureType, BigDecimal stockValue, float lowTemp,
			float highTemp, float lowTempOffset, float highTempOffset, Date installationDate,String currency) {
		
		int assetId=0;
		String query="";
		dbBureauConnect();
		if (installationDate!=null){
		query="INSERT INTO [AlarmAssets] ([dep_id],[Fixture_Name],[Fixture_Type],[Stock_Value],[Low_Temp],[High_Temp]" +
				",[Low_Temp_Offset],[High_Temp_Offset],[Installation_Date],[Value_Currency]) VALUES" +
				"(" + departmentId + ",'" + fixtureName + "','" + fixtureType + "'," + stockValue + "," + lowTemp + "," + highTemp + "," +
				lowTempOffset + "," + highTempOffset + ",'" + new java.sql.Date(installationDate.getTime()) + "','" + currency +"') SELECT SCOPE_IDENTITY()";
		}else{
			query="INSERT INTO [AlarmAssets] ([dep_id],[Fixture_Name],[Fixture_Type],[Stock_Value],[Low_Temp],[High_Temp]" +
			",[Low_Temp_Offset],[High_Temp_Offset],[Installation_Date],[Value_Currency]) VALUES" +
			"(" + departmentId + ",'" + fixtureName + "','" + fixtureType + "'," + stockValue + "," + lowTemp + "," + highTemp + "," +
			lowTempOffset + "," + highTempOffset + "," + null + ",'" + currency + "') SELECT SCOPE_IDENTITY()";
		}
		try{
			Statement stmnt=dbBureauConn.createStatement();
			ResultSet result = stmnt.executeQuery(query);
			while (result.next()){
				assetId=result.getInt(1);
			}
		}catch (SQLException e) {
			System.out.println("SQL Exception in function copyAlarmAsset in class AlarmAssetDAO");
		}
		return assetId;
	}


	@Override
	public AlarmAsset getAlarmAsset(Connection dbBureauConn,int alarmAssetId) {
		AlarmAsset asset=new AlarmAsset();
		String query="SELECT [alarmasset_id],[Fixture_Name],[dep_id],[Fixture_Type] FROM [AlarmAssets] WHERE [alarmasset_id]=" + alarmAssetId ;
		try{
			Statement stmnt=dbBureauConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while (result.next()){
				asset.setAlarmAssetId(result.getInt("alarmasset_id"));
				asset.setFixtureName(result.getString("Fixture_Name"));
				asset.setFixtureType(result.getString("Fixture_Type"));
				asset.setDepartmentId(result.getInt("dep_id"));
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getAlarmAsset in class AlarmAssetDAO");
		}
		return asset;
	}

	@Override
	public void moveAlarmAsset(int alarmAssetId, int depId) {
		String query="UPDATE [AlarmAssets] " +
		"SET [dep_id] =" + depId +
		" WHERE [alarmasset_id]=" + alarmAssetId;
		dbBureauConnect();
		try{
			Statement stmnt=dbBureauConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function moveAlarmAsset in class AlarmAssetDAO");
		}
	}

	@Override
	public String getDeptAlarmAssetIds(int departmentId) {
		String assetIds="";
		String query="SELECT [alarmasset_id] FROM [AlarmAssets] WHERE [dep_id]=" + departmentId;
		dbBureauConnect();
		try{
			Statement stmnt=dbBureauConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while (result.next()){
				if (result.isLast()){
					assetIds+= result.getInt("alarmasset_id");
				}else{
					assetIds+= result.getInt("alarmasset_id") + ",";
				}
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getDeptAlarmAssets in class AlarmAssetDAO");
		}
		return assetIds;
	}

	@Override
	public List<String> getFixtureTypes() {
		List<String> types = new ArrayList<String>();
		String query="SELECT DISTINCT [Fixture_Type] FROM [FixtureTypes]";
		dbBureauConnect();
		try{
			Statement stmnt=dbBureauConn.createStatement();
			ResultSet result=stmnt.executeQuery(query);
			while (result.next()){
				types.add(result.getString("Fixture_Type"));
			}
		}catch (SQLException e){
			System.out.println("SQL Exception in function getFixtureTypes in class AlarmAssetDAO");
		}
		return types;
	}
}
