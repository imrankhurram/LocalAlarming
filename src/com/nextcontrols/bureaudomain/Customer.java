package com.nextcontrols.bureaudomain;

import java.io.Serializable;
import java.util.Set;


public class Customer implements Serializable{
    
  /**
	 * 
	 */
/**************** Persistent Properties ****************/

    private int id;
    private int version;
    private String name;
    private String businesstype;
    /*********** Constructors ********************/
    
   public Customer (){}
    
    public Customer(int id,int version, String name, String businesstype){
    	this.id=id;
    	this.version=version;
    	this.name=name;
    	this.businesstype=businesstype;;
    	}
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBusinesstype() {
		return businesstype;
	}
	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}
    
    
}
