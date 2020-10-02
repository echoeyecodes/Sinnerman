package com.example.myapplication.Activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Adapters.CommentsAdapter;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.R;

public class CommentActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private LinearLayout toolbar;
private TextView toolbar_title;
private ImageButton back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        recyclerView = findViewById(R.id.comments_recycler_view);
        toolbar = findViewById(R.id.comments_toolbar);
        toolbar_title = toolbar.findViewById(R.id.sub_activity_title);
        back_button = toolbar.findViewById(R.id.sub_activity_back_btn);
        toolbar_title.setText("Comments");

        back_button.setOnClickListener(v -> super.onBackPressed());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final DiffUtil.ItemCallback<CommentModel> commentModelItemCallback = new CommentItemCallback();
        recyclerView.setAdapter(new CommentsAdapter(commentModelItemCallback));
    }

    private static class CommentItemCallback extends DiffUtil.ItemCallback<CommentModel>{

        @Override
        public boolean areItemsTheSame(@NonNull CommentModel oldItem, @NonNull CommentModel newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CommentModel oldItem, @NonNull CommentModel newItem) {
            return oldItem.getComment().equals(newItem.getComment());
        }
    }
}
