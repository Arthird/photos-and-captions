package com.example.ybd.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.ybd.data.PrefsHelper
import com.example.ybd.data.Repository
import com.example.ybd.utils.NotificationHelper

class UnlockReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        // При создании ресивера context не null
        val safeContext = context ?: return
        val prefs = PrefsHelper(safeContext)

        // Проверяем, действительно ли таймер был запущен
        if (!prefs.isTimerRunning) return

        // Логика, которая раньше была в UnlockWorker
        val currentCount = prefs.unlockedItemCount
        val maxCount = Repository.getMaxItemCount()

        if (currentCount < maxCount) {
            val newCount = (currentCount + Repository.NEW_ITEMS_PER_BATCH)
                .coerceAtMost(maxCount)

            prefs.unlockedItemCount = newCount
            prefs.hasNewContent = true

            // Отправляем уведомление
            NotificationHelper.sendNewContentNotification(safeContext)
        }

        // В любом случае сбрасываем флаг таймера
        prefs.isTimerRunning = false
    }
}