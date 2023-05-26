package com.example.singleplayerponggame

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.singleplayerponggame.ui.theme.SinglePlayerPongGameTheme

class MainActivity : ComponentActivity() {
    private lateinit var ballView: BallView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ballView = BallView(this, null)
        setContentView(ballView)
    }

    override fun onResume() {
        super.onResume()

        ballView.start()
    }

    override fun onPause() {
        super.onPause()

        ballView.stop()
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