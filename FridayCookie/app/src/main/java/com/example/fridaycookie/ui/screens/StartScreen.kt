package com.example.fridaycookie.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.fridaycookie.network.CookieYear
import com.example.fridaycookie.ui.BasicView
import com.example.fridaycookie.ui.LabeledLazyColumn
import com.example.fridaycookie.ui.theme.FridayCookieTheme

@Composable
fun StartScreen(
    cookieUIState: CookieUIState,
    modifier: Modifier = Modifier,
    navigateToYearScreen: (CookieYear) -> Unit
) {
    when (cookieUIState) {
        is CookieUIState.Loading -> LoadingScreen(modifier = modifier)
        is CookieUIState.Success ->
            SuccessScreen(cookieUIState.fridayCookies, modifier = modifier) {
                year: CookieYear ->
                navigateToYearScreen(year)
            }

        is CookieUIState.Error -> ErrorScreen(modifier = modifier)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    BasicView(
        header = "Cookie lookup",
        info = "Fetching data",
        modifier = modifier
    ) {
        Text(
            text = "Loading...",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    FridayCookieTheme {
        LoadingScreen()
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    BasicView(header = "Cookie lookup", info = "Error") {
        Text(
            text = "Unable to load data.",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SuccessScreen(cookieYears: List<CookieYear>, modifier: Modifier = Modifier, navigateToYearScreen: (CookieYear) -> Unit) {
    BasicView(
        header = "Cookie lookup",
        info = "Year",
        modifier = modifier) {
        LabeledLazyColumn(
            itemList=cookieYears,
            modifier = modifier,
            labelMaker = {
                    year: CookieYear -> year.yearNumber.toString()
            }
        ) {
            year: CookieYear -> navigateToYearScreen(year)
        }
    }
}
