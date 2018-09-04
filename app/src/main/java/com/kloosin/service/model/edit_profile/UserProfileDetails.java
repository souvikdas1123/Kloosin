package com.kloosin.service.model.edit_profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileDetails {

    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("emailIsValidate")
    @Expose
    private Integer emailIsValidate;
    @SerializedName("nickName")
    @Expose
    private String nickName;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("currentCity")
    @Expose
    private String currentCity;
    @SerializedName("homeTown")
    @Expose
    private String homeTown;
    @SerializedName("shortBioData")
    @Expose
    private String shortBioData;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("currentCompaneName")
    @Expose
    private String currentCompaneName;
    @SerializedName("currentDesignation")
    @Expose
    private String currentDesignation;
    @SerializedName("lastEducation")
    @Expose
    private String lastEducation;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("interestArea")
    @Expose
    private String interestArea;
    @SerializedName("friendSearchKeyword")
    @Expose
    private String friendSearchKeyword;
    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("profilePicturePath")
    @Expose
    private String profilePicturePath;
    @SerializedName("coverPicture")
    @Expose
    private String coverPicture;
    @SerializedName("coverPicturePath")
    @Expose
    private String coverPicturePath;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    @SerializedName("ipAddress")
    @Expose
    private String ipAddress;
    @SerializedName("roleId")
    @Expose
    private Integer roleId;
    @SerializedName("activeTill")
    @Expose
    private String activeTill;
    @SerializedName("isActive")
    @Expose
    private Integer isActive;
    @SerializedName("opMode")
    @Expose
    private String opMode;
    @SerializedName("longitude")
    @Expose
    private Integer longitude;
    @SerializedName("latitude")
    @Expose
    private Integer latitude;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Integer getEmailIsValidate() {
        return emailIsValidate;
    }

    public void setEmailIsValidate(Integer emailIsValidate) {
        this.emailIsValidate = emailIsValidate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getShortBioData() {
        return shortBioData;
    }

    public void setShortBioData(String shortBioData) {
        this.shortBioData = shortBioData;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCurrentCompaneName() {
        return currentCompaneName;
    }

    public void setCurrentCompaneName(String currentCompaneName) {
        this.currentCompaneName = currentCompaneName;
    }

    public String getCurrentDesignation() {
        return currentDesignation;
    }

    public void setCurrentDesignation(String currentDesignation) {
        this.currentDesignation = currentDesignation;
    }

    public String getLastEducation() {
        return lastEducation;
    }

    public void setLastEducation(String lastEducation) {
        this.lastEducation = lastEducation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInterestArea() {
        return interestArea;
    }

    public void setInterestArea(String interestArea) {
        this.interestArea = interestArea;
    }

    public String getFriendSearchKeyword() {
        return friendSearchKeyword;
    }

    public void setFriendSearchKeyword(String friendSearchKeyword) {
        this.friendSearchKeyword = friendSearchKeyword;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getCoverPicturePath() {
        return coverPicturePath;
    }

    public void setCoverPicturePath(String coverPicturePath) {
        this.coverPicturePath = coverPicturePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getActiveTill() {
        return activeTill;
    }

    public void setActiveTill(String activeTill) {
        this.activeTill = activeTill;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getOpMode() {
        return opMode;
    }

    public void setOpMode(String opMode) {
        this.opMode = opMode;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

}
