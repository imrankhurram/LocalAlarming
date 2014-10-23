package com.nextcontrols.pagebeans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.LazyDataModel;

import com.nextcontrols.bureaudao.ConnectionBean;
import com.nextcontrols.bureaudao.LocalActiveAlarmDAO;
import com.nextcontrols.bureaudomain.LocalActiveAlarm;



@ManagedBean(name="currentalarms")
@SessionScoped
public class CurrentAlarmsPageBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	private  List<LocalActiveAlarm> alarms=new ArrayList<LocalActiveAlarm>();
	private int userId;
	private Timestamp ackTime;
	String problemIdentified;
	String additionalInfo;
	String problemIdentifiedAll;
	String additionalInfoAll;
	String message;
	String panelmessage;       
	String userName;
	String name;								//Logged in user full name
	String controllerType;
	String comment;
	String commentack;

	private String actionTakenAll; 
	private boolean flagPrbIAll;
	private String commentsForpbIdentified="";	
	private String commentsForAckTaken="";
	private String commentsForpbIdentifiedAll="";
	private String commentsForAckTakenAll="";
	boolean print=false;
	boolean normal=true;
	boolean allalarms=false;
	boolean rendered=false;
	boolean ack;
	boolean alldialog=false;
	boolean complete;
    boolean dialog=false;
  	boolean ackDialog=true;
	boolean printDialog=false;
	boolean noAlarms=false;
	boolean alarms2print;
	boolean val= false;
	boolean flag=false;
	private Map<String, Float> exptrColNamesWithSize = new LinkedHashMap<String, Float>();
	private List<String> exptrColumnValues = new ArrayList<String>();
	private Map<String, String> exptrHeaderContent = new LinkedHashMap<String, String>();
	private String exptrPageLogo;
	private String actionTaken="";
	private boolean flagAC;
	private boolean flagAcAll;
	private String verisaeWrkOrder;
	private String partnersName="";
	private boolean flg;
	int index=-1; // index for handling browsing through button
	int currentAlarmsSize=0; //active alarms size---- 10 of 1 out of 10 
	int numberActiveAlarms=0; //index------ 1 of 1 out of 10
	int numberOfAck=0;
	int numberOfComplete=0;
	
	LocalActiveAlarm selectedAlarm=null;
	LocalActiveAlarm toPrintAlarm=null;
	//LazyDataModel<LocalActiveAlarm> active_alarms;
	
	 private List<LocalActiveAlarm> filteredAlarms;
	 
	    public List<LocalActiveAlarm> getFilteredCars() {
	        return filteredAlarms;
	    }
	 
	    public void setFilteredCars(List<LocalActiveAlarm> filteredAlarms) {
	        this.filteredAlarms = filteredAlarms;
	    }
	
	
	
	public void redirect(ActionEvent actionevent) {
	//	System.out.println("This is Alarm id"+this.selectedAlarm.getAlarmId());
		LocalActiveAlarmDAO.getInstance().delLocalActiveAlarm(this.selectedAlarm.getAlarmId());
		alarms.remove(this.selectedAlarm);
	  //return "CurrentAlarmsPage?faces-redirect=true";
	/*	ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context.redirect(context.getRequestContextPath() + "/CurrentAlarmsPage.jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/
	}
	
	public CurrentAlarmsPageBean(){
			ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
			HttpSession session = (HttpSession)ectx.getSession(false);
			this.userName=(String)session.getAttribute("user");
			this.name=(String)session.getAttribute("name");
			try {
				Class.forName ("com.mysql.jdbc.Driver").newInstance ();
			}catch (Exception e) {e.printStackTrace();}
			processAlarms();
		//	active_alarms = new AlarmsLazyList(alarms); 
	}
				
    public void processAlarms (){
		alarms=new ArrayList<LocalActiveAlarm>();
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);			
		userId=Integer.parseInt(session.getAttribute("userId").toString());
		/*Calendar aCalendar = Calendar.getInstance();
		aCalendar.set(Calendar.DATE, 1);
		aCalendar.add(Calendar.DAY_OF_MONTH, -40);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		  String endate = format1.format(aCalendar.getTime());
		aCalendar.set(Calendar.DATE, 1);
		  String stdate = format1.format(aCalendar.getTime());*/
		Calendar aCalendar = Calendar.getInstance();
		Date secondDate = aCalendar.getTime();
		aCalendar.add(Calendar.DATE, -30);
		Date firstDate = aCalendar.getTime();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String endate = format1.format(secondDate);
		 String stdate = format1.format(firstDate );
			  
		Connection dbAlarmsConn;
		Connection dbN6Conn;
		Statement stmnt = null;
		ResultSet result = null;
		try{
			dbAlarmsConn = ConnectionBean.getInstance().getAlarmMYSQLConnection();
		    //dbConn = DriverManager.getConnection ("jdbc:mysql://mysqltest:3306/bureauv2alarms", "bureauuser", "4Alarms.ETC");
			dbN6Conn=ConnectionBean.getInstance().getBureauConnection();
			String query = "SELECT * FROM user_branches where user_id='"+userId+"'";
			 stmnt=dbN6Conn.createStatement();
			 result=stmnt.executeQuery(query);
			List<String> branchcode=new ArrayList<String>();
			while(result.next()){
        		branchcode.add(result.getString("branch_code"));
			}
			session.setAttribute("siteCodes", branchcode); 
			this.ack=false;
			this.complete=false;
	
			String branch;
			for(int j=0;j<branchcode.size();j++)			
			{
			   branch=branchcode.get(j);
			//  query="SELECT * FROM `bureauv2alarms`.`alarming_localactivealarm` where site_code='"+branch+"' AND receive_time >='"+stdate+"' AND receive_time<='"+endate+"'  ORDER BY receive_time DESC;";
   query="SELECT laa.alarm_id,laa.alarm_type,laa.receive_time,laa.site_code,laa.alarm_string,laa.controller_type,laa.controller_name,laa.site_name,laa.alarmasset_name,aasset.fixture_type FROM `bureauv2alarms`.`alarming_localactivealarm`  as laa Inner Join `bureauv2alarms`.`alarming_alarm` as alarm on laa.alarm_id = alarm.alarm_id Inner Join `bureauv2alarms`.`alarming_alarmassets` as aasset on alarm.alarmasset_id = aasset.alarmasset_id where laa.site_code='"+branch+"' AND (laa.receive_time >='"+stdate+"'  AND laa.receive_time<='"+endate+"') ORDER BY laa.receive_time DESC;";			   
//			   System.out.println(query);
			   stmnt=dbAlarmsConn.createStatement();
			   result=stmnt.executeQuery(query);
			   //String query2;
			   //Statement stmnt2;
			   //ResultSet result2;
			   LocalActiveAlarm localactivealarm;
			   while(result.next()){
				    //query2="SELECT * FROM `bureauv2alarms`.`audits_local_incident_audit` where alarm_id="+result.getLong("alarm_id")+";";
				    //stmnt2=dbAlarmsConn.createStatement();
				    //result2=stmnt2.executeQuery(query2);
				    //if(result2.next())
				//	{
				//      localactivealarm=new LocalActiveAlarm(result.getLong("alarm_id"),result.getString("alarm_type"),result.getTimestamp("receive_time"),result.getString("site_code"),result.getString("alarm_string"),
				//	  result.getString("controller_type"),result.getString("alarmasset_name"),false,true,result.getString("site_name"));
					//  localactivealarm.setAckTime(result2.getTimestamp("problem_identified_timestamp"));
					//  this.complete = true;
					//  this.numberOfComplete++;
					//}
					//else 
					//{
					  localactivealarm=new LocalActiveAlarm(result.getLong("alarm_id"),result.getString("alarm_type"),result.getTimestamp("receive_time"),result.getString("site_code"),result.getString("alarm_string"),
								  result.getString("controller_type"),result.getString("alarmasset_name"),result.getString("fixture_type"),true,false,result.getString("site_name"));
					
				//	  this.ack=true;
				//	  this.numberOfAck++;
				//	}
				//	result2.getStatement().close();
					alarms.add(localactivealarm);
			   }
			   result.getStatement().close();
			//System.out.println(userId + "     " + branch + "      " + branchcode);  
			}//for
			  if(alarms.size()!=0)
		      {
				this.selectedAlarm=(LocalActiveAlarm)alarms.get(0);
		      }
			  dbN6Conn.close();
			  dbAlarmsConn.close();			
			}
			catch (Exception e){
				e.printStackTrace();
			}finally{
				try {
					result.close();
					stmnt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stmnt=null;
				result=null;
				dbAlarmsConn=null;
				dbN6Conn=null;
			}	
	}

	public void moveforwardAlarms (){
		if(index+1<alarms.size())
		{
			index++;
			++numberActiveAlarms;
			this.selectedAlarm=(LocalActiveAlarm)alarms.get(index);
		}		
	}
	
	public void movebackwordAlarms (){
		if(index-1>-1)
		{
			index--;
			--numberActiveAlarms;
			this.selectedAlarm=(LocalActiveAlarm)alarms.get(index);
		}		
	}

	public String processAcknowledge()
	{
		//System.out.print("processAcknowledge()");
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		String user=session.getAttribute("user").toString();
		Statement stmnt;
		String query;
		Connection dbAlarmsConn;
			try{
				dbAlarmsConn = ConnectionBean.getInstance().getAlarmMYSQLConnection();
				//dbConn = DriverManager.getConnection ("jdbc:mysql://mysqltest:3306/bureauv2alarms", "bureauuser", "4Alarms.ETC");		
				Calendar calendar = Calendar.getInstance();
				java.sql.Timestamp ackTime = new java.sql.Timestamp(calendar.getTime().getTime());
				this.selectedAlarm.setAckTime(ackTime);
			    query="insert into `bureauv2alarms`.`audits_local_incident_audit`(alarm_id, problem_identified_by, problem_identified_timestamp) values('"+this.selectedAlarm.getAlarmId()+"','"+user+"','"+ackTime+"');";
			 	stmnt=dbAlarmsConn.createStatement();
		        stmnt.executeUpdate(query);
		      	this.selectedAlarm.setAck(false);
			    this.selectedAlarm.setComplete(true);
			    this.numberOfAck--;
				this.numberOfComplete++;
			    this.complete=true;
			   	this.ack=false;
		   
		        LocalActiveAlarm aa;
		        for(int i=0;i<alarms.size();i++)
				 {
				  	 aa=(LocalActiveAlarm)this.alarms.get(i);
				   	 if(aa.isAck()) //every alarms available is for complete
				   	 {	
				   		 this.ack=true;
				   		 break;
				   	 }			
				 }
		         dbAlarmsConn.close();	
				}catch (Exception e){
					e.printStackTrace();
				}
	
				this.additionalInfo="";
				this.problemIdentified="";
				this.additionalInfoAll="";
				this.problemIdentifiedAll="";
		return "CurrentAlarmsPage?faces-redirect=true";
	}
	
	public String processAcknowledgeAllAlarms(){
		//System.out.print("processAcknowledge()");
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		String user=session.getAttribute("user").toString();
		Statement stmnt;
		String query;
		Connection dbAlarmsConn;
			try{
				dbAlarmsConn = ConnectionBean.getInstance().getAlarmMYSQLConnection();
				//dbConn = DriverManager.getConnection ("jdbc:mysql://mysqltest:3306/bureauv2alarms", "bureauuser", "4Alarms.ETC");		
				LocalActiveAlarm aa;	
				     for(int i=0;i<alarms.size();i++)
					 {
				       	 aa=(LocalActiveAlarm)this.alarms.get(i);
				       	 if(aa.isAck()) 
					     { 
				        	 Calendar calendar = Calendar.getInstance();
				        	 java.sql.Timestamp ackTime = new java.sql.Timestamp(calendar.getTime().getTime());
				           	 aa.setAckTime(ackTime);
				        	 query="insert into `bureauv2alarms`.`audits_local_incident_audit`(alarm_id, problem_identified_by, problem_identified_timestamp) values('"+aa.getAlarmId()+"','"+user+"','"+ackTime+"');";
					 		 stmnt=dbAlarmsConn.createStatement();
				             stmnt.executeUpdate(query);
				             aa.setAck(false);
					         aa.setComplete(true);
					         this.numberOfAck--;
					         this.numberOfComplete++;
					     }// if	alarm to b ack	
					 }//for	
				this.ack=false;
				this.complete=true;
				this.allalarms=false;
				dbAlarmsConn.close();			
			}
			catch (Exception e){e.printStackTrace();}	
			this.additionalInfo="";
			this.problemIdentified="";
			this.additionalInfoAll ="";
			this.problemIdentifiedAll	="";
		return "CurrentAlarmsPage?faces-redirect=true";
	}
	
	public String processComplete()
	{
		System.out.print("processComplete(): ");
	    int auditid=0;
    	Statement stmnt;
    	String query;
    	Connection dbAlarmsConn;
		try{
			dbAlarmsConn = ConnectionBean.getInstance().getAlarmMYSQLConnection();
			//dbConn = DriverManager.getConnection ("jdbc:mysql://mysqltest:3306/bureauv2alarms", "bureauuser", "4Alarms.ETC");	
			query="select audit_id from `bureauv2alarms`.`audits_local_incident_audit` where alarm_id='"+this.selectedAlarm.getAlarmId()+"';";
			stmnt	=dbAlarmsConn.createStatement();
		    ResultSet results=stmnt.executeQuery(query);
            if(results.next()){
		    	auditid=results.getInt("audit_id");
            }
            if(this.problemIdentified.equalsIgnoreCase("Other")) {
            	this.problemIdentified=this.commentsForpbIdentified;
            }
            if(this.actionTaken.equalsIgnoreCase("Other")) {
            	this.actionTaken= this.commentsForAckTaken;
            }
			query="update `bureauv2alarms`.`audits_local_incident_audit` Set problem_identified_detail='"+this.problemIdentified+"',incident_report= '"+this.additionalInfo+"', partnersName='"+this.partnersName+"', action_taken='"+this.actionTaken+"'," +
			"VerisaeNumber='"+this.verisaeWrkOrder+"' where audit_id='"+auditid+"';";
			stmnt=dbAlarmsConn.createStatement();
            stmnt.executeUpdate(query);
            query="DELETE FROM `bureauv2alarms`.`alarming_localactivealarm` WHERE alarm_id='"+this.selectedAlarm.getAlarmId()+"';";
            stmnt=dbAlarmsConn.createStatement();
            stmnt.executeUpdate(query);
            this.comment=this.additionalInfo+"\n"+this.problemIdentified;
            this.selectedAlarm.setComplete(false);
            this.toPrintAlarm=this.selectedAlarm;
            this.alarms.remove(this.selectedAlarm);
            this.dialog=false;
            this.numberOfComplete--;
            this.additionalInfo="";
			this.problemIdentified="";
			this.additionalInfoAll ="";
			this.problemIdentifiedAll	="";
			this.actionTaken="";
//			this.partnersName="";
//			this.verisaeWrkOrder="";
			this.commentsForAckTaken="";
			this.commentsForpbIdentified="";
			this.flagAC=false;
			this.flag=false;
			
			
			dbAlarmsConn.close();	
	}catch (Exception e){e.printStackTrace();}
	return "CurrentAlarmsPage?faces-redirect=true";
	}

	
	public String processCompletePrint()
	{
		System.out.print("processCompletePrint(): ");
		int auditid=0;
    	Statement stmnt;
    	String query;
    	Connection dbAlarmsConn;
				try{
					dbAlarmsConn = ConnectionBean.getInstance().getAlarmMYSQLConnection();
				    //dbConn = DriverManager.getConnection ("jdbc:mysql://mysqltest:3306/bureauv2alarms", "bureauuser", "4Alarms.ETC");	
					query="select audit_id from `bureauv2alarms`.`audits_local_incident_audit` where alarm_id='"+this.selectedAlarm.getAlarmId()+"';";
						stmnt	=dbAlarmsConn.createStatement();
		    			ResultSet results=stmnt.executeQuery(query);
                       	if(results.next())
		    	    		auditid=results.getInt("audit_id");
					                       	       			
                        if(this.problemIdentified.equalsIgnoreCase("Other")) {
                        	this.problemIdentified=this.commentsForpbIdentified;
                        }
                        if(this.actionTaken.equalsIgnoreCase("Other")) {
                        	this.actionTaken= this.commentsForAckTaken;
                        }
            			query="update `bureauv2alarms`.`audits_local_incident_audit` Set problem_identified_detail='"+this.problemIdentified+"',incident_report= '"+this.additionalInfo+"', partnersName='"+this.partnersName+"', action_taken='"+this.actionTaken+"'," +
            			"VerisaeNumber='"+this.verisaeWrkOrder+"' where audit_id='"+auditid+"';";                       		
            			    stmnt=dbAlarmsConn.createStatement();
                       		stmnt.executeUpdate(query);
	     
                       		query="DELETE FROM `bureauv2alarms`.`alarming_localactivealarm` WHERE alarm_id='"+this.selectedAlarm.getAlarmId()+"';";
	                   		stmnt=dbAlarmsConn.createStatement();
                       		stmnt.executeUpdate(query);
                       		this.toPrintAlarm=selectedAlarm;
                       		//System.out.println(this.additionalInfo);
                       		this.comment=this.problemIdentified;
                       		this.commentack=this.actionTaken;
                       	//	System.out.println(this.comment + "\n" + this.commentack);
                       		this.selectedAlarm.setComplete(false);
                       		this.alarms.remove(this.selectedAlarm);
                       		this.ackDialog=false;
                       		this.printDialog=true;
                       		this.dialog=true;
                       		this.numberOfComplete--;
                       		this.additionalInfo="";
                			this.problemIdentified="";
                			this.additionalInfoAll ="";
                			this.problemIdentifiedAll	="";
                			this.actionTaken="";
                			this.commentsForAckTaken="";
                			this.commentsForpbIdentified="";
                			this.flagAC=false;
                			this.flag=false;
                			//this.partnersName="";
                			
                			
                			dbAlarmsConn.close();	
				}catch (Exception e){e.printStackTrace();}
		return "CompleteAndPrintPage?faces-redirect=true";	
	}
	
	public String processCompleteAllAlarms(){
		//System.out.print("processCompleteAllAlarms(): "+this.problemIdentifiedAll+this.additionalInfoAll);
    	Statement stmnt;
    	String query;
    	List<LocalActiveAlarm> removeListAlarm = new ArrayList<LocalActiveAlarm>();
    	Connection dbAlarmsConn;
    	try{
			dbAlarmsConn = ConnectionBean.getInstance().getAlarmMYSQLConnection();
			//dbConn = DriverManager.getConnection ("jdbc:mysql://mysqltest:3306/bureauv2alarms", "bureauuser", "4Alarms.ETC");	
			LocalActiveAlarm tempLAA;	
			for(int i=0;i<alarms.size();i++)
			{
				tempLAA=(LocalActiveAlarm)this.alarms.get(i);
			   	 if(tempLAA.isComplete())
	             { 
			   		 if(this.problemIdentifiedAll.equalsIgnoreCase("other")) {
			   			 this.problemIdentifiedAll=this.commentsForpbIdentifiedAll;
			   		 }
			   		 if(this.actionTakenAll.equalsIgnoreCase("other")) {
			   			 this.actionTakenAll= this.commentsForAckTakenAll;
			   		 }
			   		 //System.out.println(tempLAA.getAlarmId());
               		 query="update `bureauv2alarms`.`audits_local_incident_audit` Set problem_identified_detail='"+this.problemIdentifiedAll+"',incident_report= '"+this.additionalInfoAll+"', action_taken='"+this.actionTakenAll+"' where alarm_id="+tempLAA.getAlarmId()+";";
                	 stmnt=dbAlarmsConn.createStatement();
                	 stmnt.executeUpdate(query);
                	 query="DELETE FROM `bureauv2alarms`.`alarming_localactivealarm` WHERE alarm_id='"+tempLAA.getAlarmId()+"';";
                	 stmnt=dbAlarmsConn.createStatement();
                	 stmnt.executeUpdate(query);
                	 removeListAlarm.add(tempLAA);
                	 this.numberOfComplete--;
                 }
			}
			this.alarms.removeAll(removeListAlarm);
			this.selectedAlarm=null;
			this.complete=false;
		 	this.allalarms=false;
			this.alldialog=false;
			this.additionalInfo="";
			this.problemIdentified="";
			this.additionalInfoAll ="";
			this.problemIdentifiedAll	="";
			dbAlarmsConn.close();	
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	return "CurrentAlarmsPage?faces-redirect=true";
	}
	
	public void handleClose(CloseEvent event){
		this.dialog=false;
		this.alldialog=false;
	}

	public String getMessage() {
		if((this.alarms.size()==0)&&(this.print))
			message="Last Alarm/No more Alarms to handle"; 
		else 
			message="No Alarms to handle"; 
		return message;
	}
	public void setMessage(String message) {this.message = message;}
		
	public boolean isNoAlarms() {
		if(this.alarms.size()==0)
		{
			noAlarms=true;
		}
		else{
		    noAlarms=false;
		}
		return noAlarms;
	}
	public void setNoAlarms(boolean noAlarms) {this.noAlarms = noAlarms;}
	
		
	public boolean isAlarms2print() {
		if(print==false)
			alarms2print=false;
		else{
			if(this.alarms.size()==0)
				alarms2print=false;
			else
				alarms2print=true;
		}
		return alarms2print;
	}
	public void setAlarms2print(boolean alarms2print) {this.alarms2print = alarms2print;}

	public boolean isNormal() {
		if(this.alarms.size()==0)
		{
			this.normal=false;
			if(!this.print)System.out.println("NO Alarms available");
		}
		return normal;
	}
	public void setNormal(boolean normal) {this.normal = normal;}

	public String getComment() {
		//System.out.println(this.comment);
		return comment;}
	public void setComment(String comment) {this.comment = comment;}
	
	public LocalActiveAlarm getSelectedAlarm() {return selectedAlarm;}
	public void setSelectedAlarm(LocalActiveAlarm SelectedAlarm) {this.selectedAlarm = SelectedAlarm;}
		
	public String getControllerType() {return controllerType;}
	public void setControllerType(String controllerType) {this.controllerType = controllerType;}
	
	public boolean isPrint() {return print;}
	public void setPrint(boolean print) {this.print = print;}
	
	public String getUserName() {return userName;}
	public void setUserName(String userName) {this.userName = userName;}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	public boolean isAckDialog() {return ackDialog;}
	public void setAckDialog(boolean ackDialog) {this.ackDialog = ackDialog;}
	
	public boolean isPrintDialog() {return printDialog;}
	public void setPrintDialog(boolean printDialog) {this.printDialog = printDialog;}
	
	public String getPanelmessage() {return panelmessage;}
	public void setPanelmessage(String panelmessage) {this.panelmessage = panelmessage;}
	
	public String getAckTime() {
		SimpleDateFormat ackFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String acktime=ackFormat.format(ackTime);
		return acktime;
	}
	public void setAckTime(Timestamp ackTime) {this.ackTime = ackTime;}
	
	public LocalActiveAlarm getToPrintAlarm() {return toPrintAlarm;}
	public void setToPrintAlarm(LocalActiveAlarm toPrintAlarm) {this.toPrintAlarm = toPrintAlarm;}
	
	public boolean isDialog() {return dialog;}
	public void setDialog(boolean dialog) {this.dialog = dialog;}
	
	public boolean isAlldialog() {return alldialog;}
	public void setAlldialog(boolean alldialog) {this.alldialog = alldialog;}
	
	public boolean isAck() {return ack;}
	public void setAck(boolean ack) {this.ack = ack;}

	public boolean isComplete() {return complete;}
	public void setComplete(boolean complete) {this.complete = complete;}
	
	public int getNumberOfAck() {return numberOfAck;}
	public void setNumberOfAck(int numberOfAck) {this.numberOfAck = numberOfAck;}
	
	public int getNumberOfComplete() {return numberOfComplete;}
	public void setNumberOfComplete(int numberOfComplete) {this.numberOfComplete = numberOfComplete;}
	
	public void checkIfShowCommentsBoxforAc(ActionEvent event) {
		if(this.actionTaken.equalsIgnoreCase("Other")) {
			setFlagAC(true);
		}
		else {
			setFlagAC(false);
		}
	}
	public void checkIfShowCommentsBoxforAcAll(ActionEvent event) {
		if (this.actionTakenAll.equalsIgnoreCase("other")) {
			setFlagAcAll(true);
		}
		else {
			setFlagAcAll(false);
		}
	}
	public void setFlagAC(boolean flagAC ) {
		this.flagAC = flagAC;
	}
	public boolean isFlagAC() {
		return flagAC;
	}
	public  List<LocalActiveAlarm> getAlarms (){
		if(this.alarms.size()>0) {
			setVal(true);
		}
		else {
			setVal(false);
		}
		return alarms;
		}
	public void setAlarms(List <LocalActiveAlarm> pAlarms) {alarms = pAlarms;}
	
	public int getIndex() {	return index;}
	public void setIndex(int index) {this.index = index;}
	
	public int getCurrentAlarmsSize() {return currentAlarmsSize;}
	public void setCurrentAlarmsSize(int currentAlarmsSize) {this.currentAlarmsSize = currentAlarmsSize;}
	
	public int getNumberActiveAlarms() {return numberActiveAlarms;}
	public void setNumberActiveAlarms(int numberActiveAlarms) {this.numberActiveAlarms = numberActiveAlarms;}
	
	public boolean isAllalarms() {return allalarms;}
	public void setAllalarms(boolean allalarms) {this.allalarms = allalarms;}
	
	public String getProblemIdentified() { System.out.println(this.problemIdentified);return problemIdentified;}
	public void setProblemIdentified(String problemIdentified) {this.problemIdentified = problemIdentified;}
	
	public String getAdditionalInfo() {
		//System.out.println("in get additional info " + this.additionalInfo);
		return additionalInfo;}
	public void setAdditionalInfo(String additionalInfo) {this.additionalInfo = additionalInfo;
	}
	
	public String getProblemIdentifiedAll() {return problemIdentifiedAll;}
	public void setProblemIdentifiedAll(String problemIdentifiedAll) {this.problemIdentifiedAll = problemIdentifiedAll;}

	public String getAdditionalInfoAll() {return additionalInfoAll;}
	public void setAdditionalInfoAll(String additionalInfoAll) {this.additionalInfoAll = additionalInfoAll;}
    
	public void setActionTaken(String actionTaken) {
		this.actionTaken= actionTaken;
	}
	public String getActionTaken() {
		System.out.println(this.actionTaken);
		return actionTaken;
	}
	public boolean isRendered() {return rendered;}
	public void setRendered(boolean rendered) {
		this.rendered = rendered;
		if(rendered){this.panelmessage="Acknowledge Alarm";}
		else{this.panelmessage="Current Alarms";}
	}
	public void checkIfShowCommentsBox(ActionEvent event) {
		if((this.problemIdentified.equalsIgnoreCase("other"))){
			setFlag(true);
		//	System.out.println(flag +"     " + this.problemIdentified);
		}
		else {
			setFlag(false);
			//System.out.println(flag +"     " + this.problemIdentified);


		}
	}
	public void setFlag(boolean flag) {
		this.flag=flag;
	}
	public boolean getFlag() {
		return flag;
	}
	public void setVerisaeWrkOrder(String VerisaeWrkOrder) {
		this.verisaeWrkOrder=VerisaeWrkOrder;
	}
	public String getVerisaeWrkOrder() {
		return verisaeWrkOrder;
	}
	public String pageRedirect() {
		return "completeAll?faces-redirect=true";
	}
	public String backToCurrentAlarmsPage() {
		System.out.println("in here");
		return "CurrentAlarmsPage?faces-redirect=true";

		
	}
	public String redirectToCompleteAlmsPage() {
		return "completeAlms?faces-redirect=true";
	}
	public void setVal(boolean val) {
		this.val = val;
	}
	public boolean isVal() {
		return val;
	}
	public void setExptrColNamesWithSize () {
		this.exptrColNamesWithSize.put("System Number", 25f);
		this.exptrColNamesWithSize.put("Time and Date", 30f);
		this.exptrColNamesWithSize.put("Alarm Details", 30f);
	}
	public Map<String, Float> getExptrColNamesWithSize() {
		this.exptrColNamesWithSize.clear();
		setExptrColNamesWithSize();
		return exptrColNamesWithSize;
	}
	public void setExptrColumnValues() {
		for(int i=0; i<this.alarms.size(); i++) {
			this.exptrColumnValues.add(this.alarms.get(i).getControllerName());
			this.exptrColumnValues.add(this.alarms.get(i).getRecvTime());
			this.exptrColumnValues.add(this.alarms.get(i).getAlarmString());
		}
	}
	public List<String> getExptrColumnValues() {
		this.exptrColumnValues.removeAll(exptrColumnValues);
		setExptrColumnValues();
		return exptrColumnValues;
	}
	public void setExptrHeaderContent() {
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		this.exptrHeaderContent.put("Created By: ", (String) session.getAttribute("name"));
		this.exptrHeaderContent.put("Date: ", dateFormat.format(cal.getTime()));
		this.exptrHeaderContent.put("Time: ", timeFormat.format(cal.getTime()));
	}
	public Map<String, String> getExptrHeaderContent() {
		this.exptrHeaderContent.clear();
		setExptrHeaderContent();
		return exptrHeaderContent;
	}
	public void setExptrPageLogo() {
		ServletContext context =(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		this.exptrPageLogo = context.getRealPath("") + File.separator + "images" + File.separator + "next energyemail.jpg";
	}
	public String getExptrPageLogo() {
		setExptrPageLogo();
		return exptrPageLogo;
	}
	public void checkCurrentAlarmsSize(ActionEvent event) {
	
		if(this.alarms.size()==0) {
			RequestContext context = RequestContext.getCurrentInstance();
			FacesMessage message = new FacesMessage();
			message.setDetail("No Alarms To Generate PDF");
			message.setSummary("No Alarms To Generate PDF");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			//context.addPartialUpdateTarget("frmcurrentalarmsPage:Msg");
		}
	}
	public void setPDFExporter(ActionEvent event) {	
		DataExporterBean exporterBean = new DataExporterBean();
		exporterBean.setTabColumnsWithSize(getExptrColNamesWithSize());
		exporterBean.setColumnValues(getExptrColumnValues());
		exporterBean.setHeaderContent(getExptrHeaderContent());
		exporterBean.setFileName("currentAlarms");
		exporterBean.setPageTitle("Current Alarms List");
		exporterBean.setLogo(getExptrPageLogo());
		
	}
	public void setPartnersName(String partnersName) {
		this.partnersName=partnersName;
	}
	public String getPartnersName() {
		if(this.partnersName.equals("")) {
			setFlg(false);
		}
		else {
			setFlg(true);
		}
		return partnersName;
	}
	public void setFlg(boolean flg) {
		this.flg=flg;
	}
	public boolean isFlg() {
		return flg;
	}
	public void setCommentsForpbIdentified(String commentsForpbIdentified) {
		this.commentsForpbIdentified=commentsForpbIdentified;
	}
	public String getCommentsForpbIdentified() {
		return commentsForpbIdentified;
	}
	public void setCommentsForAckTaken(String commentsForAckTaken) {
		this.commentsForAckTaken=commentsForAckTaken;
	}
	public String getCommentsForAckTaken() {
		return commentsForAckTaken;
	}
	public String getCommentack() {
		return commentack;
	}
	public void setCommentack(String commentack) {
		this.commentack=commentack;
	}
	public void setFlagPrbIAll(boolean flagPrbIAll) {
		this.flagPrbIAll=flagPrbIAll;
	}
	public boolean isFlagPrbIAll() {
		return flagPrbIAll;
	}
	public void checkIfShowCommentsBoxPrbAll(ActionEvent event) {
		if(this.problemIdentifiedAll.equalsIgnoreCase("other")) {
			setFlagPrbIAll(true);
		}
		else {
			setFlagPrbIAll(false);
		}
	}
	public void setCommentsForpbIdentifiedAll(String commentsForpbIdentifiedAll) {
		this.commentsForpbIdentifiedAll=commentsForpbIdentifiedAll;
	}
	public String getCommentsForpbIdentifiedAll() {
		return commentsForpbIdentifiedAll;
	}
	public void setActionTakenAll(String actionTakenAll) {
		this.actionTakenAll=actionTakenAll;
	}
	public String getActionTakenAll() {
		return actionTakenAll;
	}
	public void setCommentsForAckTakenAll(String commentsForAckTakenAll) {
		this.commentsForAckTakenAll= commentsForAckTakenAll;
	}
	public String getCommentsForAckTakenAll() {
		return commentsForAckTakenAll;
	}
	public void setFlagAcAll(boolean flagAcAll) {
		this.flagAcAll=flagAcAll;
	}
	public boolean isFlagAcAll() {
		return flagAcAll;
	}
	public void checkIfFieldIsEmpty(ActionEvent event) {
		System.out.println("here");
		if(this.partnersName.equalsIgnoreCase("")) {
			System.out.println(partnersName.equalsIgnoreCase(""));
			RequestContext context = RequestContext.getCurrentInstance();
			FacesMessage message = new FacesMessage();
			message.setDetail("Please provide a partners name");
			message.setSummary("Please provide a partners name");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
		    FacesContext.getCurrentInstance().addMessage(null, message);
		    //context.addPartialUpdateTarget("completeAlmsFrm:valMsg");
		}
	}
}
