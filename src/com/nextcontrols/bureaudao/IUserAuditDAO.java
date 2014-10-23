package com.nextcontrols.bureaudao;

import java.util.Date;
import java.util.List;

import com.nextcontrols.bureaudomain.UserAudit;

public interface IUserAuditDAO {
	public void insertUserAudit(final UserAudit puserAudit);
	public void insertUserAdminAudit(String userId, String eventDesc,
			String branchCode, int websiteId);
	public List<UserAudit> getUserAudits() ;
	public int countUserIdles(int userId);
	public List<UserAudit> getSpecificUserAudits(int user_id,String siteCode);
	public List<UserAudit> getDateSpecificUserAudits(int user_id,String siteCode,Date dateFrom, Date dateTo);
	public int avgAlarmHandlingTime(String username,Date dateFrom,Date dateTo);
	public int maxAlarmHandlingTime(String username,Date dateFrom,Date dateTo);
	public int minAlarmHandlingTime(String username,Date dateFrom,Date dateTo);
	public int totalAlmsHandled(String username,Date dateFrom,Date dateTo);
	public int totalAlmsCleared(String username,Date dateFrom,Date dateTo);
	public int totalAlmsHeld(String username,Date dateFrom,Date dateTo);
	public int totalAlmsOverdue(String username,Date dateFrom,Date dateTo);
}