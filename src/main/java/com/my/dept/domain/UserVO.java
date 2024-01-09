package com.my.dept.domain;

import java.util.Date;

public class UserVO {
	private String userId;
	private String userPass;
	private String userName;
	private String userRank;
	private String sessionId;
	private Date sessionLimit;
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Date getSessionLimit() {
		return sessionLimit;
	}
	public void setSessionLimit(Date sessionLimit) {
		this.sessionLimit = sessionLimit;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserRank() {
		return userRank;
	}
	public void setUserRank(String userRank) {
		this.userRank = userRank;
	}
	
	@Override
	public String toString() {
		return "UserVO [userId=" + userId + ", userPass=" + userPass + ", userName=" + userName + ", userRank="
				+ userRank + ", sessionId=" + sessionId + ", sessionLimit=" + sessionLimit + "]";
	}
	
}

