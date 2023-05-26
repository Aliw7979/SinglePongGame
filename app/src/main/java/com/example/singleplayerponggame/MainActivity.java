package com.example.singleplayerponggame;

import android.os.Bundle;
import android.view.View;
import androidx.activity.ComponentActivity;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.tooling.preview.Preview;

public class MainActivity extends ComponentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the main layout
        setContentView(R.layout.activity_main);

        // Get a reference to the ball view
        View ballView = findViewById(R.id.ball_view);
    }


    }