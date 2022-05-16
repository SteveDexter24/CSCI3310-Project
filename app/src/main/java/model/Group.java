package model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class Group {

    private Timestamp createdTime;
    private String groupName;
    private String groupDescription;
    private DocumentReference creator;
    private List<DocumentReference> users;

    public Group(Timestamp createdtime, String groupName, String groupDescription, DocumentReference creator, List<DocumentReference> users){
        this.createdTime = createdtime;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.creator = creator;
        this.users = users;
    }

    public Timestamp getCreatedTime() { return createdTime; }
    public void setCreatedTime(Timestamp createdTime) { this.createdTime = createdTime; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getGroupDescription() { return groupDescription; }
    public void setGroupDescription(String groupDescription) { this.groupDescription = groupDescription; }

    public DocumentReference getCreator() { return creator; }
    public void setCreator(DocumentReference creator) { this.creator = creator; }

    public List<DocumentReference> getUsers() { return users; }
    public void setUsers(List<DocumentReference> users) { this.users = users; }
}
