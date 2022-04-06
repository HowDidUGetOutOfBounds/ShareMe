package com.example.mvvmalarm.di

import android.app.Application
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmalarm.data.PointDAO
import com.example.mvvmalarm.data.PointDatabase
import com.example.mvvmalarm.model.MainViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class appModule(mApplication: Application) {
    private var pointDatabase: PointDatabase =
        PointDatabase.getInstance(mApplication)
    private var locationManager = mApplication.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

    @Singleton
    @Provides
    fun provideLocationManager(): LocationManager {
        return locationManager
    }


    @Singleton
    @Provides
    fun providesRoomDatabase(): PointDatabase {
        return pointDatabase
    }

    @Singleton
    @Provides
    fun providesUsersDao(pointDatabase: PointDatabase): PointDAO {
        return pointDatabase.pointDao()
    }

    @Singleton
    @Provides
    fun providesVMFactory(locationManager: LocationManager,
                          dataSource: PointDAO): MainViewModelFactory {
        return MainViewModelFactory(locationManager, dataSource)
    }
}