package com.example.myapplication.BottomNavigationFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapters.NotificationsAdapter
import com.example.myapplication.MainActivity
import com.example.myapplication.Models.NotificationModel
import com.example.myapplication.R
import com.example.myapplication.RootBottomFragment
import com.example.myapplication.Utils.CustomItemDecoration
import com.example.myapplication.Utils.IntegerToDp


class NotificationFragment : RootBottomFragment() {
    private var recyclerView: RecyclerView? = null

     init{
         TAG ="NOTIFICATION_FRAGMENT"
     }

    companion object{
        fun newInstance(): NotificationFragment = NotificationFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false)
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

        recyclerView = view.findViewById(R.id.fragment_notifications_recycler_view)
        val notificationItemCallback = NotificationItemCallback()

        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = NotificationsAdapter(notificationItemCallback, mainActivityContext)
        recyclerView?.addItemDecoration( CustomItemDecoration(IntegerToDp.intToDp(10), IntegerToDp.intToDp(15)))
        recyclerView?.adapter = adapter
    }

    inner class NotificationItemCallback : DiffUtil.ItemCallback<NotificationModel>(){

        override fun areItemsTheSame( oldItem : NotificationModel, newItem : NotificationModel) : Boolean {
            return oldItem.id.equals(newItem.id)
        }

        override fun areContentsTheSame(oldItem : NotificationModel, newItem : NotificationModel) : Boolean{
            return oldItem.title == newItem.title
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivityContext.setActiveBottomViewFragment(2)
    }
}