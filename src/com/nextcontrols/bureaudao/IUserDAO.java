package com.nextcontrols.bureaudao;

import java.util.Date;
import java.util.List;

import com.nextcontrols.bureaudomain.Customer;
import com.nextcontrols.bureaudomain.User;

public interface IUserDAO {
	public List<User> getUserList();
	public List<User> getAdminUserList();
	public List<Customer> getCustomerList();
	public List<Integer> getCustomerIds(int userId);
	public List<User> getUserList(List<Integer> customerIds);
	public void modifyUsers(List<User> users);
	public List<Customer> getCustomerOfUser(int userId);
	public void addUser(String userWebType, String userConfgType, String title,
			String firstName, String lastName, String email, String workPhone,
			String contactNumber, String mobilePhone, String address,
			String city, String zip, String county, String country,
			String username, String password, byte termsAndConditions,
			byte termsAndConditionsOfService, String pincode, byte enabled,
			Date passwordExpires, Date pincodeExpires,
			short pincodeFailureCount, byte isdeleted, String userBureauType,
			int customer_id, List<Customer> customers);
	public void modifyUserCustomers(int user_id, List<Customer> customers);
	public User getUser(int userId);
	public User getSpecificUser(String username);
	public boolean passwordExists(String password);
	public boolean correctPassword(String username,String password);
	public void resetPassword(String username,String password);
	public String getUsersEmail(String username);
	public int countUserHandledAlms(String username);
	public String getUserName(int userId);
}
