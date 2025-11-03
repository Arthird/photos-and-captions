package com.example.ybd

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.ybd.ui.MainViewModel
import com.example.ybd.ui.PhotoAdapter
import com.example.ybd.utils.NotificationHelper

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: PhotoAdapter

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askNotificationPermission()
        askExactAlarmPermission() // <-- ДОБАВЛЕНО: Запрос права на будильник
        NotificationHelper.createNotificationChannel(this)

        viewPager = findViewById(R.id.viewPager)
        adapter = PhotoAdapter(emptyList())
        viewPager.adapter = adapter

        viewModel.state.observe(this) { state ->
            adapter.updateItems(state.items)
            viewPager.setCurrentItem(state.initialIndex, false)
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.onPageChanged(position)
            }
        })

        viewModel.loadData()
    }
    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun askExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // Разрешение не выдано. Отправляем пользователя в настройки.
                Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).also {
                    startActivity(it)
                }
            }
        }
    }
}