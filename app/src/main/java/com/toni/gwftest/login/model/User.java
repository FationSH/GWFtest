package com.toni.gwftest.login.model;

/**
 * This is very simple class and it only contains the user attributes and JWToken
 * a constructor and the getters
 */

public class User {

    private String username, token, refresh;

    public User(String username, String token, String refresh) {
        this.username = username;
        this.refresh = refresh;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getRefresh() {
        return refresh;
    }

}
