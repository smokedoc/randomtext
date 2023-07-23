package com.example.randomtext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randomtext.ui.theme.RandomtextTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomTextApp()
        }
    }
}

@Composable
fun RandomTextApp() {
    RandomText()
}

@Composable
fun RandomText() {
    val names = listOf("alma", "körte", "narancs")
    var greetingIndex by remember { mutableStateOf(0) }
    var count by remember { mutableStateOf(0) }
    var greetingText by remember { mutableStateOf("Hello ${names[greetingIndex]}! ($count)") }

    val coroutineScope = rememberCoroutineScope()

    // Start a coroutine to update the greeting text every 3 seconds
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            while (true) {
                delay(3000) // 3 seconds delay
                count++
                greetingIndex = (greetingIndex + 1) % names.size // Ciklikusan változtatjuk az indexet
                greetingText = "Hello ${names[greetingIndex]}! ($count)"
            }
        }
    }

    RandomtextTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Text(
                text = greetingText,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 10.dp) // Horizontális középre igazítás és térköz hozzáadása
                    .focusable(),
                fontSize = 70.sp,
                color = Color.Black,
                lineHeight = 80.sp // A sortáv beállítása
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RandomText()
}
