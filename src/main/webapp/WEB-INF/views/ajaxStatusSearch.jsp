<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
	$('table tbody tr').click(function() {
	    var url = $(this).attr('data-url'); 
	    window.location.href = url;
	});
</script>
<table>
    <thead>
        <tr>
            <th>번호</th>
            <th>작성자</th>
            <th>제목</th>
            <th>작성일</th>
            <th>결재일</th>
            <th>결재자</th>
            <th>결재상태</th>
        </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${not empty approvalList}">
            <c:forEach var="approval" items="${approvalList}">
                <tr data-url="/dept/detail/${approval.seq}">
                    <td>${approval.seq}</td>
                    <td>${approval.userName}</td>
                    <td>${approval.approvalTitle}</td>
                    <td><fmt:formatDate value="${approval.regDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                    <td><fmt:formatDate value="${approval.approvalDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                    <td>${approval.approver}</td>
                    <td>
                        <c:choose>
                            <c:when test="${approval.approvalStatus == 'tmp'}">임시저장</c:when>
                            <c:when test="${approval.approvalStatus == 'wait'}">결재대기</c:when>
                            <c:when test="${approval.approvalStatus == 'ing'}">결재중</c:when>
                            <c:when test="${approval.approvalStatus == 'complete'}">결재완료</c:when>
                            <c:when test="${approval.approvalStatus == 'return'}">반려</c:when>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="7" class="emptyContent">게시글이 없습니다.</td>
            </tr>
        </c:otherwise>
    </c:choose>
	</tbody>
</table>

