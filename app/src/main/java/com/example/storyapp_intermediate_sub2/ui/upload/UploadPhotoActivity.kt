package com.example.storyapp_intermediate_sub2.ui.upload

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.storyapp_intermediate_sub2.R
import com.example.storyapp_intermediate_sub2.databinding.ActivityUploadPhotoBinding
import com.example.storyapp_intermediate_sub2.ui.camerax.CameraActivity
import com.example.storyapp_intermediate_sub2.ui.map.MapsActivity
import com.example.storyapp_intermediate_sub2.ui.story.StoryActivity
import com.example.storyapp_intermediate_sub2.util.ViewModelFactory
import com.example.storyapp_intermediate_sub2.util.bitmapToFile
import com.example.storyapp_intermediate_sub2.util.rotateBitmap
import com.example.storyapp_intermediate_sub2.util.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.io.File

class UploadPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadPhotoBinding
    private val uploadPhotoViewModel: PhotoUploadViewModel by viewModels { ViewModelFactory(this)}

    private var getFile: File? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private var lastLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        title = getString(R.string.upload_photo)
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.addBtnCameraX.setOnClickListener { startCameraX() }
        binding.addBtnGallery.setOnClickListener { startGallery() }
        binding.addBtnUpload.setOnClickListener { uploadImage() }
        binding.swLocation.setOnCheckedChangeListener { thisButton, isChecked ->
            if (isChecked){
                requestLocationPermission()
                if(checkGpsStatus()) {
                    getCurrentLocation()
                }else if(lastLocation == null && checkGpsStatus()){
                    thisButton.isChecked = false
                }else{
                    showToast("GPS is disabled, please enable it.")
                    thisButton.isChecked = false
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
                    getString(R.string.could_not_get_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationPermissionAllowed() : Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission(){
        // checking location permission
        if (!isLocationPermissionAllowed()) {
            // request permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
            )
        }
    }

    private fun getCurrentLocation() {
        if(checkGpsStatus()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    // getting the last known or current location
                    if(location != null){
                        val lat = location.latitude
                        val lon = location.longitude
                        Toast.makeText(
                            this, "lat= $lat, lon= $lon",
                            Toast.LENGTH_SHORT
                        ).show()
                        lastLocation = LatLng(lat,lon)
                    }else{
                        showToast("Please open map and get current location first.")
                        binding.swLocation.isChecked = false
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this, "Failed on getting current location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun checkGpsStatus(): Boolean {
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    private fun uploadImage() {
        val desc = binding.addEdDesc.text.toString().trim()
        val currLocation = lastLocation

        if (desc.isBlank()) {
            binding.addEdDesc.error = getString(R.string.desc_cannot_blank)
        }else if (getFile != null) {
            subscribeUploadImage(getFile!!, desc, currLocation)
        }else {
            showToast(getString(R.string.choose_photo_first))
        }
    }

    private fun subscribeUploadImage(getFile: File, desc: String, location : LatLng?) {
        showLoading(true)
        uploadPhotoViewModel.uploadImage(getFile, desc, location).observe(this) {
            showLoading(false)
            if (!it.error) {
                showToast(getString(R.string.upload_success))
                startStoryActivity()
            } else {
                showToast(getString(R.string.upload_failed))
            }
        }
    }

    private fun startStoryActivity(){
        startActivity(Intent(this, StoryActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean


            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile?.path),
                isBackCamera
            )

            getFile = bitmapToFile(result, this.baseContext)

            binding.addImgFeed.setImageBitmap(result)

        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@UploadPhotoActivity)

            getFile = myFile
            binding.addImgFeed.setImageURI(selectedImg)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.uploadProgressBar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val LOCATION_PERMISSION_REQ_CODE = 1000

        private const val TAG = "UploadPhotoActivity"
    }
}