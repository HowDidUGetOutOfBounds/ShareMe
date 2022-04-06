package com.example.mvvmalarm.repository

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmalarm.data.CustomPoint
import com.example.mvvmalarm.data.PointDAO
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow


class MainActivityViewModel(
    private val locationManager: LocationManager,
    private val dataSource: PointDAO
) : ViewModel() {

    val latitude: MutableLiveData<Double> = MutableLiveData()
    val longitude: MutableLiveData<Double> = MutableLiveData()
    val isGeoInProgress: MutableLiveData<Boolean> = MutableLiveData(true)
    val imageUri: MutableLiveData<Uri> = MutableLiveData()
    var locationUpdatedEvent: Boolean = true


    @SuppressLint("MissingPermission")
    fun trackLocation() {
        try {
            // Request location updates
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        latitude.postValue(location.latitude)
                        longitude.postValue(location.longitude)
                        if (longitude.value != null && latitude.value != null) {
                            isGeoInProgress.postValue(false)
                        }
                        Log.d("myTag", "${longitude.value}:${latitude.value}")
                    }

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                })
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }
    }

    fun getGeoData(): String {
        return "http://maps.google.com/maps?q=loc:" +
                latitude.value + "," +
                longitude.value
    }

    fun getGeoData(point: CustomPoint): String {
        return "http://maps.google.com/maps?q=loc:" +
                point.lat + "," +
                point.lon
    }


    fun getAllPointsFromDB(): Flow<List<CustomPoint>> {
        return dataSource.getAllPoints()
    }

    fun getLastPoint(): CustomPoint {
        return dataSource.getLastRecord()
    }

    fun getPointByMessage(mess: String): CustomPoint {
        return dataSource.getByMessage(mess)
    }

    fun getCameraUpdate(): CameraUpdate {
        return CameraUpdateFactory
            .newLatLngZoom(
                LatLng(
                    latitude.value!!,
                    longitude.value!!
                ), 18f
            )
    }

}