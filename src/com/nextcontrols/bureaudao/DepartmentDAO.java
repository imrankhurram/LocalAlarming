package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.Department;

public class DepartmentDAO implements IDepartmentDAO, Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Connection dbConn=null;
	
	public void dbConnect(){
		try {
			dbConn=ConnectionBean.getInstance().getBureauConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<Department> getDepartmentList(String branch_code) {
		List<Department> departmentList=new ArrayList<Department> ();
		dbConnect();
		String query="SELECT [dep_id],[version],[name],[type] FROM [Departments] WHERE [branch_code]='" + branch_code +"'";
		try {
			Statement stmnt=dbConn.createStatement();
			ResultSet result = stmnt.executeQuery(query);
			while (result.next()){
				Department newDept=new Department(result.getInt("dep_id"),result.getInt("version"),branch_code,result.getString("name"),result.getString("type"));
				departmentList.add(newDept);
			}
			
		} catch (SQLException e) {
			System.out.println("SQL Exception in function getDepartmentList in class DepartmentDAO");
		}
		return departmentList;
	}

	@Override
	public void addDepartment(int version, String branch_code,
			String name, String type) {
		
		dbConnect();
		String query="INSERT INTO [Departments]([version],[branch_code],[name],[type])" +
				" VALUES(" + version + ",'" + branch_code + "','" + name + "','" + type + "')";
		try{
			Statement stmnt=dbConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function addDepartment in class DepartmentDAO");
		}
		
	}

	@Override
	public void modifyDepartment(int dep_id, int version, String name,
			String type) {
		dbConnect();
		String query="UPDATE [Departments] " +
				"SET [version] = " + version + ", " +
				"[name] = '" + name + "'," +
				"[type] = '" + type + "' WHERE [dep_id]=" + dep_id;
		try{
			Statement stmnt=dbConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function modifyDepartment in class DepartmentDAO");
		}
		
	}

	@Override
	public void deleteDepartment(int dep_id) {
		dbConnect();
		String query="DELETE FROM [Departments] WHERE [dep_id]=" + dep_id;
		try{
			Statement stmnt=dbConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("SQL Exception in function deleteDepartment in class DepartmentDAO");
		}
	}

	@Override
	public int copyDepartment(int version, String branch_code, String name,
			String type) {
		int depId=0;
		dbConnect();
		String query="INSERT INTO [Departments]([version],[branch_code],[name],[type]) " +
				"VALUES(" + version + ",'" + branch_code + "','" + name + "','" + type + "') SELECT SCOPE_IDENTITY()";
		try{
			Statement stmnt=dbConn.createStatement();
			ResultSet result = stmnt.executeQuery(query);
			while (result.next()){
				depId=result.getInt(1);
			}
		}catch (SQLException e) {
			System.out.println("SQL Exception in function copyDepartment in class DepartmentDAO");
		}
		return depId;
	}

	@Override
	public String getDepartmentName(Connection dbConn,int dep_id) {
		String deptName="";
		String query="SELECT [name] FROM [Departments] WHERE [dep_id]=?";
		try{
			PreparedStatement getName=dbConn.prepareStatement(query);
			getName.setInt(1, dep_id);
			ResultSet result=getName.executeQuery();
			while (result.next()){
				deptName=result.getString("name");
			}
		}catch (SQLException e) {
			System.out.println("SQL Exception in function getDepartmentName in class DepartmentDAO");
		}
		return deptName;
	}
	
	public String getDepartmentName(int dep_id) {
		dbConnect();
		String deptName="";
		String query="SELECT [name] FROM [Departments] WHERE [dep_id]=?";
		try{
			PreparedStatement getName=dbConn.prepareStatement(query);
			getName.setInt(1, dep_id);
			ResultSet result=getName.executeQuery();
			while (result.next()){
				deptName=result.getString("name");
			}
		}catch (SQLException e) {
			System.out.println("SQL Exception in function getDepartmentName in class DepartmentDAO");
		}
		return deptName;
	}

}
