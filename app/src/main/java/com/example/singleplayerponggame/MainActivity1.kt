package com.example.singleplayerponggame

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.singleplayerponggame.ui.theme.SinglePlayerPongGameTheme

class MainActivity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// Set the content view to the main layout
        setContentView(R.layout.activity_main)

        // Get a reference to the ball view
        val ball = findViewById<View>(R.id.ball_view) as BallView
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SinglePlayerPongGameTheme {
        Greeting("Android")
    }
}