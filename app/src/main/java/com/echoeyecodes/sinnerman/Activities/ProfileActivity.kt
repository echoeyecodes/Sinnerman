package com.echoeyecodes.sinnerman.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.echoeyecodes.sinnerman.CustomView.ProfileItem
import com.echoeyecodes.sinnerman.Fragments.ProgressDialogFragment
import com.echoeyecodes.sinnerman.MainActivity
import com.echoeyecodes.sinnerman.Models.UserModel
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.Utils.AuthUserManager
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import com.echoeyecodes.sinnerman.viewmodel.ProfileActivityViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.layout_profile_bio.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream


class ProfileActivity : AppCompatActivity(){
    private lateinit var linearLayout: LinearLayout
    private lateinit var title: TextView
    private lateinit var name: ProfileItem
    private lateinit var username: ProfileItem
    private lateinit var email: ProfileItem
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

        name = findViewById(R.id.user_profile_name)
        username = findViewById(R.id.user_profile_username)
        email = findViewById(R.id.user_profile_email)

        profileActivityViewModel = ViewModelProvider(this).get(ProfileActivityViewModel::class.java)
        progressDialog = ProgressDialogFragment("Uploading Photo")
        progressDialog.isCancelable = false


        title.text = "Profile"


        back_btn.setOnClickListener { super.onBackPressed() }

        uploadBtn.setOnClickListener { uploadImage() }

        changePhotoBtn.setOnClickListener { selectImage() }

        userModel = AuthUserManager.getInstance().getUser(this)

        initUserData(userModel)

        setNewImage(userModel.profile_url)

        profileActivityViewModel.getStatus().observe(this, Observer<NetworkState> { state ->
            if (state == NetworkState.LOADING) {
                progressDialog.show(supportFragmentManager, PROGRESS_DIALOG_FRAGMENT_TAG)
            } else {
                progressDialog.dismiss()
                if (state == NetworkState.ERROR) {
                    Toast.makeText(this, "An error occurred during upload", Toast.LENGTH_SHORT).show()
                } else if (state == NetworkState.SUCCESS) {
                    val intent = Intent(this, MainActivity::class.java)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        })
    }

    private fun initUserData(userModel: UserModel){
        username.setName(userModel.username)
        name.setName(userModel.fullname)
        email.setName(userModel.email)
    }

    fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), UPLOAD_IMAGE_PRESET)
    }

    private fun getImagePath(uri: Uri) : Boolean{
        val file = File(cacheDir, "temp_store")
        val inputStream = contentResolver.openInputStream(uri)
        if(inputStream != null){
         try{
             val outputStream = FileOutputStream(file)
             val buffer = ByteArray(1024)
             var len: Int

             len = inputStream.read(buffer)
             while(len > 0){
                 outputStream.write(buffer, 0, len)
                 len = inputStream.read(buffer)
             }
             outputStream.close()
             inputStream.close()
             return true
         }catch (exception: Exception){
             return false
         }
        }else{
            return false
        }
    }


    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun uploadImage() {
        val eval = getImagePath(Uri.parse(userModel.profile_url))
        if(eval){
            val file = File(cacheDir, "temp_store")
            val requestBody =  RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body = MultipartBody.Part.createFormData("image", file.name, requestBody)
            profileActivityViewModel.uploadPhoto(body)
        }
    }

    private fun setNewImage(path: String){
        this.userModel.profile_url = path
        Glide.with(this).load(Uri.parse(this.userModel.profile_url)).into(profileImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == UPLOAD_IMAGE_PRESET){
            if(data != null){
                uploadBtn.isEnabled = true
                Log.d("CARRR", data.data.toString())
                setNewImage(data.data.toString())
            }else{
                uploadBtn.isEnabled = false
            }
        }
    }

}