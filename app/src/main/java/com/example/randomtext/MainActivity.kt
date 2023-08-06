package com.example.randomtext
import android.os.Bundle
import android.view.View
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.randomtext.ui.theme.RandomtextTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
            RandomTextApp()
        }
    }
}

@Composable
fun RandomTextApp() {
    var names by remember { mutableStateOf(emptyList<String>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // Start a coroutine to fetch data
        names = fetchDataFromUrl("https://gitlab.com/Jancsoj78/braindriver-ai/-/raw/main/data")
        isLoading = false
    }

    if (isLoading) {
        Text(
            text = "Loading...",
            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
            fontSize = 24.sp
        )
    } else {
        RandomText(names)
    }
}

@Composable
fun RandomText(names: List<String>) {
    var greetingText by remember { mutableStateOf("Loading...") }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Start a coroutine to update the greeting text every 3 seconds
        coroutineScope.launch {
            while (true) {
                val randomIndex = (0 until names.size).random()
                greetingText = "Hello ${names[randomIndex]}!"
                delay(3000) // 3 seconds delay
            }
        }
    }

    RandomtextTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Videó lejátszása
                AndroidView(
                    factory = { context ->
                        val videoView = VideoView(context)
                        val videoPath = "android.resource://${context.packageName}/${R.raw.background}" // Videó erőforrása
                        videoView.setVideoPath(videoPath)
                        videoView.start()
                        videoView.setOnPreparedListener { mediaPlayer ->
                            mediaPlayer.isLooping = true
                        }
                        videoView
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Szöveg megjelenítése
                Text(
                    text = greetingText,
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 10.dp), // Horizontális középre igazítás és térköz hozzáadása
                    fontSize = 70.sp,
                    color = Color.Yellow,
                    lineHeight = 80.sp // A sortáv beállítása
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val names = listOf("alma", "körte", "narancs") // Provide dummy data for preview
    RandomText(names)
}

suspend fun fetchDataFromUrl(url: String): List<String> {
    return withContext(Dispatchers.IO) {
        try {
            val response = URL(url).readText()
            response.lines()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
