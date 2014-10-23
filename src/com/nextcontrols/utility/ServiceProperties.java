package com.nextcontrols.utility;



	import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.swing.JOptionPane;

import com.mysql.jdbc.MySQLConnection;
@ManagedBean
@ApplicationScoped
	public class ServiceProperties {
		
		public String SERVER;
		public String USER;
		public String PASSWORD;
		public String DEBUG;
		public String HOST;
		public String FROM;
		public String TO;
		public String CONNECTIONBUREAU;
		public String CONNECTIONTUTELA;
		public String DATABASESERVERNAME;
		public String DATABASENAME;
		public String DATABASEUSERNAME;
		public String DATABASEPASSWORD;
		public String LOGPATH;
		public String ALARMSDATABASE;
		public String MYSQLDATABASE;
		//public String PHONEEXTENSIONS;
		public String CONTACTTYPES;
		public String callOptions;
		private Properties properties;
		public static ServiceProperties sProps;
		private ServiceProperties()  {
			loadProperties();
			loadCallProperties();
		}
		private ServiceProperties(boolean test) 
		{}
		public synchronized static ServiceProperties getInstance() {
			if (sProps == null) {
				sProps = new ServiceProperties();
			}
			return sProps;
		}

		
		public void loadProperties()  {
			try {
				loadProperties("/Service.properties");
			} catch (Exception e) {
				e.printStackTrace();
			}	
			}
		
		public void loadCallProperties(){
			try{
				loadCallProperties("/CallOptions.properties");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void loadProperties(String pPath) throws Exception {
			InputStream stream = ServiceProperties.class
					.getResourceAsStream(pPath);
		    properties = new Properties();
			properties.load(stream);
			SERVER = properties.getProperty("messagingservice.TextMessageBean.server");
			DEBUG = properties.getProperty("messagingservice.TextMessageBean.debug");
			USER = properties.getProperty("messagingservice.TextMessageBean.user");
			PASSWORD = properties.getProperty("messagingservice.TextMessageBean.password");
			HOST =  properties.getProperty("messagingservice.EmailBean.host");
			FROM =  properties.getProperty("messagingservice.EmailBean.from");
			TO =  properties.getProperty("messagingservice.EmailBean.to");
			CONNECTIONBUREAU = properties.getProperty("messagingservice.TextMessageBean.conBureauString");
			CONNECTIONTUTELA = properties.getProperty("messagingservice.TextMessageBean.conTutelaString");
			DATABASESERVERNAME = properties.getProperty("messagingservice.TextMessageBean.databaseServerName");;
			DATABASENAME = properties.getProperty("messagingservice.TextMessageBean.databaseName");;
			DATABASEUSERNAME = properties.getProperty("messagingservice.TextMessageBean.databaseUserName");;
			DATABASEPASSWORD = properties.getProperty("messagingservice.TextMessageBean.databasePassword");;
			LOGPATH = properties.getProperty("messagingservice.readReceiptBean.logpath");
			ALARMSDATABASE = properties.getProperty("messagingservice.AlarmMessageBean.conAlarmDBString");
			MYSQLDATABASE=properties.getProperty("messagingservice.TextMessageBean.conalarmDBString");
			//PHONEEXTENSIONS=properties.getProperty("phone.extensions");
			CONTACTTYPES=properties.getProperty("contact.types");
			stream.close();	
		}
		
		public void loadCallProperties(String path) throws Exception{
			InputStream stream = ServiceProperties.class
			.getResourceAsStream(path);
			properties = new Properties();
			properties.load(stream);
			callOptions=properties.getProperty("call.options");
			stream.close();	
		}
		

		public Properties getProperties(){
			properties.put("server", SERVER);
			properties.put("user", USER);
			properties.put("password", PASSWORD);
			properties.put("mail.debug", DEBUG);
			properties.put("mail.smtp.host", HOST);
			properties.put("from",FROM);
			properties.put("to",TO);
			properties.put("conBureauString",CONNECTIONBUREAU);
			properties.put("conTutelaString",CONNECTIONTUTELA);
			properties.put("logpath",LOGPATH);
			properties.put("databaseServerName",DATABASESERVERNAME);
			properties.put("databaseName",DATABASENAME);
			properties.put("databaseUserName",DATABASEUSERNAME);
			properties.put("databasePassword",DATABASEPASSWORD);
			return properties;
		}
		
		public Connection getDatabaseConnection() throws SQLException{
			try {
				System.out.println("jdbc:jtds:sqlserver://"+
						DATABASESERVERNAME+":1433/"+DATABASENAME+";user="+DATABASEUSERNAME+";password="+DATABASEPASSWORD);
				return DriverManager.getConnection("jdbc:jtds:sqlserver://"+
						DATABASESERVERNAME+":1433/"+DATABASENAME+";user="+DATABASEUSERNAME+";password="+DATABASEPASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException() ;
			}
		}
		
		public Connection getBureauConnection() throws SQLException{
			try {
				return DriverManager.getConnection(CONNECTIONBUREAU);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException() ;
			}
		}
		
		public Connection getTutelaConnection() throws SQLException{
			try {
				return DriverManager.getConnection(CONNECTIONTUTELA);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException() ;
			}
		}
		
		public Connection getConnectionMQSQLDB() throws SQLException{
			try {
				return DriverManager.getConnection(MYSQLDATABASE,"bureauuser","4Alarms.ETC");
//				  return DriverManager.getConnection(MYSQLDATABASE,"root","5533117");

			}catch (SQLException e) {
						e.printStackTrace();
						throw new SQLException() ;
					}
				
		}
		
		public Connection getAlarmConnectionMQSQLDB() throws SQLException{
			try {
				
//				return DriverManager.getConnection(MYSQLDATABASE,"root","root");
				  return DriverManager.getConnection(MYSQLDATABASE,"bureauuser","4Alarms.ETC");
				//System.out.println("jdbc:mysql://MySQLTest:3306/bureauv2alarms");
			 //return DriverManager.getConnection("jdbc:mysql://MySQLTest:3306/bureauv2alarms","bureauuser","4Alarms.ETC");
			}catch (SQLException e) {
						e.printStackTrace();
						throw new SQLException() ;
			}
				
		}
		
		
		/*public String getPhoneExtensions(){
			return PHONEEXTENSIONS;
		}*/
		
		public String getContactTypes(){
			return CONTACTTYPES;
		}
		
		public String getCallOptions(){
			return callOptions;
		}
		
		
		static {
			try {
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		static {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public static void main(String[] args){
			try {
				new ServiceProperties().getAlarmConnectionMQSQLDB();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
}

