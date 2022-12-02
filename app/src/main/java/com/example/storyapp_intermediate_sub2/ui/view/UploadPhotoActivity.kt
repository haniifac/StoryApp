package com.example.storyapp_intermediate_sub2.ui.view

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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp_intermediate_sub2.R
import com.example.storyapp_intermediate_sub2.data.repository.SessionManager
import com.example.storyapp_intermediate_sub2.databinding.ActivityUploadPhotoBinding
import com.example.storyapp_intermediate_sub2.ui.viewmodel.PhotoUploadViewModel
import com.example.storyapp_intermediate_sub2.util.bitmapToFile
import com.example.storyapp_intermediate_sub2.util.rotateBitmap
import com.example.storyapp_intermediate_sub2.util.uriToFile
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import java.io.File

class UploadPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadPhotoBinding
    private val uploadPhotoViewModel by viewModels<PhotoUploadViewModel>()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

    private var getFile: File? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private var lastLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        title = getString(R.string.upload_photo)
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userSession = SessionManager.getInstance(dataStore)
        uploadPhotoViewModel.putSession(userSession)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        subscribeLoading()
        subscribeUploadImage()

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
                    showGpsAlertDialog()
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
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE)
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
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this, "Failed on getting current location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }else{
            Toast.makeText(
                this, "GPS is disabled",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkGpsStatus(): Boolean {
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun startGpsIntent(){
        if (!checkGpsStatus()) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun showGpsAlertDialog(){
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage("GPS is Disabled, do you want to enable it?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Turn on GPS", DialogInterface.OnClickListener { dialog, id ->
//                finish()
                startGpsIntent()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Turn on GPS")
        // show alert dialog
        alert.show()
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun subscribeLoading() {
        uploadPhotoViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun subscribeUploadImage() {
        uploadPhotoViewModel.uploadResponse.observe(this) {
            if (!it.error) {
                showToast(getString(R.string.upload_success))
                finish()
            } else {
                showToast(getString(R.string.upload_failed))
            }
        }
    }

    private fun uploadImage() {
        val token = uploadPhotoViewModel.getToken()
        val desc = binding.addEdDesc.text.toString().trim()
        var currLocation = lastLocation

        if (desc.isBlank()) {
            binding.addEdDesc.error = getString(R.string.desc_cannot_blank)
        }else if (getFile != null) {
            uploadPhotoViewModel.uploadImage(getFile!!, desc, token, currLocation)
        }else {
            showToast(getString(R.string.choose_photo_first))
        }
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