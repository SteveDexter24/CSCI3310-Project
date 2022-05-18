package model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.PostRepository;

public class PostViewModel extends AndroidViewModel {

    public static PostRepository postRepository;
    public final LiveData<List<Post>> allPosts;

    public PostViewModel(@NonNull Application application){
        super(application);
        postRepository = new PostRepository(application);
        allPosts = postRepository.getAllPosts();
    }

    public static LiveData<List<Post>> getAllPosts() {return postRepository.getAllPosts();}
    public static void insertPost(Post post) {postRepository.insertPost(post);}

}
