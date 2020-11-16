package com.echoeyecodes.sinnerman.Fragments.DrawerLayoutFragments;

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.echoeyecodes.sinnerman.*
import com.echoeyecodes.sinnerman.Fragments.BottomNavigationFragments.ExploreFragment
import com.echoeyecodes.sinnerman.Fragments.BottomNavigationFragments.HomeFragment
import com.echoeyecodes.sinnerman.Fragments.BottomNavigationFragments.NotificationFragment
import com.echoeyecodes.sinnerman.Interface.PrimaryFragmentContext
import com.google.android.material.bottomnavigation.BottomNavigationView

class PrimaryFragment : DrawerFragments(), BottomNavigationView.OnNavigationItemSelectedListener, PrimaryFragmentContext, FragmentManager.OnBackStackChangedListener{

    private lateinit var bottomNavigationView: BottomNavigationView
    private var active_fragment: RootBottomFragment? = null

    init {
        TAG = "PRIMARY_FRAGMENT"
    }

    companion object{
        fun getInstance() = PrimaryFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_primary, container, false)
    }

    override fun onResume() {
        super.onResume()
        mainActivityContext.onDrawerFragmentActive(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView = view.findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        parentFragmentManager.addOnBackStackChangedListener(this)

        if(active_fragment == null){
            navigateToBottomFragment(HomeFragment.newInstance(this))
        }else{
            navigateToBottomFragment(active_fragment!!)
        }
    }

    private fun navigateToBottomFragment(fragment: RootBottomFragment) {
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        val existingFragment = parentFragmentManager.findFragmentByTag(fragment.getTAG()) as RootBottomFragment?
        if (existingFragment != null) {
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, existingFragment, existingFragment.TAG)
        } else {
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, fragment, fragment.TAG)
        }
        if (active_fragment == null || fragment.getTAG() != active_fragment!!.getTAG()) {
            fragmentTransaction.addToBackStack(null)
        }
        active_fragment = fragment
        fragmentTransaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment: RootBottomFragment
        when (item.itemId) {
            R.id.action_home -> {
                fragment = HomeFragment.newInstance(this)
                if(active_fragment?.TAG != fragment.TAG)
                openBottomFragment(fragment, fragment.getTAG())
                return true
            }
            R.id.action_notifications -> {
                fragment = NotificationFragment.newInstance(this)
                if(active_fragment?.TAG != fragment.TAG)
                openBottomFragment(fragment, fragment.getTAG())
                return true
            }
            R.id.action_explore -> {
                fragment = ExploreFragment.newInstance(this)
                if(active_fragment?.TAG != fragment.TAG)
                openBottomFragment(fragment, fragment.getTAG())
                return true
            }
        }
        return false
    }


    override fun openBottomFragment(fragment: RootBottomFragment, tag: String) {
        navigateToBottomFragment(fragment)
    }

    override fun setActiveBottomViewFragment(position: Int) {
        bottomNavigationView.menu.getItem(position).isChecked = true
    }

    override fun navigateToVideos(video_url: String) {
        mainActivityContext.navigateToVideos(video_url)
    }

    override fun onBackStackChanged() {
        val fragments = parentFragmentManager.fragments
        val fragment = fragments[fragments.size - 1]
        if (fragment is RootBottomFragment) {
            active_fragment = fragment
        }
    }
}
