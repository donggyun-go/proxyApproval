package com.my.dept.domain;

import java.time.LocalDateTime;

public class ProxyApprovalVO {
	private String proxySeq; 
	private String proxyId;
	private String approvalId;
	private String approvalName;
	private LocalDateTime proxyDate;
	private String approvalRank; 
    private LocalDateTime startDate;
    private LocalDateTime endDate; 
	
	public String getProxySeq() {
		return proxySeq;
	}
	public void setProxySeq(String proxySeq) {
		this.proxySeq = proxySeq;
	}
	public String getProxyId() {
		return proxyId;
	}
	public void setProxyId(String proxyId) {
		this.proxyId = proxyId;
	}
	public String getApprovalId() {
		return approvalId;
	}
	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}
	public String getApprovalName() {
		return approvalName;
	}
	public void setApprovalName(String approvalName) {
		this.approvalName = approvalName;
	}
	public LocalDateTime getProxyDate() {
		return proxyDate;
	}
	public void setProxyDate(LocalDateTime proxyDate) {
		this.proxyDate = proxyDate;
	}
	
    public String getApprovalRank() {
		return approvalRank;
	}
	public void setApprovalRank(String approvalRank) {
		this.approvalRank = approvalRank;
	}
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	
    @Override
    public String toString() {
        return "ProxyApprovalVO [proxySeq=" + proxySeq + ", proxyId=" + proxyId + ", approvalId=" + approvalId
                + ", proxyDate=" + proxyDate + ", approvalRank=" + approvalRank 
                + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    } 

}
