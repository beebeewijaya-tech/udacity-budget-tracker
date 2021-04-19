package com.beebee.mybudget.screens.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.beebee.mybudget.R
import com.beebee.mybudget.databinding.ActivityHomeBinding
import com.beebee.mybudget.screens.splash.SplashActivity
import com.beebee.mybudget.utils.Token
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        val viewModelFactory = HomeViewModel.HomeViewModelFactory(this.application, this)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        drawerLayout = binding.drawerLayout

        val navController = findNavController(R.id.nav_host_fragment_container)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        binding.navView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            Token.removeToken(this)
            viewModel.logout()
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
            true
        }

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)


        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_container)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.description = "Budget Tracking"
            notificationChannel.enableVibration(true)
            notificationChannel.apply {
                setShowBadge(false)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}