package com.example.ybd.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.ybd.data.PhotoItem
import com.example.ybd.data.PrefsHelper
import com.example.ybd.data.Repository
import com.example.ybd.workers.UnlockWorker
import java.util.concurrent.TimeUnit

// Состояние экрана: список фоток и НА КАКУЮ прокрутить
data class MainScreenState(
    val items: List<PhotoItem>,
    val initialIndex: Int
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = PrefsHelper(application)
    private val workManager = WorkManager.getInstance(application)
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
            initialIndex = oldItemCount // Индекс первого нового элемента

            prefs.hasNewContent = false // Сбрасываем флаг
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

        val unlockWorkRequest = OneTimeWorkRequest.Builder(UnlockWorker::class.java)
            .setInitialDelay(160, TimeUnit.MINUTES) // ВРЕМЯ ТАЙМЕРА
            .build()

        workManager.enqueueUniqueWork(
            "unlock_content_timer",
            ExistingWorkPolicy.REPLACE, // Заменит старый таймер, если он был
            unlockWorkRequest
        )
    }
}