package com.echoeyecodes.sinnerman

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.echoeyecodes.sinnerman.Activities.ActivityFragment
import com.echoeyecodes.sinnerman.Activities.ProfileActivity
import com.echoeyecodes.sinnerman.Activities.VideoActivity
import com.echoeyecodes.sinnerman.Fragments.CategoryBottomSheet
import com.echoeyecodes.sinnerman.Fragments.DrawerLayoutFragments.PrimaryFragment
import com.echoeyecodes.sinnerman.Fragments.ProgressDialogFragment
import com.echoeyecodes.sinnerman.Interface.MainActivityContext
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.Utils.AuthUserManager
import com.echoeyecodes.sinnerman.Utils.AuthenticationManager
import com.echoeyecodes.sinnerman.Utils.PreferenceManager
import com.echoeyecodes.sinnerman.viewmodel.MainActivityViewModel
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
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

class MainActivity : AppCompatActivity(), MainActivityContext, FragmentManager.OnBackStackChangedListener {
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var active_fragment: DrawerFragments? = null
    private lateinit var circleImageView: CircleImageView
    private lateinit var drawerImage: CircleImageView
    private lateinit var toolbarTitle:TextView
    private lateinit var drawerName:TextView
    private lateinit var drawerUsername:TextView
    private lateinit var toolbar: LinearLayout
    private lateinit var user_profile: ImageView
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout:DrawerLayout


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
            Glide.with(this).load(Uri.parse(userModel.profile_url)).into(drawerImage)
            toolbar.visibility = View.VISIBLE

            drawerUsername.text = "@".plus(userModel.username)
            drawerName.text = userModel.fullname

            drawerImage.setOnClickListener {
                startActivityForResult(Intent(this, ProfileActivity::class.java), 0)
                drawerLayout.closeDrawers()
            }
        }
    }

    private fun refreshUserData() {
        mainActivityViewModel.updateCurrentUser()
    }

    private fun beginActivity() {
        setContentView(R.layout.activity_main)
        initViews()
        initUserData()
        refreshUserData()
    }

    private fun initViews() {

        preferenceManager = PreferenceManager(this)
        navigationView = findViewById(R.id.side_nav_view)

        drawerLayout = findViewById(R.id.main_drawer_layout)
        val headerView = navigationView.getHeaderView(0)
        drawerImage = headerView.findViewById(R.id.drawer_profile_image)
        drawerName = headerView.findViewById(R.id.drawer_profile_name)
        drawerUsername = headerView.findViewById(R.id.drawer_profile_username)
        user_profile = findViewById(R.id.user_profile_btn)
        toolbar = findViewById(R.id.toolbar)
        toolbarTitle = findViewById(R.id.toolbar_title)

        supportFragmentManager.addOnBackStackChangedListener(this)

        user_profile.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        navigationView.setNavigationItemSelectedListener {
                navigateToNavigationOptions(it.itemId)
                it.isChecked = true
                true
        }

        val fragment = PrimaryFragment.getInstance()
        openFragment(fragment)
    }

    private fun navigateToNavigationOptions(id:Int){
        val fragment: DrawerFragments
        when(id){
            R.id.home_activity -> {
                fragment = PrimaryFragment.getInstance()
                if(active_fragment?.TAG != fragment.TAG)
                openFragment(fragment)
            }
            R.id.history -> {
                fragment = ActivityFragment("history")
                if(active_fragment?.TAG != fragment.TAG)
                openFragment(fragment)
            }
            R.id.favorites -> {
                fragment = ActivityFragment("likes")
                if(active_fragment?.TAG != fragment.TAG)
                    openFragment(fragment)
            }
            R.id.watch_later -> {
                fragment = ActivityFragment("later")
                if(active_fragment?.TAG != fragment.TAG)
                    openFragment(fragment)
            }
            else -> { }
        }

        drawerLayout.closeDrawers()
    }

    override fun onResume() {
        super.onResume()
        showSystemUI()
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    override fun openFragment(fragment: DrawerFragments) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.drawer_fragment_container, fragment, fragment.TAG)
        active_fragment = fragment

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
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
        Snackbar.make(drawerLayout, "Video link copied to clipboard", Snackbar.LENGTH_LONG).show()
    }

    private fun openShareIntent(link: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, link)
        intent.type = "text/plain"
        startActivity(intent)
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawers()
            return
        }
        if (supportFragmentManager.backStackEntryCount == 2 && active_fragment is PrimaryFragment) {
            finish()
        }else{
            super.onBackPressed()
        }
    }

    override fun onBackStackChanged() {
        val fragments = supportFragmentManager.fragments
        val fragment = fragments[fragments.size - 1] as CustomFragment
        if (fragment is DrawerFragments) {
            active_fragment = fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            refreshUserData()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onDrawerFragmentActive(fragment: DrawerFragments) {
        when(fragment){
            is PrimaryFragment -> toolbarTitle.text = "SinnermanTV"
            is ActivityFragment -> {
            when (fragment.activityContext) {
                "likes" -> {
                    toolbarTitle.text="Liked Videos"
                }
                "history" -> {
                    toolbarTitle.text="Watch History"
                }
                "later" -> {
                    toolbarTitle.text="Watch Later"
                }
            }
        }
        }
    }

}