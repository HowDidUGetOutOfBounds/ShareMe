package com.example.mvvmalarm.UI

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mvvmalarm.R
import com.example.mvvmalarm.ThisApplication
import com.example.mvvmalarm.Utills.ManagePermissions
import com.example.mvvmalarm.Utills.Utils.PermissionsRequestCode
import com.example.mvvmalarm.Utills.Utils.list
import com.example.mvvmalarm.databinding.ActivityMainBinding
import com.example.mvvmalarm.model.MainViewModelFactory
import com.example.mvvmalarm.repository.MainActivityViewModel
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private lateinit var managePermissions: ManagePermissions
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val mainActivityViewModel: MainActivityViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ThisApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        managePermissions = ManagePermissions(this, list, PermissionsRequestCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (managePermissions.checkPermissions()) {
                mainActivityViewModel.trackLocation()
            }
        }


    }

    private fun setupActionBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navHostFragment).navigateUp(appBarConfiguration)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val isGranted = managePermissions.processPermissionsResult(
            requestCode,
            permissions as Array<String>,
            grantResults
        )

        if (!isGranted) {
            Toast.makeText(this, "Turn on permissions for this app in settings", Toast.LENGTH_SHORT)
                .show()
            finish()
        } else {
            mainActivityViewModel.trackLocation()
        }
    }


}