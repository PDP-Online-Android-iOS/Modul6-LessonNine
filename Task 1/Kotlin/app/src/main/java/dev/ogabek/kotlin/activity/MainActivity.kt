package dev.ogabek.kotlin.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.ogabek.kotlin.R
import dev.ogabek.kotlin.adapter.PostAdapter
import dev.ogabek.kotlin.manager.AuthManager
import dev.ogabek.kotlin.manager.DatabaseHandler
import dev.ogabek.kotlin.manager.DatabaseManager
import dev.ogabek.kotlin.model.Post
import dev.ogabek.kotlin.utils.Extensions.toast

class MainActivity : BaseActivity() {
    lateinit var recyclerView: RecyclerView

    var postList: ArrayList<Post> = ArrayList()
    var adapter = PostAdapter(this, postList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

    }

    private fun initViews() {
        findViewById<ImageView>(R.id.iv_logout).setOnClickListener {
            AuthManager.signOut()
            callSignInActivity()
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.adapter = adapter
        adapter.onDeleteClick = { post ->
            DatabaseManager.apiDeletePost(post, object: DatabaseHandler {
                override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                    Log.d("FirebaseDatabase", "apiDeletePost : $post / $posts")
                    apiLoadPosts()
                }

                override fun onError() {
                    toast("Something went wrong when deleting")
                }
            })
        }

        val iv_logout = findViewById<ImageView>(R.id.iv_logout)
        iv_logout.setOnClickListener {
            AuthManager.signOut()
            callSignInActivity()
        }

        val fab_create = findViewById<FloatingActionButton>(R.id.fab_create)
        fab_create.setOnClickListener {
            callCreateActivity()
        }

        apiLoadPosts()

        setItemTouchHelper()

    }

    private fun apiLoadPosts() {
        showLoading(this)
        DatabaseManager.apiLoadPosts(object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                dismissLoading()
                postList.clear()
                postList.addAll(posts)
                adapter.notifyDataSetChanged()
            }

            override fun onError() {
                dismissLoading()
            }
        })
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            // Load all posts...
            apiLoadPosts()
        }
    }

    private fun callCreateActivity() {
        val intent = Intent(this, CreateActivity::class.java)
        resultLauncher.launch(intent)
    }

    private fun setItemTouchHelper() {

        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            private val limitScrollX = dpToPx(100f, this@MainActivity)
            private var currentScrollX = 0
            private var currentScrollWhenInActive = 0
            private var initWhenInActive = 0f
            private var firstInActive = false

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }


            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX == 0f) {
                        currentScrollX = viewHolder.itemView.scrollX
                    }

                    if (isCurrentlyActive) {
                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if (scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX
                        } else if (scrollOffset < 0) {
                            scrollOffset = 0
                        }

                        viewHolder.itemView.scrollTo(scrollOffset, 0)
                    } else {
                        //slide with auto anim
                        if (firstInActive) {
                            firstInActive = false
                            currentScrollWhenInActive = viewHolder.itemView.scrollX
                            initWhenInActive = dX
                        }

                        if (viewHolder.itemView.scrollX < limitScrollX) {
                            viewHolder.itemView.scrollTo(
                                (currentScrollWhenInActive * dX / initWhenInActive).toInt(),
                                0
                            )
                        }
                    }
                }
            }
            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                if (viewHolder.itemView.scrollX > limitScrollX) {
                    viewHolder.itemView.scrollTo(limitScrollX, 0)
                } else if (viewHolder.itemView.scrollX < 0) {
                    viewHolder.itemView.scrollTo(0, 0)
                }
            }

        }).apply {
            attachToRecyclerView(recyclerView)
        }
    }

    private fun dpToPx(dpValue: Float, context: Context): Int {
        return (dpValue * context.resources.displayMetrics.density).toInt()
    }

}