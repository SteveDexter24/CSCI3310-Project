package model;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class ChatRoom {
    private String user1;
    private String user2;
    private DocumentReference otherUser;
    private List<DocumentReference> chats;

    public ChatRoom () {
    }

    public ChatRoom(String user1, String user2, DocumentReference otherUser, List<DocumentReference> chats) {
        this.user1 = user1;
        this.user2 = user2;
        this.otherUser = otherUser;
        this.chats = chats;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public DocumentReference getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(DocumentReference otherUser) {
        this.otherUser = otherUser;
    }

    public List<DocumentReference> getChats() {
        return chats;
    }

    public void setChats(List<DocumentReference> chats) {
        this.chats = chats;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }
}
