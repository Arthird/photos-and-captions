package com.example.ybd.ui

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ybd.data.PhotoItem
import com.example.ybd.data.PrefsHelper
import com.example.ybd.data.Repository
import com.example.ybd.workers.UnlockReceiver
import java.util.concurrent.TimeUnit

data class MainScreenState(
    val items: List<PhotoItem>,
    val initialIndex: Int
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = PrefsHelper(application)
    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val _state = MutableLiveData<MainScreenState>()
    val state: LiveData<MainScreenState> = _state
    private var currentItems: List<PhotoItem> = emptyList()

    fun loadData() {
        val allItems = Repository.getAllItems()
        val unlockedCount = prefs.unlockedItemCount
        val hasNew = prefs.hasNewContent

        currentItems = allItems.take(unlockedCount)

        val initialIndex: Int

        if (hasNew) {
            val oldItemCount = (unlockedCount - Repository.NEW_ITEMS_PER_BATCH)
                .coerceAtLeast(0)
            initialIndex = oldItemCount

            prefs.hasNewContent = false
        } else {
            initialIndex = prefs.lastViewedIndex.coerceAtMost(currentItems.size - 1)
        }

        _state.value = MainScreenState(currentItems, initialIndex)
    }

    fun onPageChanged(position: Int) {
        prefs.lastViewedIndex = position

        if (position == currentItems.size - 1) {
            startUnlockTimer()
        }
    }

    private fun startUnlockTimer() {
        if (prefs.isTimerRunning) return

        if (prefs.unlockedItemCount >= Repository.getMaxItemCount()) return

        prefs.isTimerRunning = true

        val triggerAtMillis = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(160) // ВРЕМЯ ТАЙМЕРА

        val intent = Intent(getApplication(), UnlockReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            0, // request code
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } catch (se: SecurityException) {
            se.printStackTrace()
            prefs.isTimerRunning = false
        }
    }
}