package model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;

import java.util.List;

@Entity(tableName = "Post_table")
public class Post {
    @PrimaryKey
    @NonNull
    private String postId;
    private String userImageUrl;
    private String username;
    private String postImageUrl;
    private String postMessage;
    private Timestamp createdAt;
    private String groupId;
    private List<String> likes;

    public Post() {

    }

    public Post(String postId, String groupId, String userImageUrl, String username, String postImageUrl, String postMessage, Timestamp createdAt, List<String> likes) {
        this.groupId = groupId;
        this.userImageUrl = userImageUrl;
        this.username = username;
        this.postImageUrl = postImageUrl;
        this.postMessage = postMessage;
        this.createdAt = createdAt;
        this.postId = postId;
        this.likes = likes;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }
}
