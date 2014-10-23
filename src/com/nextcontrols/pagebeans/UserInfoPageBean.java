package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import com.nextcontrols.bureaudao.LocalActiveAlarmDAO;
import com.nextcontrols.bureaudao.UserActivityDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudao.UserDAO;
import com.nextcontrols.bureaudomain.User;
import com.nextcontrols.bureaudomain.UserAudit;

@ManagedBean(name="userInfo")
@SessionScoped
public class UserInfoPageBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String mUserName;
	private String mPassword;
	private String mFirstName;
	private String mLastName;
	private String mEmail;
	private String mStatus;
	private String userType;
	private String userBureauType;
	//private String extension;
	private int usersOnlineCount;
	//private List<String> phoneExtensions=new ArrayList<String>();
	private int userId;
	private UserDAO userDB=new UserDAO();
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMMMMM yyyy hh:mm a");
	private String currentTime;
	
	private int almsPerHour;
	private int almsLastYear;
	private int actionedAlms;
	private String avgResponseTime;
	
	private static boolean wrongDetails=false;
	public UserInfoPageBean(){
		
	}

	public String getFirstName() {
		return mFirstName;
	}

	public void setFirstName(String mFirstName) {
		this.mFirstName = mFirstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String mLastName) {
		this.mLastName = mLastName;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String mStatus) {
		this.mStatus = mStatus;
	}

	public String getUserName() {
		return mUserName;
	}
	
	public void setUserName(String mUserName) {
		this.mUserName = mUserName;
	}
	
	public String getPassword() {
		return mPassword;
	}
	
	public void setPassword(String mPassword) {
		this.mPassword = mPassword;
	}
	
	public void setUserId(int userId){
		this.userId=userId;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public void setUserType(String userType){
		this.userType=userType;
	}
	
	public String getUserType(){
		return userType;
	}
	
	public void setUserBureauType(String userBureauType) {
		this.userBureauType = userBureauType;
	}

	public String getUserBureauType() {
		return userBureauType;
	}

	
	public void updateUsersActivity(ActionEvent actionEvent){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		
		Calendar calendar=Calendar.getInstance();
		Date now=calendar.getTime();
		
		UserActivityDAO updateUserActivity=new UserActivityDAO();
		this.setAlmsPerHour();
		this.setActionedAlms();
		this.setAlmsLastYear();
		this.setAvgResponseTime();
	}
	
	public void updateUsersActivity(){   //overloading
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		
		Calendar calendar=Calendar.getInstance();
		Date now=calendar.getTime();
		
		UserActivityDAO updateUserActivity=new UserActivityDAO();
		this.setAlmsPerHour();
		this.setActionedAlms();
		this.setAlmsLastYear();
		this.setAvgResponseTime();
	}

	
	/*public void setExtension(String extension) {
		this.extension = extension;
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		session.setAttribute("extension", extension);
	}

	public String getExtension() {
		return extension;
	}*/

/*	public List<String> completePhoneExtension(String query){
		List<String> extensionsList=new ArrayList<String>();
		String allExtensions=ServiceProperties.getInstance().getPhoneExtensions();
		String [] separatedExtensions=allExtensions.split(",");
		for (int i=0;i<=separatedExtensions.length-1;i++){
			if (separatedExtensions[i].indexOf(query)!=-1){
				extensionsList.add(separatedExtensions[i]);
			}
		}
		return extensionsList;
	}
*/
	public String loginAction() {
        String action = null;
        
        if ( mUserName.equalsIgnoreCase("user") && mPassword.equalsIgnoreCase("password") ){
            action = "logon_failure";
        }
        else{
        	UserRegistry.getInstance().addUser(this);
            action = "logon_success";
        }
        
        return action;
    }
	
	public String action()
	  {
	    FacesContext context = FacesContext.getCurrentInstance();
	    FacesMessage message = new FacesMessage("Clicked on User: " + getUserName());
	    context.addMessage(null, message);
	    return null;
	  }
	
	public String add()
	  {
	    return "success";
	  }
	
		
	public boolean checkUserName(){
		if (this.getUserName()==null){
			return false;
		}else{
			return true;
		}
	}
	
	public void dbInsertLogin(){
		Calendar calendar=Calendar.getInstance();
		Date now=calendar.getTime();
		UserActivityDAO userLogin=new UserActivityDAO();
		userLogin.userLogin(getUserName(), new Timestamp(now.getTime()) , new Timestamp(now.getTime()), this.getUserType());
	}
	
	public void loginAudit(){
		UserAuditDAO loginAudit=new UserAuditDAO();
		Calendar cal=Calendar.getInstance();
		Date currDate=cal.getTime();
		loginAudit.insertUserAudit(new UserAudit(this.getUserId(),new Timestamp(currDate.getTime()),"UserLogin","The user has logged in",null));
	}
	
	public String processLogin(){
		int customerId=-1;
		if ((userDB.correctPassword(this.getUserName(), encryptPassword(this.getPassword()))==true)){
		try{
			User currentUser=userDB.getSpecificUser(this.getUserName());
			this.setUserId(currentUser.getUserId());
			this.setFirstName(currentUser.getFirstName());
			this.setLastName(currentUser.getLastName());
			this.setUserType(currentUser.getUserWebType());
			this.setUserBureauType(currentUser.getUserBureauType());
			customerId=currentUser.getCustomer_id();
		}catch(Exception e){
			e.printStackTrace();
		
		}	
			//this.dbInsertLogin();
			ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
			HttpSession session = (HttpSession)ectx.getSession(false);
			session.setAttribute("user", this.getUserName()); //saving the username in the session
			session.setAttribute("userId", this.getUserId());
			session.setAttribute("name", this.getFirstName() + " " + this.getLastName());
			session.setAttribute("userType", this.getUserType());
			session.setAttribute("customerId", customerId);
			FacesContext ctx= FacesContext.getCurrentInstance();
			Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
			if(sessionMap.containsKey("currentalarms")){
				CurrentAlarmsPageBean temp = (CurrentAlarmsPageBean)sessionMap.get("currentalarms");
				temp.processAlarms();
			}
			wrongDetails=false;
			return "CurrentAlarmsPage?faces-redirect=true";
		}else{
			wrongDetails=true;
			return "login?faces-redirect=true";
		}
	 }
	
	public String processLHLogin(){
		if ((userDB.correctPassword(this.getUserName(), encryptPassword(this.getPassword()))==true)){
			User currentUser=userDB.getSpecificUser(this.getUserName());
			this.setUserId(currentUser.getUserId());
			this.setFirstName(currentUser.getFirstName());
			this.setLastName(currentUser.getLastName());
			this.setUserType(currentUser.getUserWebType());
			this.setUserBureauType(currentUser.getUserBureauType());
			this.dbInsertLogin();
			ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
			HttpSession session = (HttpSession)ectx.getSession(false);
			session.setAttribute("user", this.getUserName()); //saving the username in the session
			session.setAttribute("userId", this.getUserId());
			session.setAttribute("name", this.getFirstName() + " " + this.getLastName());
			this.updateUsersActivity();
			this.loginAudit();
			wrongDetails=false;
			if (this.getUserBureauType().equals("LHCustomer")){
				return "LHCustomerAreasPage?faces-redirect=true";
			}else{
				return "LHLogin?faces-redirect=true";
			}
		}else{
			wrongDetails=true;
			return "login?faces-redirect=true";
		}
	 }
	

	
	private String encryptPassword(String password) {
		//encrypting the password before saving it
		byte[] defaultBytes = password.getBytes();
        StringBuffer hexString = new StringBuffer();
        try{
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();
                           for (int i=0;i<messageDigest.length;i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if(hex.length()==1)
                    hexString.append('0');
                hexString.append(hex);
                }
            }catch (Exception e){e.printStackTrace();}
		password = hexString.toString();
		//System.out.println(password);
		return password;
	}
	
	public String resetPassword(){
		//String newPassword=generatePassword();
		//String message="Your password for the Remote Monitoring Bureau was reset.\nYour new password is: " + newPassword;
		//String emailAddr=userDB.getUsersEmail(this.getUserName());
		//fn_SendEmail(1, emailAddr, message, "PasswordGenerator");
		wrongDetails=true;
		return "login?faces-redirect=true";
	}
	
	private String generatePassword(){
		String strNewPass= "";
		String encryptedPass="";
	    int whatsNext;
	    String characters="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.,;[]#=-+*&^%$£";

	    for(int i=0;i<12;i++)
	    {
	    	whatsNext = (int) Math.floor(Math.random() * characters.length());
	    	strNewPass=strNewPass + characters.charAt(whatsNext);
	    }
	    encryptedPass=encryptPassword(strNewPass);
	    UserDAO userDB=new UserDAO();
	    if (userDB.passwordExists(encryptedPass)==true){
	    	strNewPass=this.generatePassword();
	    }
	    userDB.resetPassword(this.getUserName(), encryptedPass);
	   return strNewPass;
	}
	
	public void setWrongDetails(boolean WrongDetails) {
		wrongDetails = WrongDetails;
	}

	public boolean isWrongDetails() {
		return wrongDetails;
	}
	
	
	public void setActionedAlms() {
		LocalActiveAlarmDAO activeAlarmDAO = new LocalActiveAlarmDAO();
		this.actionedAlms = activeAlarmDAO.actionedAlmCount();
		//RequestContext requestContext = RequestContext.getCurrentInstance();
		//if (requestContext!=null){
			//requestContext.addPartialUpdateTarget("AlmsActioned");
		//}
	}

	public int getActionedAlms() {
		return actionedAlms;
	}

	public void setAvgResponseTime() {
		LocalActiveAlarmDAO activeAlarmDAO = new LocalActiveAlarmDAO();
		int responseTime= activeAlarmDAO.averageResponseTime();
		int hours = responseTime / 3600;
		int remainder = responseTime % 3600;
		int minutes = remainder / 60;
		int seconds = remainder % 60;
		this.avgResponseTime = ( (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds< 10 ? "0" : "") + seconds );
		
		/*RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext!=null){
			requestContext.addPartialUpdateTarget("AvgAlmsResponsetime");
		}*/
	}

	public String getAvgResponseTime() {
		return avgResponseTime;
	}

	public void setAlmsPerHour() {
		LocalActiveAlarmDAO activeAlarmDAO = new LocalActiveAlarmDAO();
		this.almsPerHour = activeAlarmDAO.receivedAlmsPerHour();
		//RequestContext requestContext = RequestContext.getCurrentInstance();
		/*if (requestContext!=null){
			requestContext.addPartialUpdateTarget("Alms");
		}*/
	}

	public int getAlmsPerHour() {
		
		return almsPerHour;
	}
	
	public void setAlmsLastYear() {
		LocalActiveAlarmDAO activeAlarmDAO = new LocalActiveAlarmDAO();
		this.almsLastYear = activeAlarmDAO.receivedAlmsLastYear();
		//RequestContext requestContext = RequestContext.getCurrentInstance();
		/*if (requestContext!=null){
			requestContext.addPartialUpdateTarget("AlmsThisYear");
		}*/
	}

	public int getAlmsLastYear() {
		return almsLastYear;
	}

/*	public void setPhoneExtensions() {
		String allExtensions=ServiceProperties.getInstance().getPhoneExtensions();
		String [] separatedExtensions=allExtensions.split(",");
		for (int i=0;i<=separatedExtensions.length-1;i++){
				phoneExtensions.add(separatedExtensions[i]);
			}
	}

	public List<String> getPhoneExtensions() {
		setPhoneExtensions();
		return phoneExtensions;
	}
*/
	public void setUsersOnlineCount() {
		UserActivityDAO activityDB = new UserActivityDAO();
		this.usersOnlineCount = activityDB.getNumberUsersOnline();
	}

	public int getUsersOnlineCount() {
		setUsersOnlineCount();
		return usersOnlineCount;
	}

	public void setCurrentTime() {
		Date now = Calendar.getInstance().getTime();
		if (now!=null){
			this.currentTime = dateFormat.format(now);
		}
	}

	public String getCurrentTime() {
		setCurrentTime();
		return currentTime;
		
	}
}
