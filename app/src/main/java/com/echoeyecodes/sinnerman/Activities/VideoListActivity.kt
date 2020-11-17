package com.echoeyecodes.sinnerman.Activities

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.echoeyecodes.sinnerman.Adapters.HomeFragmentRecyclerViewAdapter
import com.echoeyecodes.sinnerman.BuildConfig
import com.echoeyecodes.sinnerman.CustomFragment
import com.echoeyecodes.sinnerman.DrawerFragments
import com.echoeyecodes.sinnerman.Fragments.ProgressDialogFragment
import com.echoeyecodes.sinnerman.Interface.HomeFragmentListener
import com.echoeyecodes.sinnerman.Interface.MainActivityContext
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.Utils.*
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import com.echoeyecodes.sinnerman.viewmodel.VideoListViewModel
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters
import com.google.firebase.dynamiclinks.DynamicLink.SocialMetaTagParameters
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import java.util.*
import kotlin.collections.ArrayList

class VideoListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, MainActivityContext, HomeFragmentListener{
    private lateinit var toolbar: LinearLayout
    private lateinit var toolbar_text: TextView
    private lateinit var toolbar_back_btn: ImageButton
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeFragmentRecyclerViewAdapter
    private lateinit var viewModel: VideoListViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

        relativeLayout = findViewById(R.id.video_list_activity_root)
        toolbar = findViewById(R.id.video_list_toolbar)
        toolbar_text = toolbar.findViewById(R.id.sub_activity_title)
        toolbar_back_btn = toolbar.findViewById(R.id.sub_activity_back_btn)
        val title = intent.getStringExtra("title")
        val key = intent.getStringExtra("key")
        toolbar_text.text = title ?: ""
        toolbar_back_btn.setOnClickListener { super.onBackPressed() }

        viewModel = ViewModelProvider(this).get(VideoListViewModel::class.java)
        viewModel.tag_id = key ?: ""


        swipeRefreshLayout = findViewById(R.id.home_swipe_refresh)
        recyclerView = findViewById(R.id.video_list_recycler_view)

        swipeRefreshLayout.setOnRefreshListener(this)

        adapter = HomeFragmentRecyclerViewAdapter(SealedClassDiffUtil(), this, this)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.addItemDecoration(CustomItemDecoration(IntegerToDp.intToDp(15), IntegerToDp.intToDp(15)))

        recyclerView.adapter = adapter

        viewModel.videos.observe(this, Observer<List<VideoResponseBody>> { videos ->
            val items = videos.map { Result.Success(it) }
            adapter.submitList(items)
        })

        viewModel.networkStatus.observe(this, Observer<Result<VideoResponseBody>> { state ->
            when (state) {
                is Result.Loading -> {
                    val originalItems = ArrayList(viewModel.state.map { Result.Success(it) }) + Result.Loading
                    adapter.submitList(originalItems)
                }
                is Result.Error -> {
                    val originalItems = ArrayList(viewModel.state.map { Result.Success(it) }) + Result.Error
                    adapter.submitList(originalItems)
                }
                is Result.Refreshing -> swipeRefreshLayout.isRefreshing = true
                else -> {}
            }

            swipeRefreshLayout.isRefreshing = state == Result.Refreshing
        })

        recyclerView.addOnScrollListener(CustomScrollListener(fetchMore = this::fetchMore))
    }

    fun fetchMore(){
        viewModel.fetchMore(Result.Loading);
    }

    override fun retry(){
        viewModel.retry()
    }

    override fun onItemsChanged() {
    }


    override fun onRefresh() {
        viewModel.refresh()
    }


    override fun openFragment(fragment: DrawerFragments?, item: MenuItem?) {

    }

    override fun navigateToVideos(video_url: String?) {
        val intent = Intent(this, VideoActivity::class.java)
        intent.putExtra("video_id", video_url)
        startActivity(intent)
    }

    override fun openExternalLink(link: String) {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.setPackage("com.android.chrome")
        try {
            startActivity(i)
        } catch (e: ActivityNotFoundException) {
            // Chrome is probably not installed
            // Try with the default browser
            i.setPackage(null)
            startActivity(i)
        }
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
                }.addOnFailureListener {
                    progressDialogFragment.dismiss()
                    Toast.makeText(this, "Could not create link", Toast.LENGTH_SHORT).show()
                }
    }

    override fun onCategorySelected(position: Int) {

    }

    override fun onDrawerFragmentActive(fragments: DrawerFragments?) {

    }

    private fun copyLinkToClipboard(link: String) {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Share link", link)
        clipboardManager.setPrimaryClip(clipData)
        showSnackBarMessage()
    }

    private fun showSnackBarMessage() {
        Snackbar.make(relativeLayout, "Video link copied to clipboard", Snackbar.LENGTH_LONG).show()
    }

    private fun openShareIntent(link: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, link)
        intent.type = "text/plain"
        startActivity(intent)
    }

}