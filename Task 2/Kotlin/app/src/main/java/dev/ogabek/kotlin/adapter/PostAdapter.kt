package dev.ogabek.kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import dev.ogabek.kotlin.R
import dev.ogabek.kotlin.activity.MainActivity
import dev.ogabek.kotlin.model.Post

class PostAdapter(var activity: MainActivity, var items: ArrayList<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onEditClick: ((post: Post) -> Unit)? = null

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_list, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post: Post = items[position]
        if (holder is PostViewHolder) {
            holder.apply {
                if (itemView.scaleX != 0f) {
                    itemView.scrollTo(0, 0)
                }
                tv_title.text = post.title!!
                tv_body.text = post.body
                edit.setOnClickListener {
                    onEditClick?.invoke(post)
                }
            }
        }
    }

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_title: TextView = view.findViewById(R.id.tv_title)
        val tv_body: TextView = view.findViewById(R.id.tv_body)
        val edit: ImageView = view.findViewById(R.id.edit)
    }
}