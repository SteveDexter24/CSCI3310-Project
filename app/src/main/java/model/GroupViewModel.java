package model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.GroupRepository;

public class GroupViewModel extends AndroidViewModel {

    public static GroupRepository groupRepository;
    public final LiveData<List<Group>> allGroups;

    public GroupViewModel(@NonNull Application application){
        super(application);
        groupRepository = new GroupRepository(application);
        allGroups = groupRepository.getAllGroups();
    }

    public static LiveData<List<Group>> getAllGroups() {return groupRepository.getAllGroups();}
    public static List<Group> searchGroups(String query) {return groupRepository.searchGroups(query);}
    public static void insertGroup(Group group) {groupRepository.insertGroup(group);}
    public static void getGroup(String groupId) {groupRepository.getGroup(groupId);}
}
