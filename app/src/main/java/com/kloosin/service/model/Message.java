package com.kloosin.service.model;

public class Message {

    public static class ResponseBody {
        private int toUserId;
        private int fromUserId;
        private String messageBody;
        private boolean isDelivered;
        private boolean isRead;
        private boolean isPending;
        private boolean isDelete;
        private String toUserProfileImagePath;
        private String fromUserProfileImagePath;
        private int id;
        private String userName;
        private int userId;
        private int createdBy;
        private String ipAddress;
        private int roleId;
        private String activeTill;
        private int isActive;
        private String opMode;
        private double longitude;
        private double latitude;

        public int getToUserId() {
            return (toUserId);
        }

        public int getFromUserId() {
            return (fromUserId);
        }

        public String getMessageBody() {
            return (messageBody);
        }

        public boolean isDelivered() {
            return (isDelivered);
        }

        public boolean isRead() {
            return (isRead);
        }

        public boolean isPending() {
            return (isPending);
        }

        public boolean isDelete() {
            return (isDelete);
        }

        public String getToUserProfileImagePath() {
            return (toUserProfileImagePath);
        }

        public String getFromUserProfileImagePath() {
            return (fromUserProfileImagePath);
        }

        public int getId() {
            return (id);
        }

        public String getUserName() {
            return (userName);
        }

        public int getUserId() {
            return (userId);
        }

        public int getCreatedBy() {
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

        public int getIsActive() {
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

    public static class SendBody {

        private int toUserId;
        private int fromUserId;
        private String messageBody;

        public int getToUserId() {
            return (toUserId);
        }

        public void setToUserId(int toUserId) {
            this.toUserId = toUserId;
        }

        public int getFromUserId() {
            return (fromUserId);
        }

        public void setFromUserId(int fromUserId) {
            this.fromUserId = fromUserId;
        }

        public String getMessageBody() {
            return (messageBody);
        }

        public void setMessageBody(String messageBody) {
            this.messageBody = messageBody;
        }

    }
}
