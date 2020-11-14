package com.echoeyecodes.sinnerman

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.echoeyecodes.sinnerman.Activities.ProfileActivity
import com.echoeyecodes.sinnerman.Activities.VideoActivity
import com.echoeyecodes.sinnerman.BottomNavigationFragments.ExploreFragment
import com.echoeyecodes.sinnerman.BottomNavigationFragments.HomeFragment
import com.echoeyecodes.sinnerman.BottomNavigationFragments.NotificationFragment
import com.echoeyecodes.sinnerman.Fragments.CategoryBottomSheet
import com.echoeyecodes.sinnerman.Fragments.CategoryBottomSheet.Companion.newInstance
import com.echoeyecodes.sinnerman.Fragments.ProgressDialogFragment
import com.echoeyecodes.sinnerman.Interface.MainActivityContext
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.Utils.AuthUserManager
import com.echoeyecodes.sinnerman.Utils.AuthenticationManager
import com.echoeyecodes.sinnerman.Utils.PreferenceManager
import com.echoeyecodes.sinnerman.viewmodel.MainActivityViewModel
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters
import com.google.firebase.dynamiclinks.DynamicLink.SocialMetaTagParameters
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity(), MainActivityContext, BottomNavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var search_btn: TextView
    private lateinit var circleImageView: CircleImageView
    private lateinit var categoryBtn: ImageView
    private lateinit var toolbar: LinearLayout
    private var active_fragment: RootBottomFragment? = null
    private lateinit var user_profile: ImageView
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var frameLayout: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        val authenticationManager = AuthenticationManager()
        val token = authenticationManager.checkToken(this)
        if (token == null || token == "") {
            authenticationManager.startAuthActivity(this)
        } else {
            beginActivity()
        }
        mainActivityViewModel.isLoaded.observe(this, Observer { value: Boolean ->
            if (value) {
                initUserData()
            }
        })
    }

    private fun initUserData() {
        val userModel = AuthUserManager.getInstance().getUser(this)
        circleImageView = findViewById(R.id.user_profile_btn)
        if (userModel != null) {
            Glide.with(this).load(Uri.parse(userModel.profile_url)).into(circleImageView)
            toolbar.visibility = View.VISIBLE
        }
    }

    private fun refreshUserData() {
        mainActivityViewModel!!.updateCurrentUser()
    }

    private fun beginActivity() {
        setContentView(R.layout.activity_main)
        initViews()
        initUserData()
        refreshUserData()
    }

    private fun initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        preferenceManager = PreferenceManager(this)
        user_profile = findViewById(R.id.user_profile_btn)
        toolbar = findViewById(R.id.toolbar)
        categoryBtn = findViewById(R.id.category_btn)
        frameLayout = findViewById(R.id.main_activity_root)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        search_btn = findViewById(R.id.search_input_button)
        user_profile.setOnClickListener { startActivityForResult(Intent(this, ProfileActivity::class.java), 0) }
        supportFragmentManager.addOnBackStackChangedListener(this)
        navigateToBottomFragment(HomeFragment.newInstance())
        categoryBtn.setOnClickListener { newInstance(mainActivityViewModel.selectedPosition).show(supportFragmentManager, "CATEGORY_BOTTOM_SHEET") }


        preferenceManager.category.asLiveData().observe(this, Observer<String?> { category ->
            if (category == "gaming") {
                mainActivityViewModel.selectedPosition = 0
                categoryBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_gaming, null))
            } else if (category == "movies") {
                mainActivityViewModel.selectedPosition = 1
                categoryBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_movie, null))
            }
        })

    }

    private fun navigateToBottomFragment(fragment: RootBottomFragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val existingFragment = supportFragmentManager.findFragmentByTag(fragment.getTAG())
        if (existingFragment != null) {
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, existingFragment, fragment.getTAG())
        } else {
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, fragment, fragment.getTAG())
        }
        if (active_fragment == null || fragment.getTAG() != active_fragment!!.getTAG()) {
            fragmentTransaction.addToBackStack(null)
        }
        active_fragment = fragment
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        showSystemUI()
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
            return
        }
        super.onBackPressed()
    }

    override fun openFragment(fragment: Fragment, tag: String) {
        if (fragment is RootBottomFragment) {
            navigateToBottomFragment(fragment)
        }
    }

    override fun setActiveBottomViewFragment(position: Int) {
        bottomNavigationView!!.menu.getItem(position).isChecked = true
    }

    override fun navigateToVideos(video_url: String) {
        val intent = Intent(this, VideoActivity::class.java)
        intent.putExtra("video_id", video_url)
        startActivity(intent)
    }

    override fun onOptionSelected(video: VideoResponseBody, position: Int) {
        val progressDialogFragment = ProgressDialogFragment("Creating Link")
        progressDialogFragment.isCancelable = false
        progressDialogFragment.show(supportFragmentManager, "link_sync_fragment")
        val title = video.video.title
        val description = video.video.description
        val imageUrl = video.video.thumbnail
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.sinnerman.com/videos/" + video.video.id))
                .setDomainUriPrefix("https://sinnerman.page.link")
                .setAndroidParameters(AndroidParameters.Builder().setFallbackUrl(Uri.parse(BuildConfig.APP_REDIRECT_URL))
                        .build()).setSocialMetaTagParameters(SocialMetaTagParameters.Builder()
                        .setTitle(title).setDescription(description).setImageUrl(Uri.parse(imageUrl)).build())
                .buildShortDynamicLink().addOnCompleteListener { task: Task<ShortDynamicLink?> ->
                    if (task.isSuccessful && task.result != null) {
                        val link = Objects.requireNonNull(task.result!!.shortLink).toString()
                        if (position == 0) {
                            copyLinkToClipboard(link)
                        } else if (position == 1) {
                            openShareIntent(link)
                        }
                    } else {
                        progressDialogFragment.dismiss()
                        Toast.makeText(this, "Could not create link", Toast.LENGTH_SHORT).show()
                    }
                    progressDialogFragment.dismiss()
                }.addOnFailureListener { e: Exception? ->
                    progressDialogFragment.dismiss()
                    Toast.makeText(this, "Could not create link", Toast.LENGTH_SHORT).show()
                }
    }

    private fun restartActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onCategorySelected(position: Int) {
        val category = if(position == 0){
            "gaming"
        }else{
            "movies"
        }

        CoroutineScope(Dispatchers.IO).launch {
            preferenceManager.setPreferences(category)

            val fragment = supportFragmentManager.findFragmentByTag("CATEGORY_BOTTOM_SHEET") as CategoryBottomSheet?
            fragment?.dismiss()

            runOnUiThread { restartActivity() }
        }
    }

    private fun copyLinkToClipboard(link: String) {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Share link", link)
        clipboardManager.setPrimaryClip(clipData)
        showSnackBarMessage()
    }

    private fun showSnackBarMessage() {
        Snackbar.make(frameLayout, "Video link copied to clipboard", Snackbar.LENGTH_LONG).show()
    }

    private fun openShareIntent(link: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, link)
        intent.type = "text/plain"
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment: RootBottomFragment
        when (item.itemId) {
            R.id.action_home -> {
                fragment = HomeFragment.newInstance()
                openFragment(fragment, fragment.getTAG())
                return true
            }
            R.id.action_notifications -> {
                fragment = NotificationFragment.newInstance()
                openFragment(fragment, fragment.getTAG())
                return true
            }
            R.id.action_explore -> {
                fragment = ExploreFragment.newInstance()
                openFragment(fragment, fragment.getTAG())
                return true
            }
        }
        return false
    }

    override fun onBackStackChanged() {
        val fragments = supportFragmentManager.fragments
        val fragment = fragments[fragments.size - 1]
        if (fragment is RootBottomFragment) {
            active_fragment = fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            refreshUserData()
        }
    }

}