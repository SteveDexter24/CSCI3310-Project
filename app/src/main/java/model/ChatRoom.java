package model;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class ChatRoom {
    private List<String> users;
    private List<DocumentReference> chats;
    private String chatRoomId;
    private String uniqueChatRoomIdentifier;
    private String lastMessage;

    public ChatRoom () {
    }

    public ChatRoom(List<String> users, List<DocumentReference> chats, String chatRoomId, String uniqueChatRoomIdentifier, String lastMessage) {
        this.users = users;
        this.chats = chats;
        this.chatRoomId = chatRoomId;
        this.uniqueChatRoomIdentifier = uniqueChatRoomIdentifier;
        this.lastMessage = lastMessage;
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

    public String getUniqueChatRoomIdentifier() {
        return uniqueChatRoomIdentifier;
    }

    public void setUniqueChatRoomIdentifier(String uniqueChatRoomIdentifier) {
        this.uniqueChatRoomIdentifier = uniqueChatRoomIdentifier;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
