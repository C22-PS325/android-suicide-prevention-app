package com.example.suicidepreventiveapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.suicidepreventiveapp.R
import com.example.suicidepreventiveapp.data.preferences.UserModel
import com.example.suicidepreventiveapp.data.preferences.UserPreferences
import com.example.suicidepreventiveapp.data.service.AccessTokenWorker
import com.example.suicidepreventiveapp.databinding.ActivityMainBinding
import com.example.suicidepreventiveapp.ui.fragment.LoginFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mUserPreferences: UserPreferences
    private lateinit var userModel: UserModel

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workManager = WorkManager.getInstance(this)

        mUserPreferences = UserPreferences(this)
        userModel = mUserPreferences.getUser()

        if (userModel.accessToken == "") {
            val loginPage = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(loginPage)
            finish()
        } else {
            val navView: BottomNavigationView = binding.navView

//        val navController = if (userModel.accessToken == "") {
//            findNavController(R.id.loginFragment)
//        } else {
//            findNavController(R.id.nav_host_fragment_activity_main)
//        }

            val navController = findNavController(R.id.nav_host_fragment_activity_main)

//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_indicator, R.id.navigation_diary, R.id.navigation_profile
//            )
//        )
//        setupActionBarWithNavController(navController)

            navView.setupWithNavController(navController)

            periodicTask()
        }


    }

    private fun periodicTask() {
        Log.d(MainActivity::class.java.simpleName, "MASUKKASHIASHIA")
        if (userModel.refreshToken != "") {
            Log.d("MAINACITIAISHA", userModel.accessToken!!)
            val data = Data.Builder()
                .putString(AccessTokenWorker.REFRESH_TOKEN, userModel.refreshToken)
                .build()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            periodicWorkRequest = PeriodicWorkRequest.Builder(AccessTokenWorker::class.java, 29, TimeUnit.MINUTES)
                .setInputData(data)
                .setConstraints(constraints)
                .build()

            workManager.enqueue(periodicWorkRequest)
            workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
                .observe(this@MainActivity) {}

        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
    }
}