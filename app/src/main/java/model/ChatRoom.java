package model;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class ChatRoom {
    private List<String> users;
    private List<DocumentReference> chats;
    private String chatRoomId;
    private String uniqueChatRoomIdentifier1;
    private String uniqueChatRoomIdentifier2;
    private String lastMessage;

    public ChatRoom () {
    }

    public ChatRoom(List<String> users, List<DocumentReference> chats, String chatRoomId, String uniqueChatRoomIdentifier1, String uniqueChatRoomIdentifier2, String lastMessage) {
        this.users = users;
        this.chats = chats;
        this.chatRoomId = chatRoomId;
        this.uniqueChatRoomIdentifier1 = uniqueChatRoomIdentifier1;
        this.uniqueChatRoomIdentifier2 = uniqueChatRoomIdentifier2;
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

    public String getUniqueChatRoomIdentifier1() {
        return uniqueChatRoomIdentifier1;
    }

    public void setUniqueChatRoomIdentifier1(String uniqueChatRoomIdentifier1) {
        this.uniqueChatRoomIdentifier1 = uniqueChatRoomIdentifier1;
    }

    public String getUniqueChatRoomIdentifier2() { return uniqueChatRoomIdentifier2; }

    public void setUniqueChatRoomIdentifier2(String uniqueChatRoomIdentifier2) { this.uniqueChatRoomIdentifier2 = uniqueChatRoomIdentifier2; }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
