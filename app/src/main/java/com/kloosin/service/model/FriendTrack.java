package com.kloosin.service.model;

/**
 * Author SD
 * Taking only the elements required now to create the model will be updated later
 **/
public class FriendTrack {

    private String userName;
    private String userId;
    private String profilePicturePath;
    private double latitude;
    private double longitude;

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
