package com.example.singleplayerponggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

public class BallView extends View {

    private SensorManager sensorManager;
    private Sensor acceleromoeter;
    private Sensor rotationSensor;
    private Sensor gyro;
    private Paint paint = new Paint();
    private float x = -10f; // X coordinate of the ball
    private float y = -10f; // Y coordinate of the ball
    private float dx = 0f; // Change in X coordinate per frame
    private float dy = 30f; // Change in Y coordinate per frame
    private float discX = -10f;
    private float discY = -10f;
    private float discWidth = -10f;
    private float discHeight = 25f;

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (x == -10f && y == -10f) {
            x = getWidth() / 2f;
            y = getHeight() / 3f;
        }

        if (discX == -10f && discY == -10f) {
            discX = (getWidth() / 2f) - (1f / 6f) * getWidth();
            discY = (getHeight() / 4f) * 3;
            discWidth = getWidth() / 3f;
        }

        // Set the background color to white
        canvas.drawColor(Color.WHITE);

        // Set the paint color to white
        paint.setColor(Color.BLACK);

        // Update the ball position
        x += dx;
        y += dy;

        // Check if the ball hits the edges of the view
        if (x > getWidth() - 25 || x < 25) {
            dx = -dx;
        }
        if (y > getHeight() - 25 || y < 25) {
            dy = -dy;
        }

        // Draw the ball at the updated position
        canvas.drawCircle(x, y, 25f, paint);
        canvas.drawRect(discX, discY, discX + discWidth, discHeight + discY, paint);

        // Redraw the view on the next frame
        invalidate();
    }
}