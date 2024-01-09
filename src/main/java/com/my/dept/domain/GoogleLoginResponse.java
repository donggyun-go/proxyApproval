package com.my.dept.domain;

public class GoogleLoginResponse {
    private String access_token; // 애플리케이션이 Google API 요청을 승인하기 위해 보내는 토큰
    private String expires_in;   // Access Token의 남은 수명
    private String refreshToken;    // 새 액세스 토큰을 얻는 데 사용할 수 있는 토큰
    private String scope;
    private String token_type;   // 반환된 토큰 유형(Bearer 고정)
    private String id_token;
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public String getId_token() {
		return id_token;
	}
	public void setId_token(String id_token) {
		this.id_token = id_token;
	}
	
	@Override
	public String toString() {
		return "GoogleLoginResponse [access_token=" + access_token + ", expires_in=" + expires_in + ", refreshToken="
				+ refreshToken + ", scope=" + scope + ", token_type=" + token_type + ", id_token=" + id_token + "]";
	}
    
}
