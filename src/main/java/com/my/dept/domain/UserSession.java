package com.my.dept.domain;

public class UserSession {
    private String userRank;
    private String approvalUserRank;
    private String approvalUserId;
    private String userName;
    private String userId;
	public String getUserRank() {
		return userRank;
	}
	public void setUserRank(String userRank) {
		this.userRank = userRank;
	}
	public String getApprovalUserRank() {
		return approvalUserRank;
	}
	public void setApprovalUserRank(String approvalUserRank) {
		this.approvalUserRank = approvalUserRank;
	}
	public String getApprovalUserId() {
		return approvalUserId;
	}
	public void setApprovalUserId(String approvalUserId) {
		this.approvalUserId = approvalUserId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "userSession [userRank=" + userRank + ", approvalUserRank=" + approvalUserRank + ", approvalUserId="
				+ approvalUserId + ", userName=" + userName + ", userId=" + userId + "]";
	}
    
    
}
