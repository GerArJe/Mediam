package com.example.mediam.post.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mediam.R
import com.example.mediam.databinding.ActivityPostBinding
import com.example.mediam.model.entity.Post
import com.example.mediam.model.entity.Topic
import com.example.mediam.post.viewModel.PostActivityViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PostActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: PostActivityViewModel
    private lateinit var resultGallery: ActivityResultLauncher<Intent>
    private lateinit var resultCamera: ActivityResultLauncher<Intent>
    private var photoUri: Uri? = null
    private lateinit var spTopics: Spinner
    private var topics: List<Topic> = arrayListOf()
    private var idUser: String = ""
    private var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        post = intent.getSerializableExtra("post") as Post?

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post)
        viewModel = ViewModelProvider(this)[PostActivityViewModel::class.java]
        binding.viewModel = viewModel


        post?.let {
            binding.btnSubmitPost.text = getString(R.string.txt_btn_edit_post)
            viewModel.post = it
        } ?: run {
            binding.btnSubmitPost.text = getString(R.string.txt_btn_create_post)
        }

        getUser()
        setSpinner()
        takePicture()
        onSubmit()
        back()
    }

    private fun back() {
        binding.btnBackPost.setOnClickListener {
            finish()
        }
    }

    private fun getUser() {
        val preferences =
            getSharedPreferences("store_app.pref", MODE_PRIVATE)
        idUser = preferences.getString("idUSer", "").toString()
    }

    private fun createImage(): File? {
        val timeStamp = SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ",jpg", storageDir)
    }

    private fun takePicture() {
        camera()
        gallery()
    }

    private fun gallery() {
        binding.ibGalleryPost.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.resolveActivity(packageManager)?.let {
                resultGallery.launch(galleryIntent)
            } ?: run {
                println("error galleryIntent")
            }
        }

        resultGallery =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) run {
                    photoUri = it.data!!.data!!
                    Glide.with(applicationContext).load(photoUri).into(binding.ivPost)
                }
            }
    }

    private fun camera() {
        binding.ibCameraPost.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.resolveActivity(packageManager)?.let {
                var photoFile: File? = null
                try {
                    photoFile = createImage()
                } catch (e: IOException) {

                }
                photoFile?.let {
                    photoUri = FileProvider.getUriForFile(
                        applicationContext,
                        "com.example.storeapp.fileprovider",
                        photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    resultCamera.launch(takePictureIntent)
                }
            } ?: run {
                println("error takePictureIntent")
            }
        }

        resultCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                Glide.with(applicationContext).load(photoUri).into(binding.ivPost)
            }
        }
    }


    private fun onSubmit() {
        if (idUser !== "") {
            viewModel.post.idUser = idUser
        }
        post?.let {
            update()
        } ?: run {
            add()
        }
    }

    private fun add() {
        binding.btnSubmitPost.setOnClickListener {
            if (viewModel.post.isComplete()) {
                binding.btnSubmitPost.isEnabled = false
                viewModel.add(photoUri).observe(this) { id ->
                    if (id != "") {
                        binding.btnSubmitPost.isEnabled = true
                        finish()
                    } else {
                        binding.btnSubmitPost.isEnabled = true
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.txt_error_create_post),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.txt_fields_required),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    private fun update() {
        binding.btnSubmitPost.setOnClickListener {
            if (viewModel.post.isComplete()) {
                viewModel.update(photoUri).observe(this) { state ->
                    if (state) {
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.txt_error_update_post),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.txt_fields_required),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }


    private fun setSpinner() {
        viewModel.getTopics()
        spTopics = findViewById(R.id.sp_topic_post)
        viewModel.topics.observe(this) {
            topics = it
            val topicsAdapter: ArrayAdapter<Topic> = ArrayAdapter<Topic>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                it
            )
            spTopics.adapter = topicsAdapter
            with(binding.spTopicPost) {
                onItemSelectedListener = this@PostActivity
            }
        }

    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.post.idTopic = topics[position].id
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}