package com.example.myapplication.BottomNavigationFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.Adapters.NotificationsAdapter
import com.example.myapplication.Interface.NotificationFragmentListener
import com.example.myapplication.MainActivity
import com.example.myapplication.Models.UploadNotificationModel
import com.example.myapplication.R
import com.example.myapplication.RootBottomFragment
import com.example.myapplication.Utils.CustomItemDecoration
import com.example.myapplication.Utils.IntegerToDp
import com.example.myapplication.viewmodel.BottomFragmentViewModel.NotificationViewModel
import com.example.myapplication.viewmodel.NetworkState


class NotificationFragment : RootBottomFragment(), NotificationFragmentListener, SwipeRefreshLayout.OnRefreshListener {
    private var recyclerView: RecyclerView? = null
    private lateinit var swipeRefreshLayout:SwipeRefreshLayout
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var adapter: NotificationsAdapter
     init{
         TAG ="NOTIFICATION_FRAGMENT"
     }

    companion object{
        fun newInstance(): NotificationFragment = NotificationFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onAttach(context : Context) {
        super.onAttach(context)
        mainActivityContext = context as MainActivity
        if(mainActivityContext !is MainActivity){
            try {
                throw Exception("You need to implement Toggle Full Screen")
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onViewCreated( view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationViewModel = ViewModelProvider(requireActivity()).get(NotificationViewModel::class.java)

        recyclerView = view.findViewById(R.id.fragment_notifications_recycler_view)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        val notificationItemCallback = NotificationItemCallback()

        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = NotificationsAdapter(context, notificationItemCallback, mainActivityContext, this)
        recyclerView?.addItemDecoration( CustomItemDecoration(IntegerToDp.intToDp(10), IntegerToDp.intToDp(15)))
        recyclerView?.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener(this)

        notificationViewModel.networkStatus.observe(viewLifecycleOwner, Observer<NetworkState> { state ->
            if(state == NetworkState.LOADING || state == NetworkState.ERROR){
                val originalList = ArrayList<UploadNotificationModel?>(notificationViewModel.state)
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

        notificationViewModel.getNotifications().observe(viewLifecycleOwner, Observer<List<UploadNotificationModel>> { notification ->
            adapter.submitList(notification)
        })
    }

    inner class NotificationItemCallback : DiffUtil.ItemCallback<UploadNotificationModel>(){

        override fun areItemsTheSame( oldItem : UploadNotificationModel, newItem : UploadNotificationModel) : Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem : UploadNotificationModel, newItem : UploadNotificationModel) : Boolean{
            return oldItem.message == newItem.message
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivityContext.setActiveBottomViewFragment(2)
    }

    override fun retry() {
        notificationViewModel.retry()
    }

    override fun onRefresh() {
        notificationViewModel.refresh()
    }
}