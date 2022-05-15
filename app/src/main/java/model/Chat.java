package model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class Chat {
    private Timestamp time;
    private String userMessage;
    private String otherUserMessage;
    private String chatRoom;

    public Chat(){
    }

    public Chat(Timestamp time, String userMessage, String otherUserMessage, String chatRoom) {
        this.time = time;
        this.userMessage = userMessage;
        this.chatRoom = chatRoom;
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
    public String getChatRoom() { return chatRoom; }
    public void setChatRoom(String chatRoom) { this.chatRoom = chatRoom; }
}
