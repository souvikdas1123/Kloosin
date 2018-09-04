package com.kloosin.service.model;

public class UserPostRequst {

    public String postType;
    public String postBody;
    public int lognitude;
    public int latitude;
    public String postImagePath;
    public String userId;

    public String getPostImagePath() {
        return postImagePath;
    }

    public void setPostImagePath(String postImagePath) {
        this.postImagePath = postImagePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public int getLognitude() {
        return lognitude;
    }

    public void setLognitude(int lognitude) {
        this.lognitude = lognitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }


}
