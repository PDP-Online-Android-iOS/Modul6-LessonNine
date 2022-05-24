package dev.ogabek.kotlin.manager

import dev.ogabek.kotlin.model.Post

interface DatabaseHandler {
    fun onSuccess(post: Post? = null, posts: ArrayList<Post> = ArrayList())
    fun onError()
}