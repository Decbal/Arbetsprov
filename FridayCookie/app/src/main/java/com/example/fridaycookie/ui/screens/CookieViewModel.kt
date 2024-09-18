package com.example.fridaycookie.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fridaycookie.network.FridayCookie
import com.example.fridaycookie.network.CookieAPI
import com.example.fridaycookie.network.CookieWeek
import com.example.fridaycookie.network.CookieYear
import com.example.fridaycookie.network.FactAPI
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface CookieUIState {
    data class Success(val fridayCookies: List<CookieYear>) : CookieUIState
    data object Error : CookieUIState
    data object Loading : CookieUIState
}

sealed interface YearUIState {
    data class Success(val cookieYear: CookieYear) : YearUIState
    data object Error : YearUIState
}

sealed interface WeekUIState {
    data class Success(val cookieWeek: CookieWeek) : WeekUIState
    data object Error : WeekUIState
}

class CookieViewModel: ViewModel() {

    var cookieUiState: CookieUIState by mutableStateOf(CookieUIState.Loading)
        private set

    var yearUiState: YearUIState by mutableStateOf(YearUIState.Error)
        private set

    var weekUiState: WeekUIState by mutableStateOf(WeekUIState.Error)
        private set


    init {
        getCookies()
    }

    private fun getCookies() {
        viewModelScope.launch {
            cookieUiState = try {
                //val result = CookieAPI.retrofitService.getCookies()
                val result = CookieAPI.getCookies()
                CookieUIState.Success(result)
            } catch (e: IOException) {
                CookieUIState.Error
            }
        }
    }

    fun setYear(cookieYear: CookieYear) {
        yearUiState = try {
            YearUIState.Success(cookieYear)
        } catch (e: Exception) {
            YearUIState.Error
        }
    }

    fun setWeek(cookieWeek: CookieWeek) {
        weekUiState = try {
            WeekUIState.Success(cookieWeek = cookieWeek)
        } catch (e: Exception) {
            WeekUIState.Error
        }
    }
}