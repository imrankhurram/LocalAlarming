package com.nextcontrols.bureaudao;

import java.sql.Timestamp;
import java.util.List;

public interface IUserActivityDAO {
	
	void userLogin(String username,Timestamp login_time,Timestamp last_check,String userType);
	void userLogout(String username);
	List<String> userUpdateCheck(String username,Timestamp last_check);
}
