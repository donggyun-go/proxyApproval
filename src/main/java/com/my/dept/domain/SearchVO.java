package com.my.dept.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class SearchVO {
	private String userId;
	private String userRank;
    private String type;
    private String keyword;
    private String status;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    private String proxyUserRank;
    
    
	public String getProxyUserRank() {
		return proxyUserRank;
	}
	public void setProxyUserRank(String proxyUserRank) {
		this.proxyUserRank = proxyUserRank;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserRank() {
		return userRank;
	}
	public void setUserRank(String userRank) {
		this.userRank = userRank;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "SearchVO [userId=" + userId + ", userRank=" + userRank + ", type=" + type + ", keyword=" + keyword
				+ ", status=" + status + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
