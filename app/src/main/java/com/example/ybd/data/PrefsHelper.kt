package com.example.ybd.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PrefsHelper(context: Context) {

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private const val KEY_LAST_VIEWED_INDEX = "last_viewed_index"
        private const val KEY_UNLOCKED_ITEM_COUNT = "unlocked_item_count"
        private const val KEY_HAS_NEW_CONTENT = "has_new_content"
        private const val KEY_TIMER_RUNNING = "timer_running"
    }

    var lastViewedIndex: Int
        get() = prefs.getInt(KEY_LAST_VIEWED_INDEX, 0)
        set(value) = prefs.edit().putInt(KEY_LAST_VIEWED_INDEX, value).apply()

    var unlockedItemCount: Int
        get() = prefs.getInt(KEY_UNLOCKED_ITEM_COUNT, Repository.INITIAL_ITEM_COUNT)
        set(value) = prefs.edit().putInt(KEY_UNLOCKED_ITEM_COUNT, value).apply()

    var hasNewContent: Boolean
        get() = prefs.getBoolean(KEY_HAS_NEW_CONTENT, false)
        set(value) = prefs.edit().putBoolean(KEY_HAS_NEW_CONTENT, value).apply()

    var isTimerRunning: Boolean
        get() = prefs.getBoolean(KEY_TIMER_RUNNING, false)
        set(value) = prefs.edit().putBoolean(KEY_TIMER_RUNNING, value).apply()
}