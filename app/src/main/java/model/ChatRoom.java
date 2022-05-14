package model;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class ChatRoom {
    private DocumentReference user;
    private DocumentReference otherUser;
    private List<DocumentReference> chats;

    public ChatRoom(DocumentReference user, DocumentReference otherUser, List<DocumentReference> chats) {
        this.user = user;
        this.otherUser = otherUser;
        this.chats = chats;
    }

    public DocumentReference getUser() {
        return user;
    }

    public void setUser(DocumentReference user) {
        this.user = user;
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
}
