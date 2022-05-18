package model;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

@Entity(tableName = "User_table")
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "userId")
    private String userId;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "mobile")
    private String mobile;

    @ColumnInfo(name = "imageUrl")
    private String imageUrl;

    @ColumnInfo(name = "products")
    private List<String> products;

    @ColumnInfo(name = "timeAdded")
    private String timeAdded;

    @ColumnInfo(name = "chatRooms")
    private List<String> chatRooms;

    private List<String> groups;

    private String qrImageUrl;

    private List<String> likes;

    public User() {
    }

    public User(String userId, String username, String email, String password, String location, String mobile, String imageUrl, List<String> products, String timeAdded, List<String> chatRooms, List<String> groups, String qrImageUrl, List<String> likes) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.location = location;
        this.mobile = mobile;
        this.imageUrl = imageUrl;
        this.products = products;
        this.timeAdded = timeAdded;
        this.chatRooms = chatRooms;
        this.groups = groups;
        this.qrImageUrl = qrImageUrl;
        this.likes = likes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getMobile() { return mobile; }

    public void setMobile(String mobile) { this.mobile = mobile; }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getProducts() { return products; }

    public void setProducts(List<String> products) { this.products = products; }

    public String getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }

    public List<String> getChatRooms() { return chatRooms; }

    public void setChatRooms(List<String> chatRooms) { this.chatRooms = chatRooms; }

    public List<String> getGroups() { return groups; }

    public void setGroups(List<String> groups) { this.groups = groups; }

    public String getQrImageUrl() { return qrImageUrl; }

    public void setQrImageUrl(String qrImageUrl) { this.qrImageUrl = qrImageUrl; }

    public List<String> getLikes() { return likes; }

    public void setLikes(List<String> likes) { this.likes = likes; }
}
