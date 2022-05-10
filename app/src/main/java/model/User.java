package model;


import com.google.firebase.Timestamp;

public class User {
    private String userId;
    private String username;
    private String email;
    private String password;
    private String location;
    private String imageUrl;
    private Timestamp timeAdded;

    public User() {
    }

    public User(String userId, String username, String email, String password, String location, String imageUrl, Timestamp timeAdded) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.location = location;
        this.imageUrl = imageUrl;
        this.timeAdded = timeAdded;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }
}
