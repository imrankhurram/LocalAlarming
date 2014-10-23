package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextcontrols.utility.ServiceProperties;

@SessionScoped
@ManagedBean(name="connection")
public class ConnectionBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbBureauConn;
	private Connection dbMYSQLConn;
	private Connection dbAlarmMYSQLConn;
	private static ConnectionBean connBean;
	
	private ConnectionBean(){}
	
	public synchronized static ConnectionBean getInstance(){
		if (connBean==null){
			connBean=new ConnectionBean();
		}
		return connBean;
	}
	
	public Connection getBureauConnection() throws SQLException{
		if (dbBureauConn==null){
			dbBureauConn=ServiceProperties.getInstance().getBureauConnection();
		}else if (dbBureauConn.isClosed()){
			dbBureauConn=ServiceProperties.getInstance().getBureauConnection();	
		}
		return dbBureauConn;
	}
	
	public Connection getMYSQLConnection() throws SQLException{
		if (dbMYSQLConn==null){
			dbMYSQLConn=ServiceProperties.getInstance().getConnectionMQSQLDB();
		}else if (dbMYSQLConn.isClosed()){
			dbMYSQLConn=ServiceProperties.getInstance().getConnectionMQSQLDB();
		}
		return dbMYSQLConn;
	}
	
	public Connection getAlarmMYSQLConnection() throws SQLException{
		if (dbAlarmMYSQLConn==null){
			dbAlarmMYSQLConn=ServiceProperties.getInstance().getAlarmConnectionMQSQLDB();
		}else if (dbAlarmMYSQLConn.isClosed()){
			dbAlarmMYSQLConn=ServiceProperties.getInstance().getAlarmConnectionMQSQLDB();
		}
		return dbAlarmMYSQLConn;
	}
	
	public void closeConnections() throws SQLException
	{
	  dbBureauConn.close();
	  dbMYSQLConn.close();
	} 
	
	public void closeMYSQLConnection() throws SQLException
	{
	  dbMYSQLConn.close();
	}
}
