package com.my.dept.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.dept.repository.UserDAO;
import com.my.dept.domain.ApprovalHistoryVO;
import com.my.dept.domain.ApprovalVO;
import com.my.dept.domain.LoginVO;
import com.my.dept.domain.ProxyApprovalVO;
import com.my.dept.domain.SearchVO;
import com.my.dept.domain.UserVO;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDAO userDao;
	
	@Override
	public UserVO findUserById(String userId) {
		
		return userDao.findUserById(userId);
	}
	
	@Override
	public List<ApprovalVO> getApprovalList(Map<String, Object> params) {
		return userDao.getApprovalList(params);
	}

	@Override
	public void writeApproval(ApprovalVO approval) {
		userDao.writeApproval(approval);
	}

	@Override
	public int findLatestSeq() {
		return userDao.findLatestSeq();
	}

	@Override
	public ApprovalVO detail(int seq) {			
		return userDao.detail(seq);
	}

	@Override
    public void updateApprovalStatus(ApprovalVO approval) {
        userDao.updateApprovalStatus(approval);
    }

	@Override
	public void insertApprovalHistory(ApprovalHistoryVO history) {
		userDao.insertApprovalHistory(history);
	}
	
	@Override
	public List<ApprovalHistoryVO> getApprovalHistoryList(int seq) {
	    return userDao.getApprovalHistoryList(seq);
	}

	@Override
	public List<ApprovalVO> search(SearchVO criteria) {
		return userDao.search(criteria);
	}

	@Override
	public List<UserVO> findUserByRank(Map<String, Object> params) {
		return userDao.findUserByRank(params);
	}

	@Override
	public void insertProxyApproval(ProxyApprovalVO proxyApproval) {
		userDao.insertProxyApproval(proxyApproval);	
	}

	@Override
	public ProxyApprovalVO findProxy(String userId) {
		return userDao.findProxy(userId);
	}
	
	@Override
	public void keepLogin(String userId, String sessionId, Date sessionLimit) throws Exception{
		userDao.keepLogin(userId, sessionId, sessionLimit);
	}
	
	@Override
	public UserVO checkLoginBefore(String value) throws Exception{
		return userDao.checkUserWithSessionKey(value);
	}

	@Override
	public UserVO login(LoginVO loginVO) throws Exception {
	    return userDao.login(loginVO);
	}

	@Override
	public void join(UserVO user) {
		userDao.join(user);
	}
}
