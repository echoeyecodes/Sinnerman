package com.example.myapplication.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bumptech.glide.Glide;
import com.example.myapplication.Adapters.CommentsAdapter;
import com.example.myapplication.Interface.CommentActivityListener;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.CommentResponseBody;
import com.example.myapplication.R;
import com.example.myapplication.Utils.AuthUserManager;
import com.example.myapplication.Utils.CustomScrollListener
import com.example.myapplication.viewmodel.CommentActivityViewModel;
import com.example.myapplication.viewmodel.NetworkState
import com.google.android.material.textfield.TextInputEditText;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        toolbar = findViewById(R.id.comments_toolbar)
        swipeRefreshLayout = findViewById(R.id.comments_refresh_layout)
        profile_image = findViewById(R.id.comment_creator_image)
        comment_field = findViewById(R.id.comment_text_input_field)
        send_btn = findViewById(R.id.comment_send_btn)
        toolbar_title = toolbar.findViewById(R.id.sub_activity_title)
        back_button = toolbar.findViewById(R.id.sub_activity_back_btn)
        toolbar_title.text = "Comments"


        if (userModel != null) {
            Glide.with(this).load(Uri.parse(userModel.profile_url)).into(profile_image)
        }

        back_button.setOnClickListener { super.onBackPressed() }

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        recyclerView.layoutManager = linearLayoutManager
        swipeRefreshLayout.setOnRefreshListener(this)
        val commentModelItemCallback: DiffUtil.ItemCallback<CommentResponseBody> = CommentItemCallback();
        adapter = CommentsAdapter(commentModelItemCallback, this);
        recyclerView.adapter = adapter;


        send_btn.setOnClickListener {
            sendComment();
        };

        commentActivityViewModel.getComments().observe(this, Observer<List<CommentResponseBody>> { value ->
            adapter.submitList(value)
        })

        commentActivityViewModel.networkStatus.observe(this, Observer<NetworkState> { state ->

            if (state == NetworkState.LOADING || state == NetworkState.ERROR) {
                val originalList = ArrayList<CommentResponseBody?>(commentActivityViewModel.state)
                originalList.add(null)
                adapter.submitList(originalList)

                //necessary call to force notification update
                // due to the diffutil.callback comparison when
                //the state changes from loading to error or vice-versa
                adapter.notifyItemChanged(adapter.itemCount - 1)
                adapter.onNetworkStateChanged(state)
            }
            swipeRefreshLayout.isRefreshing = state == NetworkState.REFRESHING
        })


        recyclerView.addOnScrollListener(CommentScrollListener {
            fetchMore()
        })

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
            commentActivityViewModel.fetchMore(NetworkState.LOADING);
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
