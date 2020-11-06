package com.echoeyecodes.sinnerman.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.echoeyecodes.sinnerman.Interface.MainActivityContext
import com.echoeyecodes.sinnerman.Interface.NotificationFragmentListener
import com.echoeyecodes.sinnerman.Models.UploadNotificationModel
import com.echoeyecodes.sinnerman.Paging.CommonListPagingViewHolder
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.Utils.Result
import com.echoeyecodes.sinnerman.Utils.TimestampConverter.Companion.getInstance
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class NotificationsAdapter(private val context: Context, diffCallback: DiffUtil.ItemCallback<Result<UploadNotificationModel>>, private val mainActivityContext: MainActivityContext, private val notificationFragmentListener: NotificationFragmentListener) : ListAdapter<Result<UploadNotificationModel>, RecyclerView.ViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == ExploreAdapter.LOADING_LAYOUT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.paging_loading_layout, parent, false);
            return CommonListPagingViewHolder(view, notificationFragmentListener)
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_notification_item, parent, false)
        return RecentsViewHolder(view)
    }

    override fun onCurrentListChanged(previousList: MutableList<Result<UploadNotificationModel>>, currentList: MutableList<Result<UploadNotificationModel>>) {
        super.onCurrentListChanged(previousList, currentList)
        notificationFragmentListener.onItemsChanged()
    }

    companion object{
        const val LOADING_LAYOUT = 0
        const val NORMAL_LAYOUT = 1
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
            is Result.Success<UploadNotificationModel> -> {
                val viewHolder = holder as RecentsViewHolder
                val uploadNotificationModel = (getItem(position) as Result.Success<UploadNotificationModel>).data

                Glide.with(context).load(Uri.parse(uploadNotificationModel.thumbnail)).into(viewHolder.image_thumbnail)
                Glide.with(context).load(Uri.parse(uploadNotificationModel.profile_url)).into(viewHolder.circleImageView)
                viewHolder.message.text = uploadNotificationModel.message
                viewHolder.timestamp.text = getInstance().convertToTimeDifference(uploadNotificationModel.timestamp)
                viewHolder.linearLayout.setOnClickListener { v: View? -> mainActivityContext.navigateToVideos(uploadNotificationModel.video_id) }

            }
            else -> {
            }
        }

    }

    inner class RecentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image_thumbnail: ImageView = itemView.findViewById(R.id.notification_thumbnail)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.notification_item)
        val timestamp: TextView = itemView.findViewById(R.id.notification_timestamp)
        val message: TextView =itemView.findViewById(R.id.notification_message)
        val circleImageView: CircleImageView =itemView.findViewById(R.id.notification_author)

    }

}