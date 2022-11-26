package com.dhabasoft.androidintermediate.view.addstory

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dhabasoft.androidintermediate.BuildConfig
import com.dhabasoft.androidintermediate.R
import com.dhabasoft.androidintermediate.base.BaseActivity
import com.dhabasoft.androidintermediate.core.MyPreferences
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.databinding.ActivityAddStoryBinding
import com.dhabasoft.androidintermediate.utils.Utils
import com.dhabasoft.androidintermediate.utils.Utils.createCustomTempFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class AddStoryActivity : BaseActivity() {
    private lateinit var currentPhotoPath: String
    private lateinit var binding: ActivityAddStoryBinding
    private var file: File? = null
    private val viewModel: AddStoryViewModel by viewModels()
    private var token: String = ""
    private lateinit var myPreferences: MyPreferences
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    @SuppressLint("MissingPermission")
    private fun getLatestLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            it?.latitude?.let { latitude ->
                myPreferences.setLatitude("$latitude")
            }
            it?.longitude?.let { longitude ->
                myPreferences.setLongitude("$longitude")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MyPreferences(applicationContext)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        title = getString(R.string.add_story)

        token = myPreferences.getToken()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            getLatestLocation()
        }

        binding.btnCamera.setOnClickListener {
            startTakePhoto()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnUpload.setOnClickListener {
            addStory()
        }
    }

    private suspend fun compressImage(file: File): File {
        return Compressor.compress(applicationContext, file) {
            default(width = 640, format = Bitmap.CompressFormat.JPEG)
        }
    }

    private fun addStory() {
        if (file == null || binding.edDescription.text.toString().isBlank()) {
            Toast.makeText(applicationContext, getString(R.string.input_image_and_description), Toast.LENGTH_SHORT).show()
        } else {
            file?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    val compressedImage = compressImage(it)
                    val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        compressedImage.name,
                        compressedImage.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                    viewModel.addStory(
                        binding.edDescription.text.toString(),
                        filePart,
                        token,
                        myPreferences.getLatitude(),
                        myPreferences.getLongitude()
                    )
                        .observe(this@AddStoryActivity) { response ->
                            if (response != null) {
                                when (response) {
                                    is Resource.Loading -> {
                                        binding.progressAddStory.root.visibility = View.VISIBLE
                                    }
                                    is Resource.Success -> {
                                        binding.progressAddStory.root.visibility = View.GONE
                                        Toast.makeText(
                                            applicationContext,
                                            response.data?.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    }
                                    is Resource.Error -> {
                                        binding.progressAddStory.root.visibility = View.GONE
                                        Toast.makeText(
                                            this@AddStoryActivity,
                                            response.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                getLatestLocation()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            file = myFile
            val result = BitmapFactory.decodeFile(myFile.path)

            binding.ivAddPhoto.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            file = Utils.uriToFile(selectedImg, applicationContext)
            binding.ivAddPhoto.setImageURI(selectedImg)
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                applicationContext,
                BuildConfig.APPLICATION_ID,
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}