package com.example.mvvmalarm.di

import com.example.mvvmalarm.UI.FragmentCustomization
import com.example.mvvmalarm.UI.MainActivity
import com.example.mvvmalarm.UI.MapFragment
import com.example.mvvmalarm.UI.PointsListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [appModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: MapFragment)
    fun inject(fragment: FragmentCustomization)
    fun inject(fragment: PointsListFragment)
}