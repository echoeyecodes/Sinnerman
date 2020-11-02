package com.echoeyecodes.sinnerman.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import com.echoeyecodes.sinnerman.Interface.CommentActivityListener;
import com.echoeyecodes.sinnerman.Models.CommentModel;
import com.echoeyecodes.sinnerman.Models.CommentResponseBody;
import com.echoeyecodes.sinnerman.Models.UserModel;
import com.echoeyecodes.sinnerman.Paging.CommonListPagingListeners;
import com.echoeyecodes.sinnerman.Paging.CommonListPagingViewHolder;
import com.echoeyecodes.sinnerman.R;
import com.echoeyecodes.sinnerman.Utils.AuthUserManager;
import com.echoeyecodes.sinnerman.Utils.TimestampConverter;
import com.echoeyecodes.sinnerman.viewmodel.NetworkState;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentsAdapter extends ListAdapter<CommentResponseBody, CommentsAdapter.ViewHolder> {

    CommentActivityListener commentActivityListener;
    private UserModel currentUser;
    public CommentsAdapter(Context context,  @NonNull DiffUtil.ItemCallback<CommentResponseBody> diffCallback, CommentActivityListener commentActivityListener) {
        super(diffCallback);
        this.commentActivityListener = commentActivityListener;
        currentUser = AuthUserManager.getInstance().getUser(context.getApplicationContext());
    }

    @Override
    public void onCurrentListChanged(@NonNull @NotNull List<CommentResponseBody> previousList, @NonNull @NotNull List<CommentResponseBody> currentList) {
        super.onCurrentListChanged(previousList, currentList);
        commentActivityListener.onItemsChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false);
        return new ViewHolder(view, commentActivityListener);
    }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentResponseBody commentResponseBody = getItem(position);

        if (commentResponseBody == null) {
            holder.linearLayout.setVisibility(View.GONE);
            holder.handleNetworkStateChanged(commentActivityListener.onNetworkStateChanged());
            return;
        }

        holder.linearLayout.setVisibility(View.VISIBLE);
        holder.getLoading_container().setVisibility(View.GONE);
        CommentModel commentModel = commentResponseBody.getComment();
        UserModel userModel = commentResponseBody.getUser();

        if(currentUser.getId().equals(userModel.getId())){
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }else {
            holder.cardView.setCardBackgroundColor(Color.rgb(241,242,246));
        }
//        if(commentModel.getStatus() == 1){
//            holder.progressBar.setVisibility(View.VISIBLE);
//        }else{
//            holder.progressBar.setVisibility(View.GONE);
//        }

        holder.comment_author.setText(userModel.getFullname());
        holder.comment.setText(commentModel.getComment());
        holder.timestamp.setText(TimestampConverter.Companion.getInstance().convertToTimeDifference(commentModel.getCreatedAt()));

        Picasso.get().load(Uri.parse(userModel.getProfile_url())).into(holder.comment_author_image);
    }

    public static class ViewHolder extends CommonListPagingViewHolder {

        private final ProgressBar progressBar;

        private final TextView comment_author;
        private final TextView comment;
        private final LinearLayout linearLayout;
        private final TextView timestamp;
        private final CardView cardView;
        private final CircleImageView comment_author_image;

        public ViewHolder(View itemView, CommentActivityListener commentActivityListener){
            super(itemView, commentActivityListener);

            progressBar = itemView.findViewById(R.id.comment_status_indicator);
            comment = itemView.findViewById(R.id.comment);
            linearLayout = itemView.findViewById(R.id.comment_item_container);
            timestamp = itemView.findViewById(R.id.comment_timestamp);
            cardView = itemView.findViewById(R.id.comment_background_layout);

            comment_author = itemView.findViewById(R.id.comment_author_name);
            comment_author_image = itemView.findViewById(R.id.comment_author_image);
        }

    }

}