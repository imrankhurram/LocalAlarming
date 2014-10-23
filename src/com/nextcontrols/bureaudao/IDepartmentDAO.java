package com.nextcontrols.bureaudao;

import java.sql.Connection;
import java.util.List;

import com.nextcontrols.bureaudomain.Department;

public interface IDepartmentDAO {
	
	public List<Department> getDepartmentList(String branch_code);
	public void addDepartment(int version,String branch_code,String name,String type);
	public int copyDepartment(int version,String branch_code,String name,String type);
	public void modifyDepartment(int dep_id,int version,String name,String type);
	public void deleteDepartment(int dep_id);
	public String getDepartmentName(Connection dbConn,int dep_id);
	public String getDepartmentName(int dep_id);
}
