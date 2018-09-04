package com.kloosin.service.model;

import com.google.gson.annotations.SerializedName;

public class UserDetails {

    public String access_token;
    public String token_type;
    public int expires_in;
    public String fullName;
    public String userId;
    public String mobileNo;
    public String profileImage;

    @SerializedName(".issued")
    public String issued;

    @SerializedName(".expires")
    public String expires;

    public String getAccessToken() {
        return (access_token);
    }

    public String getTokenType() {
        return (token_type);
    }

    public int getExpiresIn() {
        return (expires_in);
    }

    public String getFullName() {
        return (fullName);
    }

    public String getIssued() {
        return (issued);
    }

    public String getExpires() {
        return (expires);
    }

    public String getUserId() {
        return (userId);
    }

    public String getMobileNo() {
        return (mobileNo);
    }

    public String getProfileImage() {
        return (profileImage);
    }

    ////// Login Requst

    public static class LoginRequest {

        public String grant_type;
        public String username;
        public String password;

        public String getGrantType() {
            return (grant_type);
        }

        public void setGrantType(String grant_type) {

            this.grant_type = grant_type;

        }

        public String getUsername() {
            return (username);

        }
        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return (password);
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }











}
