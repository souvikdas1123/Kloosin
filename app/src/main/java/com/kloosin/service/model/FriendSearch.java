package com.kloosin.service.model;

public class FriendSearch {

    private String fullname;
    private String userId;

    public FriendSearch(String fullname, String userId) {
        this.fullname = fullname;
        this.userId = userId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
