package data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import model.Group;

@Dao
public interface GroupDao {
    @Query("SELECT * FROM Group_table WHERE groupId == :groupId")
    Group getGroup(String groupId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroup(Group group);

    @Query("SELECT * FROM Group_table")
    LiveData<List<Group>> getAllGroups();

    @Query("SELECT * FROM Group_table WHERE groupName LIKE :search")
    List<Group> searchGroups(String search);

}
