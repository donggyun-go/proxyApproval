package com.my.dept;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.dept.domain.ApprovalHistoryVO;
import com.my.dept.domain.ApprovalVO;
import com.my.dept.domain.GoogleLoginResponse;
import com.my.dept.domain.GoogleOAuthRequest;
import com.my.dept.domain.KakaoProfile;
import com.my.dept.domain.LoginVO;
import com.my.dept.domain.OAuthToken;
import com.my.dept.domain.ProxyApprovalVO;
import com.my.dept.domain.SearchVO;
import com.my.dept.domain.UserVO;
import com.my.dept.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
    @Value("${google.auth.url}")
    private String googleAuthUrl;

    @Value("${google.login.url}")
    private String googleLoginUrl;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.redirect.url}")
    private String googleRedirectUrl;

    @Value("${google.secret}")
    private String googleClientSecret;

    @Value("${yj.key}")
    private String yjKey;
    
    
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String login(Model model, HttpServletResponse response, HttpServletRequest request) throws Exception {
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
		    for(Cookie tempCookie : cookies){
		        if(tempCookie.getName().equals("id")){
		            response.sendRedirect("/dept/board");
		        }
		    }
		}
		return "login";
	}
	
    @RequestMapping(value = "/loginPost", method = RequestMethod.POST)
    public String loginPOST(LoginVO loginVO, HttpSession session, HttpServletResponse response, Model model) throws Exception {
        String id = loginVO.getUserId();
        String passwd = loginVO.getUserPass();
        boolean loginChk = loginVO.isUseCookie();
        
        UserVO user = userService.login(loginVO);
        if (id.equals(user.getUserId())) {
            if (passwd.equals(user.getUserPass())) {
                session.setAttribute("id", id);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userRank",user.getUserRank());
                session.setAttribute("userName", user.getUserName());
                if (loginChk) {
                    Cookie cookie = new Cookie("id", id);
                    cookie.setMaxAge(60);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
                return "redirect:/board";
            } else {
                return "redirect:/"; 
            }
        } else {
            return "redirect:/";
        }
    }
    
    @RequestMapping(value="/auth/kakao/callback", method = RequestMethod.GET)
    public String kakaoCallback(String code, HttpSession session) {
        // 카카오 API 서버에게 POST 방식으로 key=value 데이터를 요청
        // 요청 방법 -> 여러가지 라이브러리  : HttpsURLConnection, Retrofit2(주로 안드로이드), OkHttp, RestTemplate
        RestTemplate rt = new RestTemplate();

        // HttpHeader 객체 생성
       	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // key=value 형태의 데이터라는 것을 알려주는 부분

        // HttpBody 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "404bed88601991f9196461d7f8d446ed");
        params.add("redirect_uri", "http://localhost:8080/dept/auth/kakao/callback");
        params.add("code", code);

        // HttpHeader와 HttpBody를 하나의 객체에 담기 -> 만든 이유 : 아래의 exchange 함수에 HttpEntity를 넣어야 해서..
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers); // body 데이터와 headers 값을 가지고 있는 Entity

        // 카카오에게 Http 요청하기 (POST 방식) -> response라는 변수에 응답을 받음
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        
        // oauthToken에 응답 데이터 담기
        // json 데이터를 자바에서 처리하기 위해 자바 객체로 바꿔야 한다.
        // 객체(현재는 OAuthToken)에 담을 때 사용할 수 있는 라이브러리 : Gson, Json Simple, ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());
        
        RestTemplate rt2 = new RestTemplate();

        // HttpHeader 객체 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // key=value 형태의 데이터라는 것을 알려주는 부분

        // HttpHeader를 객체에 담기 -> 만든 이유 : 아래의 exchange 함수에 HttpEntity를 넣어야 해서..
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers2); // headers 값을 가지고 있는 Entity

        // 카카오에게 Http 요청하기 (POST 방식) -> response2라는 변수에 응답을 받음
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );
        
        // kakaoProfile에 응답 데이터 담기
        // json 데이터를 자바에서 처리하기 위해 자바 객체로 바꿔야 한다.
        // 객체(현재는 OAuthToken)에 담을 때 사용할 수 있는 라이브러리 : Gson, Json Simple, ObjectMapper
        ObjectMapper objectMapper2 = new ObjectMapper();

        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // User 오브젝트 : (id), username, password, email, (role), (createDate)
        System.out.println("카카오 아이디(번호) : " + kakaoProfile.getId());
        System.out.println("카카오 이메일 : " + kakaoProfile.getKakao_account().getEmail());

        System.out.println("서버 유저네임 : " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
        System.out.println("서버 이메일 : " + kakaoProfile.getKakao_account().getEmail());
        UUID garbagePassword = UUID.randomUUID();
        System.out.println("서버 패스워드 : " + garbagePassword);

        UserVO user = new UserVO();
        user.setUserId(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
        user.setUserPass(garbagePassword.toString());
        user.setUserRank("gm");
        user.setUserName(kakaoProfile.getProperties().getNickname());
        
        System.out.println(user.toString());

        // 가입자 혹은 비가입자 분기 (이미 회원인지 아닌지 체크)
        UserVO originUser = userService.findUserById(user.getUserId());
        System.out.println(originUser);
        // 비가입자(null)이면, 회원가입 후 로그인 처리
        if (originUser == null) {
        	System.out.println("기존 회원이 아니기에 자동으로 회원가입을 진행합니다.");
            userService.join(user);
        }
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userRank",user.getUserRank());
        session.setAttribute("userName", user.getUserName());
        System.out.println("자동 로그인을 진행합니다.");        

        return "redirect:/board";
    }
    
    @RequestMapping(value="/api/v1/oauth2/google", method = RequestMethod.POST)
    public String loginUrlGoogle(){
        String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
                + "&redirect_uri=http://localhost:8080/api/v1/oauth2/google&response_type=code&scope=email%20profile%20openid&access_type=offline";
        return reqUrl;
    }
	
	// 로그아웃 처리
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request,
	                     HttpServletResponse response,
	                     HttpSession session) throws Exception {
	    //session 초기화
	    session.invalidate();
	    
	    //로그인 관련 쿠키 삭제
	    Cookie[] cookies = request.getCookies();
	    if(cookies!=null){
	        for(Cookie tempCookie : cookies){
	            if(tempCookie.getName().equals("id")){
	                tempCookie.setMaxAge(0);
	                tempCookie.setPath("/");
	                response.addCookie(tempCookie);
	                
	            }
	        }
	    }
	    return "redirect:/"; 
	}

    // 구글에서 리다이렉션
    @RequestMapping(value = "/login/oauth_google_check", method = RequestMethod.GET)
    public String oauth_google_check(HttpServletRequest request,
                                     @RequestParam(value = "code") String authCode,
                                     HttpServletResponse response) throws Exception{

        //2.구글에 등록된 레드망고 설정정보를 보내어 약속된 토큰을 받위한 객체 생성
        GoogleOAuthRequest googleOAuthRequest =  new GoogleOAuthRequest();
        googleOAuthRequest.setClientId(googleClientId);
        googleOAuthRequest.setClientSecret(googleClientSecret);
        googleOAuthRequest.setCode(authCode);
        googleOAuthRequest.setRedirectUri(googleRedirectUrl);
        googleOAuthRequest.setGrantType("authorization_code");

        RestTemplate restTemplate = new RestTemplate();

        //3.토큰요청을 한다.
        ResponseEntity<GoogleLoginResponse> apiResponse = restTemplate.postForEntity(googleAuthUrl + "/token", googleOAuthRequest, GoogleLoginResponse.class);
        //4.받은 토큰을 토큰객체에 저장
        GoogleLoginResponse googleLoginResponse = apiResponse.getBody();

        System.out.println("responseBody {}" + googleLoginResponse.toString());


        String googleToken = googleLoginResponse.getId_token();

        //5.받은 토큰을 구글에 보내 유저정보를 얻는다.
        String requestUrl = UriComponentsBuilder.fromHttpUrl(googleAuthUrl + "/tokeninfo").queryParam("id_token",googleToken).toString();

        //6.허가된 토큰의 유저정보를 결과로 받는다.
        String resultJson = restTemplate.getForObject(requestUrl, String.class);

        return resultJson;
    }

	
	@RequestMapping(value="/board", method = RequestMethod.GET)
	public String approvalBoard(HttpServletRequest request, Model model, HttpSession session) {

	    Cookie[] cookies = request.getCookies();
	    if(cookies !=null){
	        for(Cookie tempCookie : cookies){
	            if(tempCookie.getName().equals("id")){
	                String userId = tempCookie.getValue();
	                UserVO user = userService.findUserById(userId);
	                session.setAttribute("userId", user.getUserId());
	                session.setAttribute("userRank",user.getUserRank());
	                session.setAttribute("userName", user.getUserName());
	            }
	        }
	    }

	    String userRank = (String) session.getAttribute("userRank");
	    String userId = (String) session.getAttribute("userId");
	    ProxyApprovalVO proxyInfo = userService.findProxy(userId);
	    boolean isProxyAuthorized = (proxyInfo != null);
	    String proxyUserRank = null;
	    
	    if (proxyInfo != null) {
	        String approvalUserId = proxyInfo.getApprovalId(); 
	        UserVO ApprovalUser = userService.findUserById(approvalUserId); 

	        if (ApprovalUser != null) {
	        	proxyUserRank = ApprovalUser.getUserRank();
	        }
	    }
	    
	    Map<String, Object> params = new HashMap<>();
	    params.put("userId", userId);
	    params.put("userRank", userRank);
	    params.put("isProxyAuthorized", isProxyAuthorized);
	    params.put("proxyUserRank", proxyUserRank);
	    List<ApprovalVO> approvalList = userService.getApprovalList(params);
	    System.out.println(approvalList);
	    
	    model.addAttribute("approvalList", approvalList);

	    if (proxyInfo != null) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	        String formattedStartDate = (proxyInfo.getStartDate() != null) ? proxyInfo.getStartDate().format(formatter) : "N/A";
	        String formattedEndDate = (proxyInfo.getEndDate() != null) ? proxyInfo.getEndDate().format(formatter) : "N/A";

	        model.addAttribute("formattedStartDate", formattedStartDate);
	        model.addAttribute("formattedEndDate", formattedEndDate);
	        model.addAttribute("proxyInfo", proxyInfo);
	    }

	    return "approvalBoard";
	}
	
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchApproval(SearchVO criteria, Model model, HttpSession session) {
    	
    	String userRank = (String) session.getAttribute("userRank");
	    String userId = (String) session.getAttribute("userId");
	    
	    ProxyApprovalVO proxyInfo = userService.findProxy(userId);
	    String proxyUserRank = null;
	    
	    if (proxyInfo != null) {
	        String approvalUserId = proxyInfo.getApprovalId(); 
	        UserVO ApprovalUser = userService.findUserById(approvalUserId); 

	        if (ApprovalUser != null) {
	        	proxyUserRank = ApprovalUser.getUserRank();
	        }
	    }
	    
    	criteria.setUserId(userId);
    	criteria.setUserRank(userRank);
    	criteria.setProxyUserRank(proxyUserRank);
        List<ApprovalVO> searchResults = userService.search(criteria);
        model.addAttribute("approvalList", searchResults);
        
	    if (proxyInfo != null) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	        String formattedStartDate = (proxyInfo.getStartDate() != null) ? proxyInfo.getStartDate().format(formatter) : "N/A";
	        String formattedEndDate = (proxyInfo.getEndDate() != null) ? proxyInfo.getEndDate().format(formatter) : "N/A";

	        model.addAttribute("formattedStartDate", formattedStartDate);
	        model.addAttribute("formattedEndDate", formattedEndDate);
	        model.addAttribute("proxyInfo", proxyInfo);
	    }

        return "approvalBoard"; 
    }
    
    @RequestMapping(value = "/ajaxStatusSearch", method = RequestMethod.POST)
    public Map<String, Object> ajaxSearch(HttpSession session,
                                                @ModelAttribute SearchVO criteria) {
        String userId = (String) session.getAttribute("userId");
        String userRank = (String) session.getAttribute("userRank");
        
        criteria.setUserId(userId);
        criteria.setUserRank(userRank);
        
	    ProxyApprovalVO proxyInfo = userService.findProxy(userId);
	    String proxyUserRank = null;
	    
	    if (proxyInfo != null) {
	        String approvalUserId = proxyInfo.getApprovalId(); 
	        UserVO ApprovalUser = userService.findUserById(approvalUserId); 

	        if (ApprovalUser != null) {
	        	proxyUserRank = ApprovalUser.getUserRank();
	        }
	    }
    	criteria.setProxyUserRank(proxyUserRank);
    	
        List<ApprovalVO> approvals = userService.search(criteria);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("approvalList", approvals);
        resultMap.put("viewName", "ajaxsSearch");

        return resultMap;
    }

	@RequestMapping(value="/write", method = RequestMethod.GET)
	public String approvalWrite(Model model, HttpSession session) {
		
	    int nextSeq = userService.findLatestSeq();
	    String userName = (String) session.getAttribute("userName");

	    model.addAttribute("nextSeq", nextSeq);
	    model.addAttribute("userName", userName);
	    
		return "approvalWrite";
	}
	
	@RequestMapping(value="/writeForm", method = RequestMethod.POST)
	public String approvalWriteForm(ApprovalVO approval, HttpSession session,
			@RequestParam("action") String action) {
	    
	    String userId = (String) session.getAttribute("userId");
	    String userRank = (String) session.getAttribute("userRank");
	    ProxyApprovalVO proxyInfo = userService.findProxy(userId);
	    if (proxyInfo != null) {
	        userRank = proxyInfo.getApprovalRank();
	    }
	    
	    if("반려".equals(action)) {
	    	approval.setApprovalStatus("return");
	    	} 
	    else if("임시저장".equals(action)) {
	    	approval.setApprovalStatus("tmp");
	    	} 
	    else if("결재".equals(action)) {
		    if("staff".equals(userRank)) {
		        approval.setApprovalStatus("wait");
		    } else if("am".equals(userRank)) {
		        approval.setApprovalStatus("wait");
		    } else if("manager".equals(userRank)) {
		        approval.setApprovalStatus("ing");
		    } else if("gm".equals(userRank)) {
		        approval.setApprovalStatus("complete");
		    } 
	    }
	    
	    approval.setSeq(userService.findLatestSeq());
	    approval.setUserId(userId);
	    approval.setRegDate(new Date());
	    userService.writeApproval(approval);
	    
	    ApprovalHistoryVO history = new ApprovalHistoryVO();
	    history.setHistoryDate(new Date());
	    history.setApprover(userId);
	    if (proxyInfo != null) {
	        history.setProxyApprover(proxyInfo.getApprovalId());
	    }
	    history.setApprovalStatus(approval.getApprovalStatus());
	    history.setApprovalSeq(approval.getSeq());
 
	    userService.insertApprovalHistory(history);
	    
	    return "redirect:/board";
	}
	
	@RequestMapping("/detail/{seq}")
	public String getApprovalDetail(@PathVariable("seq") int seq, Model model,HttpSession session) {
	    ApprovalVO approval = userService.detail(seq); 
	    List<ApprovalHistoryVO> histories = userService.getApprovalHistoryList(seq);
	    String userRank = (String) session.getAttribute("userRank");
	    String userId = (String) session.getAttribute("userId");
	    ProxyApprovalVO proxyInfo = userService.findProxy(userId);
	    if (proxyInfo != null) {
	        userRank = proxyInfo.getApprovalRank();
	    }
	    model.addAttribute("userRank", userRank);
	    model.addAttribute("approval", approval);
	    model.addAttribute("histories", histories);
	    
	    return "approvalDetail";
	}
	
	@RequestMapping(value="/updateApproval", method = RequestMethod.POST)
	public String updateApprovalStatus(@RequestParam("seq") int seq,
									   @RequestParam(value="approvalTitle", required=false) String title,
									   @RequestParam(value="approvalContent", required=false) String content,
	                                   @RequestParam("action") String action, 
	                                   HttpSession session) {

	    String userRank = (String) session.getAttribute("userRank");
	    String userId = (String) session.getAttribute("userId");
	    ApprovalVO approval = new ApprovalVO();	      
	    ProxyApprovalVO proxyInfo = userService.findProxy(userId);
	    
	    ApprovalVO existingApproval = userService.detail(seq);
	    String currentStatus = existingApproval.getApprovalStatus();
	    approval.setSeq(seq);
	    
	    if (proxyInfo != null) {
	        userRank = proxyInfo.getApprovalRank();
	    }

	    if("임시저장".equals(action)) {
	        approval.setApprovalStatus("tmp");
    	    approval.setApprovalTitle(title); 
    	    approval.setApprovalContent(content); 
	    } else if("반려".equals(action)){
	    	approval.setApprovalStatus("return");
	    }else if("결재".equals(action)) {
	        switch (userRank) {
		        case "staff":
		        case "am":
		            if ("tmp".equals(currentStatus) || "return".equals(currentStatus)) {
		                approval.setApprovalStatus("wait");
		        	    approval.setApprovalTitle(title); 
		        	    approval.setApprovalContent(content);  
		            }
		            break;
	            case "manager":
	                if ("wait".equals(currentStatus)) {
	                    approval.setApprovalStatus("ing");
	                }

	                else if ("tmp".equals(currentStatus) || "return".equals(currentStatus)) {
		                approval.setApprovalStatus("ing");
		        	    approval.setApprovalTitle(title); 
		        	    approval.setApprovalContent(content);  
		            }
	                break;
	            case "gm":
	                if ("ing".equals(currentStatus)) {
	                    approval.setApprovalStatus("complete");
	                }
	                else if ("wait".equals(currentStatus)) {
	                    approval.setApprovalStatus("complete");
	                }
	                else if ("tmp".equals(currentStatus)) {
	                    approval.setApprovalStatus("complete");
		        	    approval.setApprovalTitle(title); 
		        	    approval.setApprovalContent(content);
	                }
	                break;
	        }
	    }
	    userService.updateApprovalStatus(approval);

	    ApprovalHistoryVO history = new ApprovalHistoryVO();
	    history.setHistoryDate(new Date());
	    String currentUser = (String) session.getAttribute("userId");
	    history.setApprover(currentUser);
	    if (proxyInfo != null) {
	        history.setProxyApprover(proxyInfo.getApprovalId());
	    }
	    history.setApprovalStatus(approval.getApprovalStatus());
	    history.setApprovalSeq(approval.getSeq());

	    userService.insertApprovalHistory(history);

	    return "redirect:/board";
	}
	
	@RequestMapping(value="/proxyApproval")
	public String ProxyApproval(HttpSession session, Model model) {
	    String userName = (String) session.getAttribute("userName");
	    String userRank = (String) session.getAttribute("userRank");
	    
	    Map<String, Object> params = new HashMap<>();
	    params.put("userRank", userRank);

	    List<UserVO> userList = userService.findUserByRank(params);
	    
	    session.setAttribute("userName", userName);
	    session.setAttribute("userRank", userRank);
	    model.addAttribute("userList", userList);
	    
	    return "proxyApproval"; 
	}
	
	@RequestMapping(value="/proxyApprovalForm", method=RequestMethod.POST)
	public ResponseEntity<String> proxyApprovalForm(HttpSession session, @RequestParam("proxyApprover") String proxyId) {
	    String approvalId = (String) session.getAttribute("userId");
	    
	    ProxyApprovalVO proxyApproval = new ProxyApprovalVO();
	    proxyApproval.setProxyId(proxyId);
	    proxyApproval.setApprovalId(approvalId);
	    proxyApproval.setProxyDate(LocalDateTime.now());
	    
	    userService.insertProxyApproval(proxyApproval);
	    
	    return new ResponseEntity<>(HttpStatus.OK);
	}
}
