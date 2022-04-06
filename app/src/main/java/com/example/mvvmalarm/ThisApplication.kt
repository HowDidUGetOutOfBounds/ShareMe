package com.example.mvvmalarm

import android.app.Application
import com.example.mvvmalarm.di.AppComponent
import com.example.mvvmalarm.di.DaggerAppComponent
import com.example.mvvmalarm.di.appModule

class ThisApplication : Application() {
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
       DaggerAppComponent.builder()
           .appModule(appModule(this))
           .build()
    }
}