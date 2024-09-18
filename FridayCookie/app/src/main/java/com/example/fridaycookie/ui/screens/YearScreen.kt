package com.example.fridaycookie.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.fridaycookie.network.CookieOfTheWeek
import com.example.fridaycookie.network.CookieWeek
import com.example.fridaycookie.network.CookieYear
import com.example.fridaycookie.ui.BasicView
import com.example.fridaycookie.ui.LabeledLazyColumn

@Composable
fun YearScreen(
    yearUIState: YearUIState,
    modifier: Modifier = Modifier,
    navigateToWeekScreen: (CookieWeek) -> Unit
) {
    when(yearUIState) {
        is YearUIState.Success -> YearSuccessScreen(cookieYear = yearUIState.cookieYear, modifier = modifier) {
            week: CookieWeek -> navigateToWeekScreen(week)
        }
        is YearUIState.Error -> YearErrorScreen(modifier = modifier)
    }
}

@Composable
fun YearSuccessScreen(cookieYear: CookieYear, modifier: Modifier = Modifier, navigateToWeekScreen: (CookieWeek) -> Unit) {
    BasicView(
        header = cookieYear.yearNumber.toString(),
        info = "Week",
        modifier = modifier
    ) {
        LabeledLazyColumn(
            itemList= cookieYear.cookies,
            modifier = modifier,
            labelMaker = {
                week: CookieWeek -> week.weekNumber.toString()
            }
        ) {
            week: CookieWeek -> navigateToWeekScreen(week)
        }
    }
}

@Composable
fun YearErrorScreen(modifier: Modifier = Modifier) {
    BasicView(header = "Unknown week", info = "Error") {
        Text(
            text = "Unable to select week.",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}