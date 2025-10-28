package com.example.ybd.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.ybd.data.PrefsHelper
import com.example.ybd.data.Repository
import com.example.ybd.utils.NotificationHelper

class UnlockWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val prefs = PrefsHelper(applicationContext)
        val maxItems = Repository.getMaxItemCount()
        val currentUnlocked = prefs.unlockedItemCount

        if (currentUnlocked < maxItems) {
            val newCount = (currentUnlocked + Repository.NEW_ITEMS_PER_BATCH).coerceAtMost(maxItems)
            prefs.unlockedItemCount = newCount

            prefs.hasNewContent = true

            NotificationHelper.createNotificationChannel(applicationContext) // На всякий случай
            NotificationHelper.sendNewContentNotification(applicationContext)
        }

        prefs.isTimerRunning = false

        return Result.success()
    }
}