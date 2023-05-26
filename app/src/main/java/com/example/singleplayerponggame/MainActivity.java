package com.example.singleplayerponggame;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import androidx.activity.ComponentActivity;


public class MainActivity extends ComponentActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the main layout
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Get a reference to the ball view
        View ballView = findViewById(R.id.ball_view);
    }


    }