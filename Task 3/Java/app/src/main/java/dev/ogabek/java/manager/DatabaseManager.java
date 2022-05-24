package dev.ogabek.java.manager;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import dev.ogabek.java.model.Post;

public class DatabaseManager {

    private static final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private static final DatabaseReference reference = database.child("posts");

    public static void storePost(Post post, DatabaseHandler handler) {
        String key;
        if (reference.push().getKey() == null) {
            return;
        } else {
            key = reference.push().getKey();
        }
        post.setId(key);
        reference.child(key).setValue(post).addOnCompleteListener(task -> {
            handler.onSuccess();
        }).addOnFailureListener(e -> {
            handler.onError();
        });
    }
    public static void apiLoadPosts(DatabaseHandler handler) {
        ArrayList<Post> posts = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot i : snapshot.getChildren()) {
                        Post post = i.getValue(Post.class);
                        if (post != null) {
                            posts.add(post);
                        }
                    }
                    handler.onSuccess(posts);
                } else {
                    handler.onSuccess(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                handler.onError();
            }
        });
    }

}
