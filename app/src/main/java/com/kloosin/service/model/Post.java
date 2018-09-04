package com.kloosin.service.model;

public class Post {

    private long id;
    private long postId;
    private String postType;
    private String postImagePath;

    private String postVideosPath;
    private String postBody;
    private String userProfileImagePath;
    private String postTime;

    private String userName;
    private long userId;
    private long createdBy;

    private String ipAddress;
    private int roleId;
    private String activeTill;
    private int isActive;
    private String opMode;

    private double longitude;
    private double latitude;

    public long getId() {
        return (id);
    }

    public long getPostId() {
        return (postId);
    }

    public String getPostType() {
        return (postType);
    }

    public String getPostImagePath() {
        return (postImagePath);
    }

    public String getPostVideosPath() {
        return (postVideosPath);
    }

    public String getPostBody() {
        return (postBody);
    }

    public String getUserProfileImagePath() {
        return (userProfileImagePath);
    }

    public String getPostTime() {
        return (postTime);
    }

    public String getUserName() {
        return (userName);
    }

    public long getUserId() {
        return (userId);
    }

    public long getCreatedBy() {
        return (createdBy);
    }

    public String getIpAddress() {
        return (ipAddress);
    }

    public int getRoleId() {
        return (roleId);
    }

    public String getActiveTill() {
        return (activeTill);
    }

    public int isActive() {
        return (isActive);
    }

    public String getOpMode() {
        return (opMode);
    }

    public double getLongitude() {
        return (longitude);
    }

    public double getLatitude() {
        return (latitude);
    }
}
