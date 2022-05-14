package model;

public class ChatList {

    private String username;
    private String lastMessage;
    private String profileImage;
    private int unseenMessages;

    public ChatList(String name, String username, String lastMessage, String profileImage, int unseenMessages) {
        this.username = username;
        this.lastMessage = lastMessage;
        this.profileImage = profileImage;
        this.unseenMessages = unseenMessages;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getProfileImage() { return profileImage; }

    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public void setUnseenMessages(int unseenMessages) {
        this.unseenMessages = unseenMessages;
    }
}
