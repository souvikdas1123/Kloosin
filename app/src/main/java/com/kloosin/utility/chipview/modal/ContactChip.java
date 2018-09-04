package com.kloosin.utility.chipview.modal;

import android.support.annotation.NonNull;

public class ContactChip implements Comparable<ContactChip>{

    private String contactProfilePicture;
    private String contactName;
    private String contactNumber;
    private boolean isCheck;

    public String getContactProfilePicture() {
        return (contactProfilePicture);
    }

    public void setContactProfilePicture(String contactProfilePicture) {
        this.contactProfilePicture = contactProfilePicture;
    }

    public String getContactName() {
        return (contactName);
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return (contactNumber);
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public boolean isCheck() {
        return (isCheck);
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public int compareTo(@NonNull ContactChip contactChip) {
        return this.contactName.compareTo(contactChip.contactName);
    }
}
