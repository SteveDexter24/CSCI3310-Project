package model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class Group {

    private Timestamp createdTime;
    private String groupName;
    private String groupDescription;
    private String creator;
    private String groupImageUrl;
    private List<String> users;
    private String groupId;

    public Group(){

    }

    public Group(Timestamp createdTime, String groupName, String groupDescription, String creator, String groupImageUrl, List<String> users, String groupId){
        this.createdTime = createdTime;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.creator = creator;
        this.groupImageUrl = groupImageUrl;
        this.users = users;
        this.groupId = groupId;
    }

    public Timestamp getCreatedTime() { return createdTime; }
    public void setCreatedTime(Timestamp createdTime) { this.createdTime = createdTime; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getGroupDescription() { return groupDescription; }
    public void setGroupDescription(String groupDescription) { this.groupDescription = groupDescription; }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public String getGroupImageUrl() { return groupImageUrl; }

    public void setGroupImageUrl(String groupImageUrl) { this.groupImageUrl = groupImageUrl; }

    public List<String> getUsers() { return users; }
    public void setUsers(List<String> users) { this.users = users; }

    public String getGroupId() { return groupId; }

    public void setGroupId(String groupId) { this.groupId = groupId; }
}
