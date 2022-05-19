package model;

import com.google.firebase.Timestamp;

public class Notification {
    private String senderUsername;
    private String to;
    private String imageUrl;
    private String message;
    private boolean isSeen;
    private Timestamp sentTime;

    public Notification() {
    }

    public Notification(String senderUsername, String to, String imageUrl, String message, Timestamp sentTime, boolean isSeen) {
        this.senderUsername = senderUsername;
        this.imageUrl = imageUrl;
        this.message = message;
        this.sentTime = sentTime;
        this.isSeen = isSeen;
        this.to = to;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getSentTime() {
        return sentTime;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
