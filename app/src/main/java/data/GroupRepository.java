package data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import model.Group;
import util.EcoSellRoomDatabase;

public class GroupRepository {
    private GroupDao groupDao;
    private LiveData<List<Group>> allGroups;
    private LiveData<List<Group>> searchGroups;
    private Group group;

    public GroupRepository(Application application) {
        EcoSellRoomDatabase db = EcoSellRoomDatabase.getDatabase(application);
        groupDao = db.groupDao();
        allGroups = groupDao.getAllGroups();
    }

    public LiveData<List<Group>> getAllGroups() {
        return allGroups;
    }

    public LiveData<List<Group>> searchGroups(String query){
        EcoSellRoomDatabase.databaseWriteExecutor.execute(() -> {
            searchGroups = groupDao.searchGroups(query);
        });
        return searchGroups;
    }

    public void insertGroup(Group group){
        EcoSellRoomDatabase.databaseWriteExecutor.execute(() -> {
            groupDao.insertGroup(group);
        });
    }

    public Group getGroup(String groupId){
        EcoSellRoomDatabase.databaseWriteExecutor.execute(() -> {
            group = groupDao.getGroup(groupId);
        });
        return group;
    }
}
