package com.wilder.mvvmnexus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.wilder.mvvmnexus.utils.ThemeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel : ViewModel() {
    val isDarkTheme: StateFlow<Boolean> = ThemeManager.isDarkTheme

    fun toggleTheme() {
        ThemeManager.toggleTheme()
    }

    fun setDarkTheme(isDark: Boolean) {
        ThemeManager.setDarkTheme(isDark)
    }
}
