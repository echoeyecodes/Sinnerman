package com.echoeyecodes.sinnerman.Activities;

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.echoeyecodes.sinnerman.Adapters.CommentsAdapter
import com.echoeyecodes.sinnerman.Interface.CommentActivityListener
import com.echoeyecodes.sinnerman.Models.CommentModel
import com.echoeyecodes.sinnerman.Models.CommentResponseBody
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.Utils.AuthUserManager
import com.echoeyecodes.sinnerman.Utils.ImageColorDrawable
import com.echoeyecodes.sinnerman.Utils.Result
import com.echoeyecodes.sinnerman.Utils.SealedClassDiffUtil
import com.echoeyecodes.sinnerman.viewmodel.CommentActivityViewModel
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class CommentActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, CommentActivityListener {
private lateinit var recyclerView :RecyclerView
private lateinit var toolbar : LinearLayout
private lateinit var send_btn : ImageButton
private lateinit var toolbar_title : TextView
private lateinit var commentActivityViewModel : CommentActivityViewModel
private lateinit var swipeRefreshLayout : SwipeRefreshLayout
private lateinit var profile_image : CircleImageView
private lateinit var back_button : ImageButton
private lateinit var adapter : CommentsAdapter
    private lateinit var empty_container: LinearLayout
private lateinit var comment_field : TextInputEditText

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val key = intent.getStringExtra("video_id") ?: ""

        commentActivityViewModel = ViewModelProvider(this).get(CommentActivityViewModel::class.java)
        commentActivityViewModel.video_id = key
        val authUserManager = AuthUserManager.getInstance()
        val userModel = authUserManager.getUser(this)
        recyclerView = findViewById(R.id.comments_recycler_view)
        empty_container = findViewById(R.id.empty_recycler_view_layout);
        toolbar = findViewById(R.id.comments_toolbar)
        swipeRefreshLayout = findViewById(R.id.comments_refresh_layout)
        profile_image = findViewById(R.id.comment_creator_image)
        comment_field = findViewById(R.id.comment_text_input_field)
        send_btn = findViewById(R.id.comment_send_btn)
        toolbar_title = toolbar.findViewById(R.id.sub_activity_title)
        back_button = toolbar.findViewById(R.id.sub_activity_back_btn)
        toolbar_title.text = "Comments"


        if (userModel != null) {
            Glide.with(this).load(Uri.parse(userModel.profile_url)).placeholder(ImageColorDrawable.Companion.getInstance()).into(profile_image)
        }

        back_button.setOnClickListener { super.onBackPressed() }

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        recyclerView.layoutManager = linearLayoutManager
        swipeRefreshLayout.setOnRefreshListener(this)
        adapter = CommentsAdapter(this, SealedClassDiffUtil(), this);
        recyclerView.adapter = adapter;


        send_btn.setOnClickListener {
            sendComment();
        };

        commentActivityViewModel.getComments().observe(this, Observer<List<CommentResponseBody>> { videos ->
            val currentState = commentActivityViewModel.networkStatus.value

            if(currentState == Result.Idle){
                val items = videos.map { Result.Success(it) }
                adapter.submitList(items)
            }
        })


        commentActivityViewModel.networkStatus.observe(this, Observer<Result<CommentResponseBody>> { state ->
            when (state) {
                is Result.Loading -> {
                    val originalItems = ArrayList(commentActivityViewModel.state.map { Result.Success(it) }) + Result.Loading
                    adapter.submitList(originalItems)
                }
                is Result.Error -> {
                    val originalItems = ArrayList(commentActivityViewModel.state.map { Result.Success(it) }) + Result.Error
                    adapter.submitList(originalItems)
                }
                is Result.Refreshing -> swipeRefreshLayout.isRefreshing = true
                else -> {}
            }

            swipeRefreshLayout.isRefreshing = state == Result.Refreshing

            if(state == Result.Idle && commentActivityViewModel.state.size == 0){
                adapter.submitList(ArrayList())
            }
        })


        recyclerView.addOnScrollListener(CommentScrollListener {
            fetchMore()
        })

    }

    private fun checkListEmpty(){
        if(adapter.itemCount == 0){
            empty_container.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }else{
            empty_container.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

        private fun sendComment() {
            val message = Objects.requireNonNull(comment_field.text).toString()
            if (message.trim().isNotEmpty()) {
                val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val time = dateTimeFormatter.format(LocalDateTime.now())
                val commentModel = CommentModel(UUID.randomUUID().toString(), Objects.requireNonNull(comment_field.text).toString(), time, 1)
                commentActivityViewModel.sendComment(commentModel)
                comment_field.setText("")
                onCommentInserted()
            }
        }

        fun fetchMore() {
            commentActivityViewModel.fetchMore(Result.Loading);
        }


        override fun onRefresh() {
            commentActivityViewModel.refresh()
        }

        override fun onCommentInserted() {
            GlobalScope.launch {
                delay(100)
                runOnUiThread{
                    recyclerView.smoothScrollToPosition(0)
                }
            }
        }

    override fun retry() {
        commentActivityViewModel.retry()
    }

    override fun onNetworkStateChanged(): NetworkState {
        return NetworkState.LOADING
    }

    override fun onItemsChanged() {
        checkListEmpty()
    }

    inner class CommentItemCallback : DiffUtil.ItemCallback<CommentResponseBody>(){

        override fun areItemsTheSame( oldItem : CommentResponseBody, newItem : CommentResponseBody) :Boolean {
            return oldItem.comment.id == newItem.comment.id
        }

        override fun areContentsTheSame(oldItem : CommentResponseBody, newItem : CommentResponseBody) : Boolean{
            return oldItem.comment == newItem.comment
        }
    }

    inner class CommentScrollListener(private val fetchMore:() -> Unit): RecyclerView.OnScrollListener(){

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == RecyclerView.SCROLL_STATE_IDLE){

                if(!recyclerView.canScrollVertically(-1)){
                    fetchMore.invoke()
                }
            }
        }

    }

}
