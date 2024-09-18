package com.example.fridaycookie.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.fridaycookie.network.CookieWeek
import com.example.fridaycookie.network.CookieYear
import com.example.fridaycookie.network.FactAPI
import com.example.fridaycookie.ui.BasicView
import com.example.fridaycookie.ui.LabeledLazyColumn

@Composable
fun WeekScreen(
    weekUIState: WeekUIState,
    modifier: Modifier = Modifier,
) {
    when(weekUIState) {
        is WeekUIState.Success -> WeekSuccessScreen(cookieWeek = weekUIState.cookieWeek, modifier = modifier)
        is WeekUIState.Error -> WeekErrorScreen(modifier = modifier)
    }
}
@Composable
fun AsyncText(num: Int, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        text = try {
            FactAPI.getFact(num)
        } catch (e: Exception) {
            "Failed to load"
        }
    }

    Text(
        text = text,
        modifier = modifier
    )
}

@Composable
fun WeekSuccessScreen(cookieWeek: CookieWeek, modifier: Modifier = Modifier) {
    BasicView(
        header = cookieWeek.cookieOfTheWeek.name,
        info = "Week: ${cookieWeek.weekNumber}",
        modifier = modifier
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(cookieWeek.cookieOfTheWeek.url)
                    .build(),
                contentDescription = "Cookie for week ${cookieWeek.weekNumber}",
                modifier = modifier.size(500.dp)
            )
            AsyncText(cookieWeek.weekNumber, modifier = modifier)
        }
    }
}

@Composable
fun WeekErrorScreen(modifier: Modifier = Modifier) {
    BasicView(header = "Error", info = "Error") {
        Text(
            text = "Fault when loading cookie.",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}