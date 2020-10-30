package com.example.myapplication.Activities

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.example.myapplication.Fragments.ProgressDialogFragment
import com.example.myapplication.Models.UserModel
import com.example.myapplication.R
import com.example.myapplication.Utils.AuthUserManager
import com.example.myapplication.viewmodel.NetworkState
import com.example.myapplication.viewmodel.ProfileActivityViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.layout_profile_bio.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream


class ProfileActivity : AppCompatActivity(){
    private lateinit var linearLayout: LinearLayout
    private lateinit var title: TextView
    private lateinit var back_btn: ImageButton
    private lateinit var profileImage:ImageView
    private lateinit var changePhotoBtn: ImageButton
    private lateinit var userModel: UserModel
    private lateinit var uploadBtn: MaterialButton
    private lateinit var profileActivityViewModel: ProfileActivityViewModel
    private lateinit var progressDialog : ProgressDialogFragment

    companion object {
        val UPLOAD_IMAGE_PRESET = 0
        val PROGRESS_DIALOG_FRAGMENT_TAG = "PROGRESS_DIALOG_FRAGMENT_TAG"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        linearLayout = findViewById(R.id.profile_toolbar)
        title = linearLayout.findViewById(R.id.sub_activity_title)
        back_btn = linearLayout.findViewById(R.id.sub_activity_back_btn)
        profileImage = findViewById(R.id.user_photo)
        uploadBtn = findViewById(R.id.upload_btn)
        changePhotoBtn = findViewById(R.id.choose_image_btn)

        profileActivityViewModel = ViewModelProvider(this).get(ProfileActivityViewModel::class.java)
        progressDialog = ProgressDialogFragment()
        progressDialog.isCancelable = false


        title.text = "Profile"


        back_btn.setOnClickListener { super.onBackPressed() }

        uploadBtn.setOnClickListener { uploadImage() }

        choose_image_btn.setOnClickListener { selectImage() }

        userModel = AuthUserManager.getInstance().getUser(this)

        setNewImage(userModel.profile_url)

        profileActivityViewModel.getStatus().observe(this, Observer<NetworkState> {state ->
            if(state == NetworkState.LOADING){
                progressDialog.show(supportFragmentManager, PROGRESS_DIALOG_FRAGMENT_TAG)
            }else{
                    progressDialog.dismiss()
                if(state == NetworkState.ERROR){
                    Toast.makeText(this, "An error occurred during upload", Toast.LENGTH_SHORT).show()
                }else if(state == NetworkState.SUCCESS){
                    finish()
                }
            }
        })
    }

    fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), UPLOAD_IMAGE_PRESET)
    }


    fun uploadImage() {
        val file = File(userModel.profile_url)

        val requestBody =  RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestBody)
        profileActivityViewModel.uploadPhoto(body)
    }

    private fun setNewImage(path:String){
        this.userModel.profile_url = path
        Glide.with(this).load(Uri.parse(this.userModel.profile_url)).into(profileImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == UPLOAD_IMAGE_PRESET){
            data?.let {
                Log.d("CARRR", it.data.toString())
                setNewImage(it.data.toString())
            }
        }
    }

}