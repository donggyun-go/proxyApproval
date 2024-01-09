<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<%
		if (session.getAttribute("userId") == null) {
		     response.sendRedirect("/dept/");
		     return;
		 }
	%>
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
					<td><input type="checkbox" disabled></td>
					<td><input type="checkbox" disabled></td>
					<td><input type="checkbox" disabled></td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="approval-form">
	    <form action="/dept/writeForm" method="post">
	        <table border="1">
	            <tr>
	                <td>번호:</td>
	                <td><input type="text" id="seq" name="seq" value="${nextSeq}"disabled></td>
	            </tr>
	            <tr>
	                <td>작성자:</td>
	                <td><input type="text" id="userName" name="userName" value="${userName}" disabled></td>
	            </tr>
	            <tr>
	                <td><label for="approvalTitle">제목:</label></td>
	                <td><input type="text" id="approvalTitle" name="approvalTitle" required></td>
	            </tr>
	            <tr>
	                <td><label for="approvalContent">내용:</label></td>
	                <td><textarea id="approvalContent" name="approvalContent" required></textarea></td>
	            </tr>
	            <tr>
	                <td colspan="2">
						<input type="submit" name="action" value="임시저장">
						<input type="submit" name="action" value="결재">
	                </td>
	            </tr>
	        </table>
	    </form>
	</div>
</div>
</body>
</html>