package data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.User;

@Dao
public interface UserDao {
    // CRUD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void edit(User user);

    @Query("DELETE FROM User_table")
    void deleteAll();

    @Delete
    void deleteOne(User user);

    @Query("SELECT * FROM User_table")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM User_table WHERE username LIKE :search OR email LIKE :search")
    LiveData<List<User>> searchUsers(String search);

}
