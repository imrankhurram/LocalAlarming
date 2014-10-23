package com.nextcontrols.bureaudomain;

import java.io.Serializable;

public class Department implements Serializable{
	
	private int dep_id;
	private int version;
	private String branch_code;
	private String name;
	private String type;
	
	public Department(){}
	
	public Department(int dep_id,int version,String branch_code,String name,String type){
		this.dep_id=dep_id;
		this.version=version;
		this.branch_code=branch_code;
		this.name=name;
		this.type=type;
	}

	public void setDep_id(int dep_id) {
		this.dep_id = dep_id;
	}

	public int getDep_id() {
		return dep_id;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}

	public void setBranch_code(String branch_code) {
		this.branch_code = branch_code;
	}

	public String getBranch_code() {
		return branch_code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
