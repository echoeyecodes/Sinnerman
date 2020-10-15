package com.example.myapplication.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.myapplication.Adapters.CommentsAdapter;
import com.example.myapplication.Interface.CommentActivityListener;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.CommentResponseBody;
import com.example.myapplication.R;
import com.example.myapplication.Utils.AuthUser;
import com.example.myapplication.ViewModel.CommentActivityViewModel;
import com.example.myapplication.ViewModel.NetworkState;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Timestamp;
import java.util.*;

public class CommentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CommentActivityListener {
private RecyclerView recyclerView;
private LinearLayout toolbar;
private ImageButton send_btn;
private TextView toolbar_title;
private CommentActivityViewModel commentActivityViewModel;
private SwipeRefreshLayout swipeRefreshLayout;
private RelativeLayout loading_layout;
private ImageButton back_button;
private CommentsAdapter adapter;
private TextInputEditText comment_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentActivityViewModel = new ViewModelProvider(this).get(CommentActivityViewModel.class);
        recyclerView = findViewById(R.id.comments_recycler_view);
        toolbar = findViewById(R.id.comments_toolbar);
        swipeRefreshLayout = findViewById(R.id.comments_refresh_layout);
        comment_field = findViewById(R.id.comment_text_input_field);
        send_btn = findViewById(R.id.comment_send_btn);
        loading_layout = findViewById(R.id.comments_loading_layout);
        toolbar_title = toolbar.findViewById(R.id.sub_activity_title);
        back_button = toolbar.findViewById(R.id.sub_activity_back_btn);
        toolbar_title.setText("Comments");

        AuthUser authUser = new AuthUser().getUser(this);
        if(authUser != null){
            Log.d("CARRR", authUser.getProfile_url());
        }

        back_button.setOnClickListener(v -> super.onBackPressed());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout.setOnRefreshListener(this);
        final DiffUtil.ItemCallback<CommentResponseBody> commentModelItemCallback = new CommentItemCallback();
        adapter = new CommentsAdapter(commentModelItemCallback, this);
        recyclerView.setAdapter(adapter);

        commentActivityViewModel.getRequest_status().observe(this, (status) ->{
                loading_layout.setVisibility(status == NetworkState.LOADING ? View.VISIBLE : View.GONE);

                if(status == NetworkState.ERROR){
                    Toast.makeText(this, commentActivityViewModel.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
        });

        commentActivityViewModel.getIsRefreshing().observe(this, (isRefreshing) ->{
            swipeRefreshLayout.setRefreshing(isRefreshing);
        });

        commentActivityViewModel.getPersistedComments().observe(this, (persistedComments) ->{
            adapter.submitList(persistedComments);
        });

        commentActivityViewModel.setVideo_id(getIntent().getStringExtra("video_id"));

        send_btn.setOnClickListener(v ->{
            sendComment();
        });
    }

    private void sendComment() {
        String message = Objects.requireNonNull(comment_field.getText()).toString();
        if(!message.isEmpty()){
            long time = new Date().getTime();
            CommentModel commentModel = new CommentModel(UUID.randomUUID().toString(), Objects.requireNonNull(comment_field.getText()).toString(), new Timestamp(time).toString());
            commentModel.setStatus(1);
            commentActivityViewModel.persistComment(commentModel);
            comment_field.setText("");
        }
    }


    @Override
    public void onRefresh() {
        commentActivityViewModel.onRefresh();
    }

    @Override
    public void onCommentInserted() {
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    private static class CommentItemCallback extends DiffUtil.ItemCallback<CommentResponseBody>{

        @Override
        public boolean areItemsTheSame(@NonNull CommentResponseBody oldItem, @NonNull CommentResponseBody newItem) {
            return oldItem.getComment().getId().equals(newItem.getComment().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CommentResponseBody oldItem, @NonNull CommentResponseBody newItem) {
            return oldItem.getComment().getCreatedAt().equals(newItem.getComment().getCreatedAt());
        }
    }
}
