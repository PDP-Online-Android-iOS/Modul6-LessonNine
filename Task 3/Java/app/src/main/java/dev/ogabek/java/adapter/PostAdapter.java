package dev.ogabek.java.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.ogabek.java.R;
import dev.ogabek.java.model.Post;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<Post> posts;

    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post post = posts.get(position);
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).tv_title.setText(post.getTitle().toUpperCase());
            ((ItemViewHolder) holder).tv_body.setText(post.getBody());
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_body;

        public ItemViewHolder(View view) {
            super(view);

            tv_title = view.findViewById(R.id.tv_title);
            tv_body = view.findViewById(R.id.tv_body);

        }
    }
}
