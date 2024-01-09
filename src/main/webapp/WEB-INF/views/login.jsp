<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<%
	
	   if (session.getAttribute("userId") != null) {
	       response.sendRedirect("/dept/board");  
	       return;
	   }
	
	%>
	<link rel="stylesheet" href="<c:url value='/resources/login.css'/>">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script type="text/javascript" src="https://developers.kakao.com/sdk/js/kakao.js"></script>
	<script>
	$(document).ready(function() {
	    $('form').on('submit', function(e) {
	        var userId = $("input[name='userId']").val().trim();
	        var userPass = $("input[name='userPass']").val().trim();
	        
	        if (userId === "") {
	            alert("아이디를 입력해주세요.");
	            $("input[name='userId']").focus();
	            e.preventDefault();
	            return;
	        }
	        
	        if (userPass === "") {
	            alert("비밀번호를 입력해주세요.");
	            $("input[name='userPass']").focus();
	            e.preventDefault(); 
	            return;
	        }
	    });
	});
	
    Kakao.init('8ca5e75b3f8e14dfa2223601ee413bae');
    function kakaoLogin() {
        Kakao.Auth.login({
            success: function (response) {
                Kakao.API.request({
                    url: '/dept/loginPost',
                    success: function (response) {
                        alert(JSON.stringify(response))
                    },
                    fail: function (error) {
                        alert(JSON.stringify(error))
                    },
                })
            },
            fail: function (error) {
                alert(JSON.stringify(error))
            },
        })
    }
	</script>
	<script src="https://apis.google.com/js/platform.js" async defer></script>
	<meta name="google-signin-client_id" content="235401747591-uldlif5jpnrl7fkdgnqp6ub5u8ncktmd.apps.googleusercontent.com">
    <meta charset="UTF-8">
    <title>결재 프로젝트</title>
</head>
<body>
     <div class="login-container">
        <h2>로그인</h2>
        <form action="/dept/loginPost" method="post">
            <div class="input-container">
                <input type="text" name="userId" placeholder="아이디">
            </div>
            <div class="input-container">
                <input type="password" name="userPass" placeholder="비밀번호">
            </div>
            <div class="custom-control custom-checkbox small">
               <label>
                <input type="checkbox" name="useCookie"> 로그인유지
               </label>
            </div>            
            <button type="submit">로그인</button>
            <a href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=404bed88601991f9196461d7f8d446ed&redirect_uri=http://localhost:8080/dept/auth/kakao/callback">
			    <img src="resources/img/kakao_login_medium_wide.png" />
			</a>
            <a href="https://accounts.google.com/o/oauth2/auth?client_id=235401747591-uldlif5jpnrl7fkdgnqp6ub5u8ncktmd.apps.googleusercontent.com&redirect_uri=http://localhost:8080/login/oauth_google_check&scope=openid%20email%20profile&response_type=code">
                <img src="resources/img/web_neutral_sq_SI@2x.png" style="width: 300px; height: 45px;" />
            </a>
        </form>
    </div>
	<c:if test="${not empty sessionScope.errorMessage}">
        <script>
            alert('${sessionScope.errorMessage}');
        </script>
    </c:if>
</body>
</html>