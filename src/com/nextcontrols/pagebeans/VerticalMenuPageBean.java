package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.nextcontrols.bureaudao.UserActivityDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.UserAudit;

@ManagedBean(name="verticalmenu")
@SessionScoped
public class VerticalMenuPageBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int linkClicked=2;
	
	public  String actionWebLinksPage (){
		setLinkClicked(1);
		return "WebLinksPage?faces-redirect=true";}
	
	
///////////////////////////   changes start //////////////////////
	
	public  String actionHistoricalAlarmsPage  (){
		setLinkClicked(3);
		return "HistoricActivityPage?faces-redirect=true";}
	
	
	
	///////////////////////////// changes end //////////////////
	
	
	public  String actionLogoutPage (){
		setLinkClicked(4);
		return "login?faces-redirect=true";}
	
	
	
	
	public  String actionCurrentAlarmsPage (){
		FacesContext ctx= FacesContext.getCurrentInstance();
		Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
		sessionMap.remove("horizontalmenu");
		switch (linkClicked){
			case 3:
				sessionMap.remove("allcustomers");
				sessionMap.remove("allbranches");
				sessionMap.remove("branchdetails");
				sessionMap.remove("historicActivity");
				sessionMap.remove("keyholders");
				sessionMap.remove("siteAlarmAssets");
				sessionMap.remove("siteAlmassetSearchString");
				sessionMap.remove("departments");
				sessionMap.remove("alarmAssets");
				sessionMap.remove("almassetSearchString");
				sessionMap.remove("allalarmtraps");
				sessionMap.remove("deptkeyholders");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				sessionMap.remove("currentalarms");
				
				break;
			case 4:
				sessionMap.remove("globalIsolations");
				break;
			case 5:
				sessionMap.remove("users");
				sessionMap.remove("userstatistics");
				break;
			case 6:
				sessionMap.remove("eventMngmt");
				break;
			case 7:
				sessionMap.remove("originalalarms");
				sessionMap.remove("alarmreports");
				break;
			case 8:
				sessionMap.remove("contactAdmin");
				break;
			case 9:
				sessionMap.remove("lhCall");
				sessionMap.remove("lhMngmt");
				sessionMap.remove("lhKeyholders");		
				break;
			default:
				break;
			
		}
		setLinkClicked(2);
		sessionMap.remove("currentalarms");
		sessionMap.remove("globalAlmTrapWorkflow");
		return "CurrentAlarmsPage?faces-redirect=true";}
	
	public  String actionSiteInformationSetupPage (){
		FacesContext ctx= FacesContext.getCurrentInstance();
		Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
		sessionMap.remove("horizontalmenu");
		switch (linkClicked){
			case 2:
				sessionMap.remove("currentalarms");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				sessionMap.remove("globalAlmTrapWorkflow");
				break;
			case 4:
				sessionMap.remove("globalIsolations");
				break;
			case 5:
				sessionMap.remove("users");
				sessionMap.remove("userstatistics");
				break;
			case 6:
				sessionMap.remove("eventMngmt");
				break;
			case 7:
				sessionMap.remove("originalalarms");
				sessionMap.remove("alarmreports");
				break;
			case 8:
				sessionMap.remove("contactAdmin");
				break;
			case 9:
				sessionMap.remove("lhCall");
				sessionMap.remove("lhMngmt");
				sessionMap.remove("lhKeyholders");
				break;
			default:
				break;
			
		}
		setLinkClicked(3);
		return "CustomerListPage?faces-redirect=true";}
	
	public  String actionReportsPage (){
		FacesContext ctx= FacesContext.getCurrentInstance();
		Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
		sessionMap.remove("horizontalmenu");
		switch (linkClicked){
			case 2:
				sessionMap.remove("currentalarms");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				sessionMap.remove("globalAlmTrapWorkflow");
				break;
			case 3:
				sessionMap.remove("allcustomers");
				sessionMap.remove("allbranches");
				sessionMap.remove("branchdetails");
				sessionMap.remove("historicActivity");
				sessionMap.remove("keyholders");
				sessionMap.remove("siteAlarmAssets");
				sessionMap.remove("siteAlmassetSearchString");
				sessionMap.remove("departments");
				sessionMap.remove("alarmAssets");
				sessionMap.remove("almassetSearchString");
				sessionMap.remove("allalarmtraps");
				sessionMap.remove("deptkeyholders");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				break;
			case 4:
				sessionMap.remove("globalIsolations");
				break;
			case 5:
				sessionMap.remove("users");
				sessionMap.remove("userstatistics");
				break;
			case 6:
				sessionMap.remove("eventMngmt");
				break;
			case 8:
				sessionMap.remove("contactAdmin");
				break;
			case 9:
				sessionMap.remove("lhCall");
				sessionMap.remove("lhMngmt");
				sessionMap.remove("lhKeyholders");
				break;
			default:
				break;
			
		}
		setLinkClicked(7);
		return "ReportsAlarmsListPage?faces-redirect=true";
	}
	public  String actionListofUsersPage (){
		FacesContext ctx= FacesContext.getCurrentInstance();
		Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
		sessionMap.remove("horizontalmenu");
		switch (linkClicked){
			case 2:
				sessionMap.remove("currentalarms");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				sessionMap.remove("globalAlmTrapWorkflow");
				break;
			case 3:
				sessionMap.remove("allcustomers");
				sessionMap.remove("allbranches");
				sessionMap.remove("branchdetails");
				sessionMap.remove("historicActivity");
				sessionMap.remove("keyholders");
				sessionMap.remove("siteAlarmAssets");
				sessionMap.remove("siteAlmassetSearchString");
				sessionMap.remove("departments");
				sessionMap.remove("alarmAssets");
				sessionMap.remove("almassetSearchString");
				sessionMap.remove("allalarmtraps");
				sessionMap.remove("deptkeyholders");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				break;
			case 4:
				sessionMap.remove("globalIsolations");
				break;
			case 6:
				sessionMap.remove("eventMngmt");
				break;
			case 7:
				sessionMap.remove("originalalarms");
				sessionMap.remove("alarmreports");
				break;
			case 8:
				sessionMap.remove("contactAdmin");
				break;
			case 9:
				sessionMap.remove("lhCall");
				sessionMap.remove("lhMngmt");
				sessionMap.remove("lhKeyholders");
				break;
			default:
				break;
			
		}
		setLinkClicked(5);
		return "ListofUsersPage?faces-redirect=true";}
	
	public String actionContactAdminPage(){
		FacesContext ctx= FacesContext.getCurrentInstance();
		Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
		sessionMap.remove("horizontalmenu");
		switch (linkClicked){
			case 2:
				sessionMap.remove("currentalarms");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				sessionMap.remove("globalAlmTrapWorkflow");
				break;
			case 3:
				sessionMap.remove("allcustomers");
				sessionMap.remove("allbranches");
				sessionMap.remove("branchdetails");
				sessionMap.remove("historicActivity");
				sessionMap.remove("keyholders");
				sessionMap.remove("siteAlarmAssets");
				sessionMap.remove("siteAlmassetSearchString");
				sessionMap.remove("departments");
				sessionMap.remove("alarmAssets");
				sessionMap.remove("almassetSearchString");
				sessionMap.remove("allalarmtraps");
				sessionMap.remove("deptkeyholders");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				break;
			case 4:
				sessionMap.remove("globalIsolations");
				break;
			case 5:
				sessionMap.remove("users");
				sessionMap.remove("userstatistics");
				break;
			case 6:
				sessionMap.remove("eventMngmt");
				break;
			case 7:
				sessionMap.remove("originalalarms");
				sessionMap.remove("alarmreports");
				break;
			case 9:
				sessionMap.remove("lhCall");
				sessionMap.remove("lhMngmt");
				sessionMap.remove("lhKeyholders");
				break;
			default:
				break;
			
		}
		setLinkClicked(8);
		return "ContactAdminPage?faces-redirect=true";}
	
	public String actionLHCallPage(){
		FacesContext ctx= FacesContext.getCurrentInstance();
		Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
		sessionMap.remove("horizontalmenu");
		switch (linkClicked){
			case 2:
				sessionMap.remove("currentalarms");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				sessionMap.remove("globalAlmTrapWorkflow");
				break;
			case 3:
				sessionMap.remove("allcustomers");
				sessionMap.remove("allbranches");
				sessionMap.remove("branchdetails");
				sessionMap.remove("historicActivity");
				sessionMap.remove("keyholders");
				sessionMap.remove("siteAlarmAssets");
				sessionMap.remove("siteAlmassetSearchString");
				sessionMap.remove("departments");
				sessionMap.remove("alarmAssets");
				sessionMap.remove("almassetSearchString");
				sessionMap.remove("allalarmtraps");
				sessionMap.remove("deptkeyholders");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				break;
			case 4:
				sessionMap.remove("globalIsolations");
				break;
			case 5:
				sessionMap.remove("users");
				sessionMap.remove("userstatistics");
				break;
			case 6:
				sessionMap.remove("eventMngmt");
				break;
			case 7:
				sessionMap.remove("originalalarms");
				sessionMap.remove("alarmreports");
				break;
			case 8:
				sessionMap.remove("contactAdmin");
				break;
			default:
				break;
			
		}
		setLinkClicked(9);
		return "LHCallPage?faces-redirect=true";
	}
	
	public  String actionTakeCallPage (){return "TakeCallPage?faces-redirect=true";}
	public  String actionSystemConfigurationPage (){return "SystemConfigurationPage?faces-redirect=true";}
	public  String actionSystemAdministrationPage (){return "SystemAdministrationPage?faces-redirect=true";}
	
	public  String actionLogout (){
		setLinkClicked(2);
		linkClicked=2;
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		//	UserActivityDAO logoutUser=new UserActivityDAO();
		//		logoutUser.userLogout(session.getAttribute("user").toString());
		//		UserAuditDAO logoutAudit=new UserAuditDAO();
		//Calendar cal=Calendar.getInstance();
		//Date now=cal.getTime();

		//	logoutAudit.insertUserAudit(new UserAudit(Integer.parseInt(session.getAttribute("userId").toString()),new Timestamp(now.getTime()),"UserLogout",
		//			"The user has logged out",null));
		session.removeAttribute("user");
		session.invalidate();
 	    return "login?faces-redirect=true";
	}
	
	public  String actionLHLogout (){
		setLinkClicked(2);
		linkClicked=2;
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserActivityDAO logoutUser=new UserActivityDAO();
		logoutUser.userLogout(session.getAttribute("user").toString());
		UserAuditDAO logoutAudit=new UserAuditDAO();
		Calendar cal=Calendar.getInstance();
		Date now=cal.getTime();

		logoutAudit.insertUserAudit(new UserAudit(Integer.parseInt(session.getAttribute("userId").toString()),new Timestamp(now.getTime()),"UserLogout",
				"The user has logged out",null));
		session.removeAttribute("user");
		session.invalidate();
 	    return "LHLogin?faces-redirect=true";
	}
	public void setLinkClicked(int LinkClicked) {
		linkClicked = LinkClicked;
	}
	public int getLinkClicked() {
		return linkClicked;
	}
	
	public String actionActiveEventManagementPage(){
		FacesContext ctx= FacesContext.getCurrentInstance();
		Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
		sessionMap.remove("horizontalmenu");
		switch (linkClicked){
			case 2:
				sessionMap.remove("currentalarms");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				sessionMap.remove("globalAlmTrapWorkflow");
				break;
			case 3:
				sessionMap.remove("allcustomers");
				sessionMap.remove("allbranches");
				sessionMap.remove("branchdetails");
				sessionMap.remove("historicActivity");
				sessionMap.remove("keyholders");
				sessionMap.remove("siteAlarmAssets");
				sessionMap.remove("siteAlmassetSearchString");
				sessionMap.remove("departments");
				sessionMap.remove("alarmAssets");
				sessionMap.remove("almassetSearchString");
				sessionMap.remove("allalarmtraps");
				sessionMap.remove("deptkeyholders");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				break;
			case 4:
				sessionMap.remove("globalIsolations");
				break;
			case 5:
				sessionMap.remove("users");
				sessionMap.remove("userstatistics");
				break;
			case 7:
				sessionMap.remove("originalalarms");
				sessionMap.remove("alarmreports");
				break;
			case 8:
				sessionMap.remove("contactAdmin");
				break;
			case 9:
				sessionMap.remove("lhCall");
				sessionMap.remove("lhMngmt");
				sessionMap.remove("lhKeyholders");
				break;
			default:
				break;
			
		}
		setLinkClicked(6);
		return "EventManagementPage?faces-redirect=true";
	}
	
	public String actionGlobalIsolationsPage(){
		FacesContext ctx= FacesContext.getCurrentInstance();
		Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
		sessionMap.remove("horizontalmenu");
		switch (linkClicked){
			case 2:
				sessionMap.remove("currentalarms");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				sessionMap.remove("globalAlmTrapWorkflow");
				break;
			case 3:
				sessionMap.remove("allcustomers");
				sessionMap.remove("allbranches");
				sessionMap.remove("branchdetails");
				sessionMap.remove("historicActivity");
				sessionMap.remove("keyholders");
				sessionMap.remove("siteAlarmAssets");
				sessionMap.remove("siteAlmassetSearchString");
				sessionMap.remove("departments");
				sessionMap.remove("alarmAssets");
				sessionMap.remove("almassetSearchString");
				sessionMap.remove("allalarmtraps");
				sessionMap.remove("deptkeyholders");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				break;
			case 5:
				sessionMap.remove("users");
				sessionMap.remove("userstatistics");
				break;
			case 6:
				sessionMap.remove("eventMngmt");
				break;
			case 7:
				sessionMap.remove("originalalarms");
				sessionMap.remove("alarmreports");
				break;
			case 8:
				sessionMap.remove("contactAdmin");
				break;
			case 9:
				sessionMap.remove("lhCall");
				sessionMap.remove("lhMngmt");
				sessionMap.remove("lhKeyholders");
				break;
			default:
				break;
			
		}
		setLinkClicked(4);
		return "GlobalIsolationsPage?faces-redirect=true";
	}
	public String actionUserManagementPage(){
		FacesContext ctx= FacesContext.getCurrentInstance();
		Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
		sessionMap.remove("horizontalmenu");
		switch (linkClicked){
			case 2:
				sessionMap.remove("currentalarms");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				sessionMap.remove("globalAlmTrapWorkflow");
				break;
			case 3:
				sessionMap.remove("allcustomers");
				sessionMap.remove("allbranches");
				sessionMap.remove("branchdetails");
				sessionMap.remove("historicActivity");
				sessionMap.remove("keyholders");
				sessionMap.remove("siteAlarmAssets");
				sessionMap.remove("siteAlmassetSearchString");
				sessionMap.remove("departments");
				sessionMap.remove("alarmAssets");
				sessionMap.remove("almassetSearchString");
				sessionMap.remove("allalarmtraps");
				sessionMap.remove("deptkeyholders");
				sessionMap.remove("alarmTypeSearch");
				sessionMap.remove("globalalarmtraps");
				break;
			case 5:
				sessionMap.remove("users");
				sessionMap.remove("userstatistics");
				break;
			case 6:
				sessionMap.remove("eventMngmt");
				break;
			case 7:
				sessionMap.remove("originalalarms");
				sessionMap.remove("alarmreports");
				break;
			case 8:
				sessionMap.remove("contactAdmin");
				break;
			case 9:
				sessionMap.remove("lhCall");
				sessionMap.remove("lhMngmt");
				sessionMap.remove("lhKeyholders");
				break;
			case 10:
				break;
			default:
				break;
			
		}
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UsersPageBean userListPageBean = (UsersPageBean) session
				.getAttribute("users");
		String userType = (String) session.getAttribute("userType");
		if (userType.equals("WebAdmin") || userType.equals("WebReadOnly")) {
			if (userListPageBean != null) {
				session.removeAttribute("users");
				userListPageBean.intitializeCustomers();
				session.setAttribute("users", userListPageBean);
			} else {
				userListPageBean = new UsersPageBean();
				userListPageBean.intitializeCustomers();
				session.setAttribute("users", userListPageBean);
			}
			setLinkClicked(10);
			return "CustomersPage.xhtml?faces-redirect=true";
				
		}
		else{
		if (userListPageBean != null) {
			session.removeAttribute("users");
			userListPageBean.initializeUsers();
			session.setAttribute("users", userListPageBean);
		} else {
			userListPageBean = new UsersPageBean();
			userListPageBean.initializeUsers();
			session.setAttribute("users", userListPageBean);
		}
		setLinkClicked(10);
		return "UsersPage.xhtml?faces-redirect=true";
		}
	}
	
	
}
