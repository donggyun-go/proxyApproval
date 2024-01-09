<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<%
		if (session.getAttribute("userId") == null) {
		     response.sendRedirect("/dept/");
		     return;
		 }
	%>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	<script>
		$(document).ready(function() {
    	    $("input[name='reset']").click(function(){
    	    	window.location.href = "http://localhost:8080/dept/board";
    	    });
		});
	</script>
	<link rel="stylesheet" href="<c:url value='/resources/approvalWrite.css'/>">
	    <meta charset="UTF-8">
	    <title>글 작성</title>
</head>
<body>
<div class="approval-container">
	<div class="approval-checklist">
		<table border="1">
			<thead>
				<tr>
					<th>결재요청</th>
					<th>과장</th>
					<th>부장</th>
				</tr>
			</thead>
			<tbody>
			<tr>
			    <td><input type="checkbox" disabled <c:if test="${approval.approvalStatus == 'wait' || approval.approvalStatus == 'ing' || approval.approvalStatus == 'complete'}">checked="checked"</c:if>></td>
			    <td><input type="checkbox" disabled <c:if test="${approval.approvalStatus == 'ing' || approval.approvalStatus == 'complete'}">checked="checked"</c:if>></td>
			    <td><input type="checkbox" disabled <c:if test="${approval.approvalStatus == 'complete'}">checked="checked"</c:if>></td>
			</tr>
			</tbody>
		</table>
	</div>
	<div class="approval-form">
	    <form action="/dept/updateApproval" method="post">
	        <table border="1">
				<tr>
				    <td>번호:</td>
				    <td><input type="text" id="seq" name="seq" value="${approval.seq}" disabled></td>
				</tr>
				<tr>
				    <td>작성자:</td>
				    <td><input type="text" id="userName" name="userName" value="${approval.userName}" disabled></td>
				</tr>
				<tr>
				    <td><label for="approvalTitle">제목:</label></td>
			        <td><input type="text" id="approvalTitle" name="approvalTitle" value="${approval.approvalTitle}" 
					       <c:if test="${!(sessionScope.userId == approval.userId && (approval.approvalStatus == 'return' || approval.approvalStatus == 'tmp'))}">disabled</c:if>>
			        </td>
		        </tr>
				<tr>
				    <td><label for="approvalContent">내용:</label></td>
				    <td>
					    <textarea id="approvalContent" name="approvalContent" 
						    <c:if test="${!(sessionScope.userId == approval.userId && (approval.approvalStatus == 'return' || approval.approvalStatus == 'tmp'))}">disabled</c:if>
						>${approval.approvalContent}</textarea>
					</td>
				</tr>
				<tr>
				    <td colspan="2">
						<c:choose>
						    <c:when test="${userRank == 'staff'}">
						        <c:choose>
						            <c:when test="${approval.approvalStatus == 'tmp' || (approval.approvalStatus == 'return' && approval.userId == sessionScope.userId)}">
						                <input type="submit" name="action" value="임시저장">
						                <input type="submit" name="action" value="결재">
						            </c:when>
						        </c:choose>
						    </c:when>
						    <c:when test="${userRank == 'manager'}">
						        <c:choose>
						            <c:when test="${approval.approvalStatus == 'tmp' || (approval.approvalStatus == 'return' && approval.userId == sessionScope.userId)}">
						                <input type="submit" name="action" value="임시저장">
						                <input type="submit" name="action" value="결재">
						            </c:when>
						            <c:when test="${approval.approvalStatus == 'wait'}">
						                <input type="submit" name="action" value="반려">
						                <input type="submit" name="action" value="결재">
						            </c:when>
						        </c:choose>
						    </c:when>
						    <c:when test="${userRank == 'gm'}">
						        <c:choose>
						            <c:when test="${approval.approvalStatus == 'tmp' || (approval.approvalStatus == 'return' && approval.userId == sessionScope.userId)}">
						                <input type="submit" name="action" value="임시저장">
						                <input type="submit" name="action" value="결재">
						            </c:when>
						            <c:when test="${approval.approvalStatus == 'wait' || approval.approvalStatus == 'ing'}">
						                <input type="submit" name="action" value="반려">
						                <input type="submit" name="action" value="결재">
						            </c:when>
						        </c:choose>
						    </c:when>
						</c:choose>

				        <input type="button" name="reset" id="reset" value="돌아가기">
				        <input type="hidden" id="seq" name="seq" value="${approval.seq}">
				    </td>
				</tr>
	        </table>
	    </form>
	</div>
	<div class="approval-history">
    	<table border="1">
	        <thead>
	            <tr>
	                <th>번호</th>
	                <th>결재일</th>
	                <th>결재자</th>
	                <th>결재상태</th>
	            </tr>
	        </thead>
	        <tbody>
			<c:forEach var="history" items="${histories}" varStatus="status">
			    <tr>
			        <td>${status.index + 1}</td>
			        <td><fmt:formatDate value="${history.historyDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			        <td>
			            <c:choose>
			                <c:when test="${not empty history.proxyApprover}">
			                    ${history.approver}(${history.proxyApprover})
			                </c:when>
			                <c:otherwise>
			                    ${history.approver}
			                </c:otherwise>
			            </c:choose>
			        </td>
			        <td>
			            <c:choose>
			                <c:when test="${history.approvalStatus == 'tmp'}">임시저장</c:when>
			                <c:when test="${history.approvalStatus == 'wait'}">결재대기</c:when>
			                <c:when test="${history.approvalStatus == 'ing'}">결재중</c:when>
			                <c:when test="${history.approvalStatus == 'complete'}">결재완료</c:when>
			                <c:when test="${history.approvalStatus == 'return'}">반려</c:when>
			            </c:choose>
			        </td>
			    </tr>
			</c:forEach>
	        </tbody>
		</table>
	</div>
</div>
</body>
</html>