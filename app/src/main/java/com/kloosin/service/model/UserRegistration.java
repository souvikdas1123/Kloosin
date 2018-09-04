package com.kloosin.service.model;

public class UserRegistration {

    public static class Request {

        private String password;
        private String confirmPassword;
        private String firstName;
        private String lastName;
        private String otp;
        private String mobileNo;

        public String getPassword() {
            return (password);
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return (confirmPassword);
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public String getFirstName() {
            return (firstName);
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return (lastName);
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getOtp() {
            return (otp);
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getMobileNo() {
            return (mobileNo);
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }
    }

    public static class Response {

    }
}
