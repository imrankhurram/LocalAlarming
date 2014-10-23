package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudao.UserDAO;
import com.nextcontrols.bureaudomain.Customer;
import com.nextcontrols.bureaudomain.User;



@ManagedBean(name = "users")
@SessionScoped
public class UsersPageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<User> usersList;
	private List<User> filteredUsersList;
	private List<Customer> customersList;
	private int selectedCustomerId;
	private String selectedCustomerName;
	private String currentDateTime;
	private User newUser;
	private List<Customer> ownCustomersList;
	private List<Customer> ownSelectedCustomersList;
	private User selectedUser;
//	private List<User> cars;
	

//	public List<User> getCars() {
//		cars=new ArrayList<User>();
//		User user=new User();
//		user.setDivisionName("dddd");
//		cars.add(user);
//		return cars;
//	}
//
//	public void setCars(List<User> cars) {
//		this.cars = cars;
//	}

	public UsersPageBean() {
		usersList = new ArrayList<User>();

	}

	public void intitializeCustomers() {
		UserDAO userDB=new UserDAO();
		customersList = userDB.getCustomerList();
	}

	public String viewCustomerUsers(int customerId, String customerName) {
		selectedCustomerId = customerId;
		this.selectedCustomerName = customerName;
		this.selectedCustomerName = this.selectedCustomerName.replace(" ", "_");
		initializeUsers();
		return "UsersPage.xhtml?faces-redirect=true";
	}

	public void initializeUsers() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		String userType = (String) session.getAttribute("userType");
		UserDAO userDB=new UserDAO();
		List<Integer> customerIds = new ArrayList<Integer>();
		if (userType.equals("WebAdmin") || userType.equals("WebReadOnly")) {
			// System.out.println("customerid: "+this.selectedCustomerId);
			customerIds.add(this.selectedCustomerId);
		} else {
			customerIds =userDB.getCustomerIds(
					(int) session.getAttribute("userId"));
			if (customerIds.isEmpty()) {
				customerIds.add((int) session.getAttribute("customerId"));
			}
		}
		this.usersList = userDB.getUserList(customerIds);
		// System.out.println("user size: " + this.usersList.size());
		// Map<Integer, String> customerIdsNdNames = UserDAO.getInstance()
		// .getDivisionNames(customerIds);
		Date today = new Date();
		// int i = 0;
		for (User user : this.usersList) {
			// user.setDivisionName(customerIdsNdNames.get(user.getCustomer_id()));
			long pinDiff = user.getPincodeExpires().getTime() - today.getTime();
			user.setPinCodeTimeout((pinDiff / (1000 * 60 * 60 * 24)));
			// if (i < 3) {
			// System.out.println("expiry date:" + user.getPincodeExpires());
			// System.out.println("expiry time utils:"
			// + TimeUnit.MILLISECONDS.toDays(pinDiff));
			// System.out.println("expiry pincode: "
			// + user.getPincodeExpires().getTime());
			// System.out.println("timout: " + user.getPinCodeTimeout());
			// }

			long passDiff = user.getPasswordExpires().getTime()
					- today.getTime();
			user.setPasswordTimeout((passDiff / (1000 * 60 * 60 * 24)));
			// i++;
		}
	}

	public void saveChanges() {
		FacesMessage message = null;
		Calendar pincodeDate = Calendar.getInstance();

		Calendar passwordDate = Calendar.getInstance();
		for (User user : this.usersList) {
			pincodeDate.setTime(new Date());
			passwordDate.setTime(new Date());
			if (user.getPinCodeTimeout().intValue() > 365) {
				// System.out.println("entered");
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Pin code timeout value should be less than 365!",
						"Pin code timeout value should be less than 365!");
				FacesContext.getCurrentInstance().addMessage(null, message);
				RequestContext.getCurrentInstance().update("userMsg");
				return;
			}
			if (user.getPasswordTimeout().intValue() > 365) {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Password timeout value should be less than 365!",
						"Password timeout value should be less than 365!");
				FacesContext.getCurrentInstance().addMessage(null, message);
				RequestContext.getCurrentInstance().update("userMsg");
				return;
			}
			if (user.getPinCodeTimeout().intValue() >= 0) {
				// System.out.println("timout:"
				// + user.getPinCodeTimeout().intValue());
				pincodeDate.add(Calendar.DATE, user.getPinCodeTimeout()
						.intValue());
				// System.out.println("date expiry:" + pincodeDate.getTime());
				user.setPincodeExpires(pincodeDate.getTime());
			}

			if (user.getPasswordTimeout().intValue() >= 0) {
				passwordDate.add(Calendar.DATE, user.getPasswordTimeout()
						.intValue());
				// System.out.println("pass expiry0: " +
				// passwordDate.getTime());
				user.setPasswordExpires(passwordDate.getTime());
				// System.out.println("pass expiry: " +
				// user.getPasswordExpires());
			}
		}
		UserDAO userDB=new UserDAO();
		userDB.modifyUsers(this.usersList);
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserAuditDAO.getInstance().insertUserAdminAudit(String.valueOf(session.getAttribute("userId")), "Users Modified! User list batch update", null, -1);
				
//				new UserAudit(Integer.parseInt(session.getAttribute("userId")
//						.toString()), new Timestamp(Calendar.getInstance()
//						.getTime().getTime()), "UsersModified",
//						"User list batch update", null));

	}

	public String cancelChanges() {
		initializeUsers();
		return "UsersPage.xhtml?faces-redirect=true";
	}

	public void initializeNewUser() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		this.newUser = new User();
		this.ownSelectedCustomersList = new ArrayList<Customer>();
		String userType = (String) session.getAttribute("userType");
		UserDAO userDB=new UserDAO();
		if (userType.equals("WebAdmin") || userType.equals("WebReadOnly")) {
			// System.out.println("entered all");
			this.ownCustomersList = userDB.getCustomerList();
		} else {
			this.ownCustomersList = userDB.getCustomerOfUser(
					(int) session.getAttribute("userId"));
		}

	}

	public void saveNewUser() {
		// for (Customer cust : ownSelectedCustomersList)
		// System.out.println("id: " + cust.getId());
		FacesMessage message = null;
		Calendar pincodeDate = Calendar.getInstance();

		Calendar passwordDate = Calendar.getInstance();
		pincodeDate.setTime(new Date());
		passwordDate.setTime(new Date());
		if (this.newUser.getPinCodeTimeout().intValue() > 365) {
			// System.out.println("entered");
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Pin code timeout value should be less than 365!",
					"Pin code timeout value should be less than 365!");
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().update("userMsg");
			return;
		}
		if (this.newUser.getPasswordTimeout().intValue() > 365) {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Password timeout value should be less than 365!",
					"Password timeout value should be less than 365!");
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().update("userMsg");
			return;
		}
		if (this.newUser.getPinCodeTimeout().intValue() >= 0) {
			pincodeDate.add(Calendar.DATE, this.newUser.getPinCodeTimeout()
					.intValue());
			this.newUser.setPincodeExpires(pincodeDate.getTime());
		}

		if (this.newUser.getPasswordTimeout().intValue() >= 0) {
			passwordDate.add(Calendar.DATE, this.newUser.getPasswordTimeout()
					.intValue());
			this.newUser.setPasswordExpires(passwordDate.getTime());
		}
		Integer temp = 0;
		Integer enabled = 1;
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);

		// System.out.println("this.newUser.getUserWebType(): "
		// + this.newUser.getUserWebType());
		UserDAO userDB=new UserDAO();
//		System.out.println("userDB: "+userDB);
//		System.out.println("web type: "+this.newUser.getUserWebType());
//		System.out.println("title :" +this.newUser.getTitle());
//		System.out.println("first name: "+this.newUser.getFirstName());
//		System.out.println(this.newUser.getLastName());
//		System.out.println(this.newUser.getEmail());
//		System.out.println(this.newUser.getContactNumber());
//		System.out.println(this.newUser.getUsername());
//		System.out.println(this.newUser.getPassword());
//		System.out.println(this.newUser.getTermsAndConditions());
//		System.out.println(this.newUser.getPincode());
//		System.out.println(this.ownSelectedCustomersList);
//		System.out.println("temp: "+temp.shortValue());
//		userDB.addUser("", "", "title", "", "", "email", "workPhone", "", "", "", "", "","","", "username", "", temp.byteValue(),temp.byteValue(), "", temp.byteValue(), new Date(), new Date(),temp.byteValue(),temp.byteValue(), null,1, this.ownSelectedCustomersList);
		userDB.addUser(this.newUser.getUserWebType(), "None",
				this.newUser.getTitle(), this.newUser.getFirstName(),
				this.newUser.getLastName(), this.newUser.getEmail(), "",
				this.newUser.getContactNumber(), "", "", "", "", "", "",
				this.newUser.getUsername(), this.newUser.getPassword(),
				this.newUser.getTermsAndConditions(), temp.byteValue(), this.newUser.getPincode(),
				enabled.byteValue(), this.newUser.getPasswordExpires(),
				this.newUser.getPincodeExpires(), temp.shortValue(),
				temp.byteValue(), null,
				(int) session.getAttribute("customerId"),
				this.ownSelectedCustomersList);

		UserAuditDAO.getInstance().insertUserAdminAudit(String.valueOf(session.getAttribute("userId")),"A new user "+this.newUser.getUsername()+" was added and assigned customers", null, -1);
//		(
//				new UserAudit(Integer.parseInt(session.getAttribute("userId")
//						.toString()), new Timestamp(Calendar.getInstance()
//						.getTime().getTime()), "UserAdded", "A new user "
//						+ this.newUser.getUsername()
//						+ " was added and assigned customers", null));

		this.newUser = new User();
		initializeUsers();
		RequestContext.getCurrentInstance().update("frmUsersPage");
	}

	public void initializeEditUser() {
		FacesMessage message = null;
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		this.ownSelectedCustomersList = new ArrayList<Customer>();
		UserDAO userDB=new UserDAO();
//		System.out.println("initialize edit user");
		if (this.selectedUser != null) {
//			for (Customer cust : UserDAO.getInstance().getCustomerOfUser(
//					this.selectedUser.getUserId())) {
//				System.out.println("cust id: " + cust.getId());
//			}
			this.ownSelectedCustomersList = userDB
					.getCustomerOfUser(this.selectedUser.getUserId());
			String userType = (String) session.getAttribute("userType");
//			System.out.println("userType: " + userType);
			if (userType.equals("WebAdmin") || userType.equals("WebReadOnly")) {
				// System.out.println("entered all");
				this.ownCustomersList = userDB.getCustomerList();
				System.out.println("size: "+this.ownCustomersList.size());
			} else {
				this.ownCustomersList = userDB
						.getCustomerOfUser((int) session.getAttribute("userId"));
			}
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('editUserDlg').show();");
			
			// removing error message from UI
			Iterator<FacesMessage> iter = FacesContext.getCurrentInstance().getMessages();
			while (iter.hasNext()) {
				iter.remove();
			}
//			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().update("userMsg");
		} else {

			message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Please select a user first!",
					"Please select a user first!");
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().update("userMsg");
		}
	}

	public void modifyUser() {
		UserDAO userDB=new UserDAO();
		userDB.modifyUser(this.selectedUser.getUserId(),
				this.selectedUser.getUserWebType(),
				this.selectedUser.getUserConfgType(),
				this.selectedUser.getTitle(), this.selectedUser.getFirstName(),
				this.selectedUser.getLastName(), this.selectedUser.getEmail(),
				this.selectedUser.getWorkPhone(),
				this.selectedUser.getContactNumber(),
				this.selectedUser.getMobilePhone(),
				this.selectedUser.getAddress(), this.selectedUser.getCity(),
				this.selectedUser.getZip(), this.selectedUser.getCountry(),
				this.selectedUser.getCounty(), this.selectedUser.getUsername(),
				this.selectedUser.getPassword(),
				this.selectedUser.getTermsAndConditions(),
				this.selectedUser.getTermsAndConditionsOfService(),
				this.selectedUser.getPincode(), this.selectedUser.getEnabled(),
				this.selectedUser.getPasswordExpires(),
				this.selectedUser.getPincodeExpires(),
				this.selectedUser.getPincodeFailureCount(),
				this.selectedUser.getIsdeleted(),
				this.selectedUser.getUserBureauType());
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
//		UserAuditDAO.getInstance().insertUserAudit(
//				new UserAudit(Integer.parseInt(session.getAttribute("userId")
//						.toString()), new Timestamp(Calendar.getInstance()
//						.getTime().getTime()), "UserModified", "User "
//						+ this.selectedUser.getUsername() + " was modified",
//						null));

		userDB.modifyUserCustomers(
				this.selectedUser.getUserId(), this.ownSelectedCustomersList);

		UserAuditDAO.getInstance().insertUserAdminAudit(String.valueOf(session.getAttribute("userId")),"User "+this.selectedUser.getUsername()+" and its customer list were modified", null, -1);
//		(
//				new UserAudit(Integer.parseInt(session.getAttribute("userId")
//						.toString()), new Timestamp(Calendar.getInstance()
//						.getTime().getTime()), "UserCustomersModified",
//						"Customer list of User "
//								+ this.selectedUser.getUsername()
//								+ " was modified", null));

//		System.out.println("user name: " + this.selectedUser.getUsername());
//		System.out.println("user modified!");
	}

	public void checkUserSelection() {
		FacesMessage message = null;
		if (this.selectedUser == null) {

			message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Please select a user first!",
					"Please select a user first!");
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().update("userMsg");
		} else {
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('confirmDlg').show()");
		}
	}

	public void deleteUser() {
		UserDAO userDB=new UserDAO();
		userDB.deleteUser(this.selectedUser.getUserId());
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserAuditDAO.getInstance().insertUserAdminAudit(String.valueOf(session.getAttribute("userId")), "User "+this.selectedUser.getUsername()+ " was deleted", null, -1);
		this.selectedUser = null;
		initializeUsers();
		RequestContext.getCurrentInstance().update("frmUsersPage");
	}

	public List<User> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<User> usersList) {
		this.usersList = usersList;
	}

	public List<Customer> getCustomersList() {
		return customersList;
	}

	public void setCustomersList(List<Customer> customersList) {
		this.customersList = customersList;
	}

	public List<User> getFilteredUsersList() {
		return filteredUsersList;
	}

	public void setFilteredUsersList(List<User> filteredUsersList) {
		this.filteredUsersList = filteredUsersList;
	}

	public String getSelectedCustomerName() {
		return selectedCustomerName;
	}

	public void setSelectedCustomerName(String selectedCustomerName) {
		this.selectedCustomerName = selectedCustomerName;
	}

	public String getCurrentDateTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
		this.currentDateTime = format.format(date);
		return currentDateTime;
	}

	public void setCurrentDateTime(String currentDateTime) {
		this.currentDateTime = currentDateTime;
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	public List<Customer> getOwnCustomersList() {
		return ownCustomersList;
	}

	public void setOwnCustomersList(List<Customer> ownCustomersList) {
		this.ownCustomersList = ownCustomersList;
	}

	public List<Customer> getOwnSelectedCustomersList() {
		return ownSelectedCustomersList;
	}

	public void setOwnSelectedCustomersList(
			List<Customer> ownSelectedCustomersList) {
		this.ownSelectedCustomersList = ownSelectedCustomersList;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

}
