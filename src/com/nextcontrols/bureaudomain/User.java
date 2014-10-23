package com.nextcontrols.bureaudomain;
// default package
// Generated 09-Feb-2010 15:54:24 by Hibernate Tools 3.2.2.GA

import com.nextcontrols.utility.ROT;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;


public class User implements Serializable{
	private int userId;
	private String userWebType;
	private String userConfgType;
	private String title;
	private String firstName;
	private String lastName;
	private String email;
	private String workPhone;
	private String contactNumber;
	private String mobilePhone;
	private String address;
	private String city;
	private String zip;
	private String county;
	private String country;
	private String username;
	private String password;
	private byte termsAndConditions=1;
	private byte termsAndConditionsOfService=1;
	private int customer_id;
	private String pincode;
	private byte enabled;
	private byte updateEnabled;
	private Date passwordExpires;
	private Date pincodeExpires;
	private short pincodeFailureCount;
	private byte isdeleted;
	private String userBureauType;
	private boolean boolEnabled;
	private boolean boolDeleted;
	
	
	private Integer checkEnabled=1;
	private Integer checkDeleted=1;
	private String divisionName;
	private Long pinCodeTimeout;
	private Long passwordTimeout;
	
	
	private Customer customers;
	
	public User() {
	}

	public User(int userId){
		this.userId = userId;
	}
	
	public User(int userId,  String userWebType,
			String userConfgType, String title,String firstName, String lastName,
			String email, String workphone,String contactNumber,String mobilePhone,String address,String city,String zip,String county,String country, String username, String password,
			byte termsAndConditions, byte termsAndConditionsOfService,int customer_id,
			String pincode, byte enabled, Date passwordExpires,
			Date pincodeExpires, short pincodeFailureCount, byte isdeleted,
			String userBureauType) {
		this.userId = userId;
		this.userWebType = userWebType;
		this.userConfgType = userConfgType;
		this.title=title;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.workPhone=workphone;
		this.contactNumber=contactNumber;
		this.mobilePhone=mobilePhone;
		this.address=address;
		this.city=city;
		this.zip=zip;
		this.county=county;
		this.country = country;
		this.username = username;
		this.password = password;
		this.termsAndConditions = termsAndConditions;
		this.termsAndConditionsOfService = termsAndConditionsOfService;
		this.customer_id=customer_id;
		this.pincode = pincode;
		this.enabled = enabled;
		this.updateEnabled=enabled;
		this.checkEnabled();
		this.passwordExpires = passwordExpires;
		this.pincodeExpires = pincodeExpires;
		this.pincodeFailureCount = pincodeFailureCount;
		this.isdeleted = isdeleted;
		this.checkDeleted();
		this.userBureauType = userBureauType;
	}

	
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Customer getCustomers() {
		return this.customers;
	}

	public void setCustomers(Customer customers) {
		this.customers = customers;
	}

	public String getUserWebType() {
		return this.userWebType;
	}

	public void setUserWebType(String userWebType) {
		this.userWebType = userWebType;
	}

	public String getUserConfgType() {
		return this.userConfgType;
	}

	public void setUserConfgType(String userConfgType) {
		this.userConfgType = userConfgType;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWorkPhone() {
		return this.workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getContactNumber() {
		return this.contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCounty() {
		return this.county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
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
		this.password = hexString.toString();
	}

	public byte getTermsAndConditions() {
		return this.termsAndConditions;
	}

	public void setTermsAndConditions(byte termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

	public byte getTermsAndConditionsOfService() {
		return this.termsAndConditionsOfService;
	}

	public void setTermsAndConditionsOfService(byte termsAndConditionsOfService) {
		this.termsAndConditionsOfService = termsAndConditionsOfService;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public String getPincode() {
		return this.pincode;
	}

	public void setPincode(String pincode) {
		ROT pinEncrypt=new ROT();
		this.pincode = pinEncrypt.transform(pincode);
	}

	public byte getEnabled() {
		return this.enabled;
	}

	public void setEnabled(byte enabled) {
		this.enabled = enabled;
	}

	public Date getPasswordExpires() {
		return this.passwordExpires;
	}

	public void setPasswordExpires(Date passwordExpires) {
		this.passwordExpires = passwordExpires;
	}

	public Date getPincodeExpires() {
		return this.pincodeExpires;
	}

	public void setPincodeExpires(Date pincodeExpires) {
		this.pincodeExpires = pincodeExpires;
	}

	public short getPincodeFailureCount() {
		return this.pincodeFailureCount;
	}

	public void setPincodeFailureCount(short pincodeFailureCount) {
		this.pincodeFailureCount = pincodeFailureCount;
	}

	public byte getIsdeleted() {
		return this.isdeleted;
	}

	public void setIsdeleted(byte isdeleted) {
		this.isdeleted = isdeleted;
	}

	public String getUserBureauType() {
		return this.userBureauType;
	}

	public void setUserBureauType(String userBureauType) {
		this.userBureauType = userBureauType;
	}
	
	private void checkEnabled(){
		if (this.getEnabled()==this.checkEnabled.byteValue()){
			setBoolEnabled(true);
		}else{
			setBoolEnabled(false);
		}
	}
	
	public void setBoolEnabled(boolean boolEnabled) {
		
		if (boolEnabled==true){
			Integer temp=1;
			setEnabled(temp.byteValue());
			this.boolEnabled = boolEnabled;
		}else{
			Integer temp=0;
			setEnabled(temp.byteValue());
			this.boolEnabled = boolEnabled;
		}
	}

	public boolean isBoolEnabled() {
		return boolEnabled;
	}
	
	private void checkDeleted(){
		if (this.getIsdeleted()==this.checkDeleted.byteValue()){
			setBoolDeleted(true);
		}else{
			setBoolDeleted(false);
		}
	}

	public void setBoolDeleted(boolean boolDeleted) {
		if (boolDeleted==true){
			Integer temp=1;
			setIsdeleted(temp.byteValue());
			this.boolDeleted = boolDeleted;
		}else{
			Integer temp=0;
			setIsdeleted(temp.byteValue());
			this.boolDeleted = boolDeleted;
		}
	}

	public boolean isBoolDeleted() {
		return boolDeleted;
	}

	public void setUpdateEnabled(byte updateEnabled) {
		this.updateEnabled = updateEnabled;
	}

	public byte getUpdateEnabled() {
		return updateEnabled;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public Long getPinCodeTimeout() {
		return pinCodeTimeout;
	}

	public void setPinCodeTimeout(Long pinCodeTimeout) {
		this.pinCodeTimeout = pinCodeTimeout;
	}

	public Long getPasswordTimeout() {
		return passwordTimeout;
	}

	public void setPasswordTimeout(Long passwordTimeout) {
		this.passwordTimeout = passwordTimeout;
	}

}
