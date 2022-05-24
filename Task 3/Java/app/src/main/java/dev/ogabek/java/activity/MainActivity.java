package dev.ogabek.java.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import dev.ogabek.java.R;
import dev.ogabek.java.adapter.PostAdapter;
import dev.ogabek.java.manager.AuthManager;
import dev.ogabek.java.manager.DatabaseHandler;
import dev.ogabek.java.manager.DatabaseManager;
import dev.ogabek.java.model.Post;

public class MainActivity extends BaseActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    private void initViews() {
        findViewById(R.id.iv_logout).setOnClickListener(view -> {
            AuthManager.signOut();
            callSignInActivity();
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        ImageView iv_logout = findViewById(R.id.iv_logout);
        iv_logout.setOnClickListener(view -> {
            AuthManager.signOut();
            callSignInActivity();
        });

        FloatingActionButton fab_create = findViewById(R.id.fab_create);
        fab_create.setOnClickListener(view -> callCreateActivity());

        apiLoadPosts();

    }

    private void apiLoadPosts() {
        showLoading(this);
        DatabaseManager.apiLoadPosts(new DatabaseHandler() {

            @Override
            public void onSuccess(ArrayList<Post> posts) {
                dismissLoading();
                refreshAdapter(posts);
            }

            @Override
            public void onSuccess() {
                dismissLoading();
            }

            @Override
            public void onError() {
                dismissLoading();
            }
        });
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            // Load all posts...
            apiLoadPosts();
        }
    });

    private void callCreateActivity() {
        Intent intent = new Intent(this, CreateActivity.class);
        resultLauncher.launch(intent);
    }

    void refreshAdapter(ArrayList<Post> posts) {
        PostAdapter adapter = new PostAdapter(this, posts);
        recyclerView.setAdapter(adapter);
    }

}