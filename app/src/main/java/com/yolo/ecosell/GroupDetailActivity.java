package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yolo.ecosell.adapter.PostAdapter;

import java.util.ArrayList;
import java.util.List;

import model.Group;
import model.GroupViewModel;
import model.Post;
import model.PostViewModel;

public class GroupDetailActivity extends AppCompatActivity {

    private final String TAG = "GroupDetailActivity";

    private ImageView groupImageView;
    private TextView groupNameTextView, groupDescriptionTextView, followersTextView;
    private FloatingActionButton addCommentButton;
    private Button joinButton;
    private RecyclerView postDetailRecyclerView;
    private int itemAtIndex;
    private String title, groupId;

    private GroupViewModel groupViewModel;
    private PostViewModel postViewModel;
    private Group group;

    private List<Post> postList;
    private PostAdapter postAdapter;

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Post Collection
    private CollectionReference postCollectionReference = db.collection("Posts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        Intent intent = getIntent();
        itemAtIndex = intent.getIntExtra("itemAtIndex", 0);
        title = intent.getStringExtra("title");
        groupId = intent.getStringExtra("groupId");
        getSupportActionBar().setTitle(title);

        postList = new ArrayList<>();

        groupImageView = findViewById(R.id.group_detail_item_image);
        groupNameTextView = findViewById(R.id.group_detail_item_name);
        groupDescriptionTextView = findViewById(R.id.group_detail_item_description);
        joinButton = findViewById(R.id.group_detail_btn_join);
        postDetailRecyclerView = findViewById(R.id.group_detail_recycler_view);
        followersTextView = findViewById(R.id.group_detail_followers);
        addCommentButton = findViewById(R.id.group_detail_floatingActionButton);

        postDetailRecyclerView.setHasFixedSize(true);
        postDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        groupViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(GroupViewModel.class);

        postViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(PostViewModel.class);

        groupViewModel.getAllGroups().observe(this, groups -> {
            group = groups.get(itemAtIndex);
            setUpUI();
        });

        addCommentButton.setOnClickListener(view -> {
            Intent goToCreatePostIntent = new Intent(this, CreatePostActivity.class);
            goToCreatePostIntent.putExtra("groupId", groupId);
            startActivity(goToCreatePostIntent);
        });

    }

    private void setUpUI(){
        Glide.with(this)
                .load(group.getGroupImageUrl()).into(groupImageView);
        groupNameTextView.setText(group.getGroupName());
        groupDescriptionTextView.setText(group.getGroupDescription());
        followersTextView.setText("Followers: " + group.getUsers().size());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //getChatRooms = new ArrayList<>();
        if (postList.isEmpty()){
            getPosts();
        }

    }

    private void getPosts(){
        postCollectionReference
                .whereEqualTo("groupId", groupId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot val: queryDocumentSnapshots){
                        Post post = val.toObject(Post.class);
                        //postViewModel.insertPost(post);
                        postList.add(post);
                    }
//                    Log.d(TAG, postList.size());
                    postDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    postAdapter = new PostAdapter(getApplicationContext(), postList);
                    postDetailRecyclerView.setAdapter(postAdapter);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG,"failed");
                    e.printStackTrace();
                });

    }
}