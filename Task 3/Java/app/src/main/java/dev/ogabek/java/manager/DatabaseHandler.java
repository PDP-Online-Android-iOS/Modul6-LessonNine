package dev.ogabek.java.manager;

import java.util.ArrayList;

import dev.ogabek.java.model.Post;

public interface DatabaseHandler {

    public void onSuccess(ArrayList<Post> posts);
    public void onSuccess();
    public void onError();

}
