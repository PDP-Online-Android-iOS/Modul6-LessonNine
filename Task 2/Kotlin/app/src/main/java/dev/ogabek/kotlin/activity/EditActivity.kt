package dev.ogabek.kotlin.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import dev.ogabek.kotlin.R
import dev.ogabek.kotlin.manager.DatabaseHandler
import dev.ogabek.kotlin.manager.DatabaseManager
import dev.ogabek.kotlin.model.Post
import dev.ogabek.kotlin.utils.Extensions.toast

class EditActivity : BaseActivity() {
    lateinit var iv_photo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        initViews()

    }

    private fun initViews() {
        val iv_close = findViewById<ImageView>(R.id.iv_close)
        val et_title = findViewById<EditText>(R.id.et_title)
        val et_body = findViewById<EditText>(R.id.et_body)
        val b_create = findViewById<Button>(R.id.b_create)

        et_title.setText(intent.getStringExtra("title"))
        et_body.setText(intent.getStringExtra("body"))
        val id = intent.getStringExtra("id")
        val img = intent.getStringExtra("img")

        iv_close.setOnClickListener {
            finish()
        }

        b_create.setOnClickListener {
            val title = et_title.text.toString().trim()
            val body = et_body.text.toString().trim()
            val post = Post(id!!, title, body)
            storeStorage(post)
        }
    }

    private fun storeStorage(post: Post) {
        showLoading(this)
        DatabaseManager.update(post, object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                Log.d("Post", "Edited")
                dismissLoading()
                finishIntent()
            }

            override fun onError() {
                dismissLoading()
                Log.d("Post", "onError: Failed")
            }

        })
    }


    fun finishIntent() {
        val returnIntent = intent
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}