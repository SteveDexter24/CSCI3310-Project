package model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.UserRepository;

public class UserViewModel extends AndroidViewModel {

    public static UserRepository repository;
    public final LiveData<List<User>> allUsers;


    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
    }
    public LiveData<List<User>> getAllUsers() { return repository.getAllUsers(); }
    public static void insert(User user) { repository.insertUser(user); }
    public static User getUser(String userId) { return repository.getUser(userId); }
    public static void editUser(User user) { repository.editUser(user); }
    public static void deleteAllUsers() { repository.deleteAllUser(); }
    public static void deleteOneUser(User user) { repository.deleteOneUser(user); }
    public static LiveData<List<User>> searchUser(String search) { return repository.searchUsers(search); }

}
