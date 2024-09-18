package com.example.fridaycookie.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fridaycookie.network.CookieWeek
import com.example.fridaycookie.network.CookieYear
import com.example.fridaycookie.ui.screens.CookieViewModel
import com.example.fridaycookie.ui.screens.StartScreen
import com.example.fridaycookie.ui.screens.WeekScreen
import com.example.fridaycookie.ui.screens.YearScreen
import com.example.fridaycookie.ui.theme.FridayCookieTheme

@Composable
fun FridayCookieApp (modifier: Modifier = Modifier) {
    val cookieViewModel: CookieViewModel = viewModel()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartScreen(cookieUIState = cookieViewModel.cookieUiState, modifier = modifier) {
                year: CookieYear -> cookieViewModel.setYear(year)
                navController.navigate("year")
            }
        }
        composable("year") {
            YearScreen(yearUIState = cookieViewModel.yearUiState, modifier = modifier) {
                week: CookieWeek -> cookieViewModel.setWeek(week)
                navController.navigate("week")
            }
        }
        composable("week") {
            WeekScreen(weekUIState = cookieViewModel.weekUiState, modifier = modifier)
        }
    }

}

@Composable
fun InfoBar(header: String, info: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(6.dp)) {
        Column (modifier = modifier) {
            Text(
                text = "$header",
                fontSize = 24.sp,
                modifier = modifier
            )
            Text(
                text = "$info",
                fontSize = 12.sp,
                modifier = modifier
            )
        }
    }
}

@Composable
fun BasicView(header: String, info: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.padding(12.dp, 48.dp)) {
        Column(modifier = modifier) {
            InfoBar(header=header, info=info, modifier)
            Box(modifier = modifier.padding(12.dp)) {
                content()
            }
        }
    }
}

@Composable
fun ListEntry(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = {
            Log.d("PRESS", "Pressed $label")
            onClick()
        },
        modifier = modifier
    ) {
        Surface (
            modifier = modifier.fillMaxWidth().background(Color.Gray).padding(3.dp)
        ) {
            Row (modifier = modifier.fillMaxWidth()) {
                Text(text = label, modifier = modifier.padding(3.dp))
                Text(text = ">", fontSize = 24.sp, textAlign = TextAlign.End, modifier = modifier.fillMaxWidth().padding(6.dp, 3.dp))
            }
        }
    }
}

@Composable
fun <T> LabeledLazyColumn(itemList: List<T>, modifier: Modifier = Modifier, labelMaker: (T) -> String, onClick: (T) -> Unit) {
    LazyColumn(modifier = modifier) {
        items(itemList) { item ->
                ListEntry(label=labelMaker(item), modifier = modifier, onClick = {onClick(item)})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasicViewPreview() {
    val list = listOf(1,2,3);
    FridayCookieTheme {
        BasicView(header = "A header", info="Some information") {
            LabeledLazyColumn(itemList = list, labelMaker = {i -> i.toString()}) {
                {}
            }
        }
    }
}