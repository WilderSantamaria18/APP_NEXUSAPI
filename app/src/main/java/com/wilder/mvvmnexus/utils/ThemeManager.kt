package com.wilder.mvvmnexus.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_IS_DARK_THEME = "is_dark_theme"
    private lateinit var prefs: SharedPreferences

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _isDarkTheme.value = prefs.getBoolean(KEY_IS_DARK_THEME, true)
    }

    fun setDarkTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        prefs.edit().putBoolean(KEY_IS_DARK_THEME, isDark).apply()
    }

    fun toggleTheme() {
        setDarkTheme(!_isDarkTheme.value)
    }
}
