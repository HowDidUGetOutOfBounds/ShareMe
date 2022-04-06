package com.example.mvvmalarm.model

import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmalarm.Utills.ManagePermissions
import com.example.mvvmalarm.data.PointDAO
import com.example.mvvmalarm.repository.MainActivityViewModel
import javax.inject.Inject
import javax.sql.CommonDataSource

class MainViewModelFactory @Inject constructor(
    private val locationManager: LocationManager,
    private val dataSource: PointDAO
    ):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(locationManager, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}