package model;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class ChatRoom {
    private List<String> users;
    private List<DocumentReference> usersDocumentReference;
    private List<DocumentReference> chats;
    private String chatRoomId;

    public ChatRoom () {
    }

    public ChatRoom(List<String> users, List<DocumentReference> usersDocumentReference, List<DocumentReference> chats, String chatRoomId) {
        this.users = users;
        this.chats = chats;
        this.chatRoomId = chatRoomId;
        this.usersDocumentReference = usersDocumentReference;
    }



    public List<DocumentReference> getChats() {
        return chats;
    }

    public void setChats(List<DocumentReference> chats) {
        this.chats = chats;
    }

    public List<String> getUsers() { return users; }

    public void setUsers(List<String> users) { this.users = users; }

    public String getChatRoomId() { return chatRoomId; }

    public void setChatRoomId(String chatRoomId) { this.chatRoomId = chatRoomId; }

    public List<DocumentReference> getUsersDocumentReference() { return usersDocumentReference; }

    public void setUsersDocumentReference(List<DocumentReference> usersDocumentReference) { this.usersDocumentReference = usersDocumentReference; }
}
