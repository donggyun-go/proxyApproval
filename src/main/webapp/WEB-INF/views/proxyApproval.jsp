<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="<c:url value='/resources/proxyApproval.css'/>">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
	    $('select[name="proxyApprover"]').change(function() {
	        var selectedUserId = $(this).val(); 
	
	        var selectedRank = $('option:selected', this).data('userRank');
	        var displayRank = '';
	        switch (selectedRank) {
	            case 'staff':
	                displayRank = '사원';
	                break;
	            case 'am':
	                displayRank = '대리';
	                break;
	            case 'manager':
	                displayRank = '과장';
	                break;
	        }
	        $('input[name="userRank"]').val(displayRank); 
	
	        $('input[name="userId"]').val(selectedUserId);
	    });
	    
	    $('form').on('submit', function(e) {
	        e.preventDefault();
	
	        var formData = $(this).serialize();
	
	        $.ajax({
	            type: "POST",
	            url: "/dept/proxyApprovalForm",
	            data: formData,
	            success: function(response) {
	                alert("대리 결재가 성공적으로 완료되었습니다.");
	                window.close();
	            },
	            error: function(xhr, status, error) {
	                alert("오류 발생: " + error);
	            }
	        });
	    });
	});
</script>

<meta charset="UTF-8">
<title>Proxy Approval</title>
</head>
<body>
<form action="/proxyApprovalForm" method="post">
    <table border="1">
        <thead>
            <tr>
                <th colspan="2">대리 결재 정보</th>
            </tr>
        </thead>
        <tbody>
			 <tr>
			    <td>대리 결재자:</td>
			    <td>
			        <select name="proxyApprover"> 
			        	<option value="" selected>--선택--</option>
			            <c:forEach items="${userList}" var="user">
						    <option value="${user.userId}" data-user-rank="${user.userRank}">
						        ${user.userName}
						    </option>
						</c:forEach>
			        </select>
			        <input type="hidden" name="selectedUserId" value="">
			    </td>
			</tr>
			<tr>
			    <td>직급:</td>
			    <td>
			        <input type="text" name="userRank" readonly>
			    </td>
			</tr>
            <tr>
                <td>대리자:</td>
                <td>
                    <c:set var="userRank" value="${sessionScope.userRank}" />
                    <c:set var="displayRank" value="" />
                    <c:choose>
                        <c:when test="${userRank == 'staff'}"><c:set var="displayRank" value="사원" /></c:when>
                        <c:when test="${userRank == 'am'}"><c:set var="displayRank" value="대리" /></c:when>
                        <c:when test="${userRank == 'manager'}"><c:set var="displayRank" value="과장" /></c:when>
                        <c:when test="${userRank == 'gm'}"><c:set var="displayRank" value="부장" /></c:when>
                    </c:choose>
                    <input type="text" name="proxy" value="${sessionScope.userName} (${displayRank})" readonly />
                </td>
            </tr>
        </tbody>
    </table>

    <div style="margin-top: 15px;" class="div-center">
        <input type="button" value="취소" onclick="window.close();" />
        <input type="submit" value="승인" />
    </div>
</form>

</body>
</html>