package dev.ogabek.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import dev.ogabek.java.R;
import dev.ogabek.java.manager.DatabaseHandler;
import dev.ogabek.java.manager.DatabaseManager;
import dev.ogabek.java.model.Post;

public class CreateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initViews();

    }

    private void initViews() {
        ImageView iv_close = findViewById(R.id.iv_close);
        EditText et_title = findViewById(R.id.et_title);
        EditText et_body = findViewById(R.id.et_body);
        Button b_create = findViewById(R.id.b_create);

        ImageView iv_photo = findViewById(R.id.iv_photo);
        ImageView iv_camera = findViewById(R.id.iv_camera);

        iv_camera.setOnClickListener(view -> Toast.makeText(this, "Next Versions", Toast.LENGTH_SHORT).show());

        iv_close.setOnClickListener(view -> finish());
        b_create.setOnClickListener(view -> {
            String title = et_title.getText().toString().trim();
            String body = et_body.getText().toString().trim();
            Post post = new Post(title, body);
            storeStorage(post);
        });
    }

    private void storeStorage(Post post) {
        DatabaseManager.storePost(post, new DatabaseHandler() {
            @Override
            public void onSuccess(ArrayList<Post> posts) {
                Log.d("Post", "Uploaded");
                dismissLoading();
                finishIntent();
            }

            @Override
            public void onSuccess() {
                Log.d("Post", "Uploaded");
                dismissLoading();
                finishIntent();
            }

            @Override
            public void onError() {
                dismissLoading();
                Log.d("Post", "onError: Failed");
            }
        });
    }

    void finishIntent() {
        Intent returnIntent = getIntent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

}