package com.echoeyecodes.sinnerman.Adapters

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.echoeyecodes.sinnerman.Interface.CommentActivityListener
import com.echoeyecodes.sinnerman.Models.CommentResponseBody
import com.echoeyecodes.sinnerman.Models.UserModel
import com.echoeyecodes.sinnerman.Paging.CommonListPagingViewHolder
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.Utils.AuthUserManager
import com.echoeyecodes.sinnerman.Utils.ImageColorDrawable
import com.echoeyecodes.sinnerman.Utils.Result
import com.echoeyecodes.sinnerman.Utils.TimestampConverter
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class CommentsAdapter(private val context: Context, diffCallback: DiffUtil.ItemCallback<Result<CommentResponseBody>>, var commentActivityListener: CommentActivityListener) : ListAdapter<Result<CommentResponseBody>, RecyclerView.ViewHolder>(diffCallback) {
    private val currentUser: UserModel = AuthUserManager.getInstance().getUser(context.applicationContext)

    override fun onCurrentListChanged(previousList: MutableList<Result<CommentResponseBody>>, currentList: MutableList<Result<CommentResponseBody>>) {
        super.onCurrentListChanged(previousList, currentList)
        commentActivityListener.onItemsChanged()
    }

    companion object {
        private const val LOADING_LAYOUT = 0
        private const val NORMAL_LAYOUT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == LOADING_LAYOUT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.paging_loading_layout, parent, false)
            return CommonListPagingViewHolder(view, commentActivityListener)
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_comment_item, parent, false)
        return ViewHolder(view, commentActivityListener)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Result.Loading -> LOADING_LAYOUT
            is Result.Error -> LOADING_LAYOUT
            is Result.Success<*> -> NORMAL_LAYOUT
            else -> throw Exception("Invalid network state")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        when (getItem(position)) {
            is Result.Loading -> {
                val networkStateHolder = holder as CommonListPagingViewHolder
                networkStateHolder.handleNetworkStateChanged(NetworkState.LOADING)
            }
            is Result.Error -> {
                val networkStateHolder = holder as CommonListPagingViewHolder
                networkStateHolder.handleNetworkStateChanged(NetworkState.ERROR)
            }
            is Result.Success<CommentResponseBody> -> {
                val viewHolder = holder as ViewHolder
                val commentResponseBody = (getItem(position) as Result.Success<CommentResponseBody>).data

                val (_, comment, createdAt) = commentResponseBody.comment
                val (id, _, _, _, fullname, profile_url) = commentResponseBody.user
                if (currentUser.id == id) {
                    viewHolder.cardView.setCardBackgroundColor(Color.WHITE)
                } else {
                    viewHolder.cardView.setCardBackgroundColor(Color.rgb(241, 242, 246))
                }

                viewHolder.comment_author.text = fullname
                viewHolder.comment.text = comment
                viewHolder.timestamp.text = TimestampConverter.getInstance().convertToTimeDifference(createdAt)
                Glide.with(context).load(Uri.parse(profile_url)).placeholder(ImageColorDrawable.getInstance()).into(viewHolder.comment_author_image)

            }
            else -> {
            }
        }

        //        if(commentModel.getStatus() == 1){
//            holder.progressBar.setVisibility(View.VISIBLE);
//        }else{
//            holder.progressBar.setVisibility(View.GONE);
//        }

    }

    class ViewHolder(itemView: View, commentActivityListener: CommentActivityListener?) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.comment_status_indicator)
        val comment_author: TextView = itemView.findViewById(R.id.comment_author_name)
        val comment: TextView = itemView.findViewById(R.id.comment)
        private val linearLayout: LinearLayout =itemView.findViewById(R.id.comment_item_container)
        val timestamp: TextView =itemView.findViewById(R.id.comment_timestamp)
        val cardView: CardView = itemView.findViewById(R.id.comment_background_layout)
        val comment_author_image: CircleImageView  = itemView.findViewById(R.id.comment_author_image)

    }


}