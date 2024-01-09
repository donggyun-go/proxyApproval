package com.my.dept.domain;

import java.util.Date;

public class ApprovalHistoryVO {

    private int historySeq;
    private Date historyDate;
    private String approver;
    private String proxyApprover;
    private String approvalStatus;
    private int approvalSeq;
	public int getHistorySeq() {
		return historySeq;
	}
	public void setHistorySeq(int historySeq) {
		this.historySeq = historySeq;
	}
	public Date getHistoryDate() {
		return historyDate;
	}
	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
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
	public int getApprovalSeq() {
		return approvalSeq;
	}
	public void setApprovalSeq(int approvalSeq) {
		this.approvalSeq = approvalSeq;
	}
	@Override
	public String toString() {
		return "ApprovalHistoryVO [historySeq=" + historySeq + ", historyDate=" + historyDate + ", approver=" + approver
				+ ", proxyApprover=" + proxyApprover + ", approvalStatus=" + approvalStatus + ", approvalSeq="
				+ approvalSeq + "]";
	}


}