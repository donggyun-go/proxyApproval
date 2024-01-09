package com.my.dept.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.my.dept.domain.ApprovalHistoryVO;
import com.my.dept.domain.ApprovalVO;
import com.my.dept.domain.LoginVO;
import com.my.dept.domain.ProxyApprovalVO;
import com.my.dept.domain.SearchVO;
import com.my.dept.domain.UserVO;

public interface UserDAO {
	
	UserVO findUserById(String userId);

	void writeApproval(ApprovalVO approval);
	
	int findLatestSeq();

	List<ApprovalVO> getApprovalList(Map<String, Object> params);

	ApprovalVO detail(int seq);
	
	void updateApprovalStatus(ApprovalVO approval);

	void insertApprovalHistory(ApprovalHistoryVO history);

	List<ApprovalHistoryVO> getApprovalHistoryList(int seq);

	List<ApprovalVO> search(SearchVO criteria);

	List<UserVO> findUserByRank(Map<String, Object> params);

	void insertProxyApproval(ProxyApprovalVO proxyApproval);
	
	ProxyApprovalVO findProxy(String userId);
	
	// 로그인 유지 처리
	void keepLogin(String userId, String sessionId, Date sessionLimit) throws Exception;

	// 세션키 검증
	UserVO checkUserWithSessionKey(String value) throws Exception;
	
	UserVO login(LoginVO loginVO) throws Exception;

	void join(UserVO user);
}
