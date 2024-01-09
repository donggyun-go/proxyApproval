package com.my.dept.domain;

import java.util.Date;

public class ApprovalVO {
    private int seq;
    private String userId;
    private String userName;
    private String approvalTitle;
    private String approvalContent;
    private Date regDate;
    private Date approvalDate;
    private String approver;
    private String approverName;
    private String proxyApprover;
    private String approvalStatus;
    private ApprovalHistoryVO history;
    
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getApprovalTitle() {
		return approvalTitle;
	}
	public void setApprovalTitle(String approvalTitle) {
		this.approvalTitle = approvalTitle;
	}
	
	public String getApprovalContent() {
		return approvalContent;
	}
	public void setApprovalContent(String approvalContent) {
		this.approvalContent = approvalContent;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public Date getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getProxyApprover() {
		return proxyApprover;
	}
	public void setProxyApprover(String proxyApprover) {
		this.proxyApprover = proxyApprover;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
    public ApprovalHistoryVO getHistory() {
        return history;
    }
    public void setHistory(ApprovalHistoryVO history) {
        this.history = history;
    }
	public String getApproverName() {
		return approverName;
	}
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	@Override
	public String toString() {
		return "ApprovalVO [seq=" + seq + ", userId=" + userId + ", userName=" + userName + ", approvalTitle="
				+ approvalTitle + ", approvalContent=" + approvalContent + ", regDate=" + regDate + ", approvalDate="
				+ approvalDate + ", approver=" + approver + ", approverName=" + approverName + ", proxyApprover="
				+ proxyApprover + ", approvalStatus=" + approvalStatus + ", history=" + history + "]";
	}
     
}