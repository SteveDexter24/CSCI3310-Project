package model;

import com.google.firebase.Timestamp;

public class Chat {
    private Timestamp time;
    private String userMessage;
    private String otherUserMessage;

    public Chat(Timestamp time, String userMessage, String otherUserMessage) {
        this.time = time;
        this.userMessage = userMessage;
        this.otherUserMessage = otherUserMessage;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getOtherUserMessage() {
        return otherUserMessage;
    }

    public void setOtherUserMessage(String otherUserMessage) {
        this.otherUserMessage = otherUserMessage;
    }
}
