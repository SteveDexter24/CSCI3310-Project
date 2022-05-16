package model;

import com.google.firebase.Timestamp;

public class Chat {
    private Timestamp time;
    private String message;
    private String userId;
    private String chatRoom;

    public Chat(){
    }

    public Chat(Timestamp time, String message, String userId, String chatRoom) {
        this.time = time;
        this.message = message;
        this.userId = userId;
        this.chatRoom = chatRoom;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getChatRoom() { return chatRoom; }
    public void setChatRoom(String chatRoom) { this.chatRoom = chatRoom; }
}
