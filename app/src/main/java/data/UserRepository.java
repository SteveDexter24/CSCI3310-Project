package data;

import android.app.Application;
import android.text.style.IconMarginSpan;

import androidx.lifecycle.LiveData;

import java.util.List;

import model.User;
import util.EcoSellRoomDatabase;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private LiveData<List<User>> searchUsers;

    public UserRepository(Application application) {
        EcoSellRoomDatabase db = EcoSellRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() { return allUsers; }

    public void insertUser(User user) {
        EcoSellRoomDatabase.databaseWriteExecutor.execute(() ->{
            userDao.insert(user);
        });
    }

    public void editUser(User user) {
        EcoSellRoomDatabase.databaseWriteExecutor.execute(() ->{
            userDao.edit(user);
        });
    }

}
