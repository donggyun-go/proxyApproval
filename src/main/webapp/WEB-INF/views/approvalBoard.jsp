<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="<c:url value='/resources/approvalBoard.css'/>">
    <meta charset="UTF-8">
    <title>결재 프로젝트</title>
    <%
	   if (session.getAttribute("userId") == null) {
		       response.sendRedirect("/dept/");
		       return;
		   }
	%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script>
	    function openPopup() {
	
	        var url = "/dept/proxyApproval";
	        
	        window.open(url, "proxyApproval", "toolbar=yes,scrollbars=yes,resizable=yes,top=100,left=500,width=600,height=400");
	    }
	    
        $(document).ready(function() {
            $('#logoutButton').click(function() {
                window.location.href = "/dept/logout";
            });
            
            $('#writeButton').click(function(){
                window.location.href = "/dept/write";
            });
            
            $('table tbody tr').click(function() {
                var url = $(this).attr('data-url'); 
                window.location.href = url;
            });
            
    	    $("input[name='reset']").click(function(){
    	    	window.location.href = "http://localhost:8080/dept/board";
    	    });
    	    
    	    $('form[action="/dept/search"] button[type="submit"]').click(function(e) {
                var typeSelected = $('select[name="type"]').val();
                var keywordInput = $('input[name="keyword"]').val().trim(); 

                if (typeSelected === "defualt" && keywordInput) {
                    alert("검색 조건을 선택해주세요!");
                    $('select[name="type"]').focus(); 
                    e.preventDefault(); 
                    return;
                }

                if (!keywordInput) {
                    alert("검색어를 입력해주세요!");
                    $('input[name="keyword"]').focus(); 
                    e.preventDefault(); 
                    return;
                }
            })
            
            $('select[name="status"]').on('change', function() {
                var statusValue = $(this).val();
                var typeValue = $('select[name="type"]').val();
                var keywordValue = $('input[name="keyword"]').val().trim();
                var startDateValue = $('input[name="startDate"]').val();
                var endDateValue = $('input[name="endDate"]').val();

                fetchSearchResults(statusValue, typeValue, keywordValue, startDateValue, endDateValue);
            });

            function fetchSearchResults(status, type, keyword, startDate, endDate) {
                $.ajax({
                    url: "/dept/ajaxStatusSearch",
                    type: "POST",
                    data: { 
                        status: status,
                        type: type,
                        keyword: keyword,
                        startDate: startDate,
                        endDate: endDate
                    },
                    success: function(response) {
                        $("#listForm").html(response);
                    },
                    error: function(error) {
                        console.error("에러: ", error); 
                    }
                });
            }
        });
    </script>
</head>
<body>
<div class="container">
    <div class="header">
	    <div>
	        <span>환영합니다, <c:out value="${sessionScope.userName}" />
	        (<c:choose>
			    <c:when test="${sessionScope.userRank == 'staff'}">사원</c:when>
			    <c:when test="${sessionScope.userRank == 'am'}">대리</c:when>
			    <c:when test="${sessionScope.userRank == 'manager'}">과장</c:when>
			    <c:when test="${sessionScope.userRank == 'gm'}">부장</c:when>
			</c:choose>)님!</span>
		    <div class="proxy-section">
		        <c:if test="${not empty proxyInfo}">
		            <span style="font-size: smaller; color: #999;">
		                대리권한 : ${proxyInfo.approvalName}(
		                <c:choose>
		                    <c:when test="${proxyInfo.approvalRank == 'manager'}">과장</c:when>
		                    <c:when test="${proxyInfo.approvalRank == 'gm'}">부장</c:when> 
		                </c:choose>
		                ) - (${formattedStartDate} ~ ${formattedEndDate})
		            </span>
		        </c:if>
		    </div>
	    </div>
        <div class="actions">
        	<button id="writeButton">글쓰기</button>
            <c:choose>
                <c:when test="${sessionScope.userRank == 'manager' or sessionScope.userRank == 'gm'}">
                    <button onclick="openPopup()">대리결재</button>
                </c:when>
            </c:choose>
            <button id="logoutButton">로그아웃</button>
        </div>
    </div>
    <div class="search-section">
	    <form action="/dept/search" method="get">
	        <select name="status">
	            <option value="defualt" <c:if test="${param.status == 'defualt'}">selected</c:if>>결재상태</option>
	            <option value="tempStorage" <c:if test="${param.status == 'tempStorage'}">selected</c:if>>임시저장</option>
	            <option value="watingApproval" <c:if test="${param.status == 'watingApproval'}">selected</c:if>>결재대기</option>
	            <option value="Approvaling" <c:if test="${param.status == 'Approvaling'}">selected</c:if>>결재중</option>
	            <option value="CompleteApproval" <c:if test="${param.status == 'CompleteApproval'}">selected</c:if>>결재완료</option>
	            <option value="return" <c:if test="${param.status == 'return'}">selected</c:if>>반려</option>
	        </select>
	        <select name="type">
	            <option value="defualt" <c:if test="${param.type == 'defualt'}">selected</c:if>>선택</option>
	            <option value="writer" <c:if test="${param.type == 'writer'}">selected</c:if>>작성자</option>
	            <option value="approver" <c:if test="${param.type == 'approver'}">selected</c:if>>결재자</option>
	            <option value="titleContent" <c:if test="${param.type == 'titleContent'}">selected</c:if>>제목+내용</option>
	        </select>
	        <input type="text" name="keyword" placeholder="검색어를 입력하세요" value="${param.keyword}">
	        <input type="date" name="startDate" value="${param.startDate}"> ~ 
	        <input type="date" name="endDate" value="${param.endDate}">
	        <button type="submit">검색</button>
	        <input type="button" name="reset" id="reset" value="초기화"></input>
	    </form>
	</div>
    <form name = "listForm" id = "listForm">
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
			                    <td>
			                    	<c:choose>
						                <c:when test="${not empty approval.proxyApprover}">
						                    ${approval.approver}(${approval.proxyApprover})
						                </c:when>
						                <c:otherwise>
						                    ${approval.approver}
						                </c:otherwise>
						            </c:choose>
			                    </td>
			                    <td>
									${approval.approvalStatus}
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
    </form>
</div>
</body>
</html>
