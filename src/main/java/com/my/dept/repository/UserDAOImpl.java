package com.my.dept.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.my.dept.domain.ApprovalHistoryVO;
import com.my.dept.domain.ApprovalVO;
import com.my.dept.domain.LoginVO;
import com.my.dept.domain.ProxyApprovalVO;
import com.my.dept.domain.SearchVO;
import com.my.dept.domain.UserVO;

@Repository
public class UserDAOImpl implements UserDAO {
	
	@Autowired
	private SqlSession sql;
	
	private static String namespace="com.my.dept.repository.UserDAO";
	
	@Override
	public UserVO findUserById(String userId) {
		
		return sql.selectOne(namespace + ".findUserById", userId);
	}
	@Override
	public List<ApprovalVO> getApprovalList(Map<String, Object> params) {
	    return sql.selectList(namespace + ".search", params);
	}
	
	@Override
	public void writeApproval(ApprovalVO approval) {
        sql.insert(namespace + ".writeApproval", approval);
    }

	public int findLatestSeq() {
	    return sql.selectOne(namespace + ".findLatestSeq");
	}
	@Override
	public ApprovalVO detail(int seq) {
		return sql.selectOne(namespace + ".detail", seq);
	}
	@Override
	public void updateApprovalStatus(ApprovalVO approval) {
		sql.update(namespace + ".updateApprovalStatus", approval);
	}
	@Override
	public void insertApprovalHistory(ApprovalHistoryVO history) {
		sql.insert(namespace + ".insertApprovalHistory", history);
	}
	
	@Override
	public List<ApprovalHistoryVO> getApprovalHistoryList(int seq) {
	    return sql.selectList(namespace + ".getApprovalHistoryList", seq);
	}
	@Override
	public List<ApprovalVO> search(SearchVO criteria) {
		return sql.selectList(namespace + ".search", criteria);
	}
	@Override
	public List<UserVO> findUserByRank(Map<String, Object> params) {
		return sql.selectList(namespace + ".findUserByRank", params);
	}
	@Override
	public void insertProxyApproval(ProxyApprovalVO proxyApproval) {
		sql.insert(namespace + ".insertProxyApproval", proxyApproval);
	}
	@Override
	public ProxyApprovalVO findProxy(String userId) {
		return sql.selectOne(namespace + ".findProxy", userId);
	}
	
	@Override
	public void keepLogin(String userId,String sessionId, Date sessionLimit)throws Exception{
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("sessionId", sessionId);
		paramMap.put("sessionLimit", sessionLimit);
		
		sql.update(namespace +".keepLogin", paramMap);
	}
	
	@Override
	public UserVO checkUserWithSessionKey(String value) throws Exception{
		return sql.selectOne(namespace +".check", value);
	}
	@Override
	public UserVO login(LoginVO loginVO) throws Exception {
	    return sql.selectOne(namespace + ".login", loginVO);
	}
	@Override
	public void join(UserVO user) {
		sql.insert(namespace  + ".join", user);
	}
}
