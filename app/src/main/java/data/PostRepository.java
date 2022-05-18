package data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import model.Post;
import util.EcoSellRoomDatabase;

public class PostRepository {
    private PostDao postDao;
    private LiveData<List<Post>> allPosts;
    private Post post;

    public PostRepository(Application application) {
        EcoSellRoomDatabase db = EcoSellRoomDatabase.getDatabase(application);
        postDao = db.postDao();
        allPosts = postDao.getAllPosts();
    }

    public LiveData<List<Post>> getAllPosts() {
        return allPosts;
    }
    public void insertPost(Post post) {
        EcoSellRoomDatabase.databaseWriteExecutor.execute(() -> {
            postDao.insertPost(post);
        });
    }

}
