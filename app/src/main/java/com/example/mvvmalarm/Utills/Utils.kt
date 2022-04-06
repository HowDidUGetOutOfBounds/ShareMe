package com.example.mvvmalarm.Utills

import android.Manifest
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    val PermissionsRequestCode = 123

    val list = listOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    fun getDate(): String? {
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        return sdf.format(Date())
    }

    fun shareMe(geoData: String): Intent {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, "Here is my location")
            putExtra(Intent.EXTRA_TEXT, geoData)
            type = "text/plain"
        }
        return sendIntent
    }

    const val POINT_FROM_MARKER_VALUE = 1
    const val POINT_FROM_LOCATION_VALUE = 0
    const val POINT_FROM_MARKER = "com.example.mvvmalarm.Utills.point_from_marker"

    const val POINT_MESSAGE = "googleMapsPointMessage"

    const val GALLERY_IMAGE_REQ_CODE = 102
}