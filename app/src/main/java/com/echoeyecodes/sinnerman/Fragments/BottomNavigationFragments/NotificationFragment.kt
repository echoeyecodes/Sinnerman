package com.echoeyecodes.sinnerman.Fragments.BottomNavigationFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.echoeyecodes.sinnerman.Adapters.NotificationsAdapter
import com.echoeyecodes.sinnerman.Interface.NotificationFragmentListener
import com.echoeyecodes.sinnerman.Interface.PrimaryFragmentContext
import com.echoeyecodes.sinnerman.MainActivity
import com.echoeyecodes.sinnerman.Models.UploadNotificationModel
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.RootBottomFragment
import com.echoeyecodes.sinnerman.Utils.*
import com.echoeyecodes.sinnerman.viewmodel.BottomFragmentViewModel.NotificationViewModel
import com.echoeyecodes.sinnerman.viewmodel.NetworkState


class NotificationFragment(private val primaryFragmentContext: PrimaryFragmentContext) : RootBottomFragment(), NotificationFragmentListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout:SwipeRefreshLayout
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var adapter: NotificationsAdapter
    private lateinit var empty_container: LinearLayout
     init{
         TAG ="NOTIFICATION_FRAGMENT"
     }

    companion object{
        fun newInstance(primaryFragmentContext: PrimaryFragmentContext): NotificationFragment = NotificationFragment(primaryFragmentContext)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }


    override fun onViewCreated( view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationViewModel = ViewModelProvider(requireActivity()).get(NotificationViewModel::class.java)

        recyclerView = view.findViewById(R.id.fragment_notifications_recycler_view)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        empty_container = view.findViewById(R.id.empty_recycler_view_layout);

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = NotificationsAdapter(requireContext(), SealedClassDiffUtil(), primaryFragmentContext, this)
        recyclerView.addItemDecoration( CustomItemDecoration(IntegerToDp.intToDp(10), IntegerToDp.intToDp(15)))
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener(this)

        notificationViewModel.getNotifications().observe(viewLifecycleOwner, Observer<List<UploadNotificationModel>> { notifications ->
            val currentState = notificationViewModel.networkStatus.value

            if(currentState == Result.Idle){
                val items = notifications.map { Result.Success(it) }
                adapter.submitList(items)
            }
        })

        notificationViewModel.networkStatus.observe(viewLifecycleOwner, Observer<Result<UploadNotificationModel>> { state ->
            when (state) {
                is Result.Loading -> {
                    val originalItems = ArrayList(notificationViewModel.state.map { Result.Success(it) }) + Result.Loading
                    adapter.submitList(originalItems)
                }
                is Result.Error -> {
                    val originalItems = ArrayList(notificationViewModel.state.map { Result.Success(it) }) + Result.Error
                    adapter.submitList(originalItems)
                }
                is Result.Refreshing -> swipeRefreshLayout.isRefreshing = true
                else -> {}
            }
            swipeRefreshLayout.isRefreshing = state == Result.Refreshing

            if(state == Result.Idle && notificationViewModel.state.size == 0){
                adapter.submitList(ArrayList())
            }

        })

        recyclerView.addOnScrollListener(CustomScrollListener(fetchMore = this::fetchMore))
    }

    private fun fetchMore(){
        notificationViewModel.fetchMore(Result.Loading);
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
        primaryFragmentContext.setActiveBottomViewFragment(2)
    }

    override fun retry() {
        notificationViewModel.retry()
    }

    override fun onItemsChanged() {
        checkListEmpty()
    }


    override fun onRefresh() {
        notificationViewModel.refresh()
    }
}