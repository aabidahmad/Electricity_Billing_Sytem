package com.ElectricityAutomationInitiative.payload;


//public class JWTAuthResponse {
//    private String accessToken;
//    private String tokenType = "Bearer";
//    public JWTAuthResponse(String accessToken) {
//        this.accessToken = accessToken;
//    }
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }
//    public void setTokenType(String tokenType) {
//        this.tokenType = tokenType;
//    }
//    public String getAccessToken() {
//        return accessToken;
//    }
//    public String getTokenType() {
//        return tokenType;
//    }
//}

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class JWTAuthResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private Collection<? extends GrantedAuthority> roles = new HashSet<GrantedAuthority>();


    public JWTAuthResponse(String jwttoken, Collection<? extends GrantedAuthority> roles) {
        this.jwttoken = jwttoken;
        this.roles = roles;
    }

    public JWTAuthResponse (String token) {
        this.jwttoken = token;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }
}