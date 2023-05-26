package com.example.singleplayerponggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class BallView extends View implements  SensorEventListener {

    private SensorManager sensorManager;
    private RectF raacket;
    private Paint paint = new Paint();
    private float x = -10f; // X coordinate of the ball
    private float y = -10f; // Y coordinate of the ball
    private float dx = 10f; // Change in X coordinate per frame
    private float dy = 30f; // Change in Y coordinate per frame
    private float discX = -10f;
    private float discY = -10f;
    private float discWidth = -10f;
    private float discHeight = 15f;
    private float currentYaw = 0f; // Current angle around the z-axis (yaw)
    private TextView textView;
    private float centerX,centerY;
    private  float rotationZ;
    private float rocketAngle;
    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Get an instance of the SensorManager system service
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // Get an instance of the gyroscope sensor

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (x == -10f && y == -10f) {
            x = getWidth() / 2f;
            y = getHeight() / 3f;
            rocketAngle = rotationZ;
        }

        if (discX == -10f && discY == -10f) {
            discX = (getWidth() / 2f) - (1f / 6f) * getWidth();
            discY = (getHeight() / 4f) * 3;
            discWidth = getWidth() / 3f;
            centerY = (2*discY + discHeight) / 2;
            centerX = (2*discX + discWidth) / 2;
        }

        if (rocketAngle == 0) {
            rocketAngle = rotationZ;
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
        if (x + 50f >= discX && x - 50f <= discX + discWidth
                && y + 50 >= discY && y - 50 <= discY + discHeight) {
            // Reverse the ball's direction
            dy = -dy;
        }

        // Check for collision with walls
        if (x - 50f < 0 || x + 50f > getWidth()) {
            // Reverse the ball's direction
            dx = -dx;
        }
        // Get the current yaw angle (in degrees) around the z-axis from the gyroscope sensor
        //textView.setText((String.valueOf(currentYawDegrees)));
        // Draw the ball at the updated position
        canvas.drawCircle(x, y, 50f , paint);
        canvas.drawText(String.valueOf(rotationZ - rocketAngle), 20, 30, paint);
        canvas.rotate(rocketAngle-rotationZ, centerX, centerY);
        canvas.drawRect(discX, discY,discX + discWidth , discHeight + discY, paint);
        paint.setTextSize(30);

        // Write the text on the canvas
        String text = "Hello, world!";
        float x = 100;
        float y = 100;

        // Redraw the view on the next frame
        invalidate();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            float[] orientation = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientation);
            rotationZ = (float) Math.toDegrees(orientation[0]) ;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    public void start() {
        // Register the SensorEventListener with the SensorManager to start receiving gyroscope sensor events
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        // Unregister the SensorEventListener when you no longer need to receive gyroscope sensor events
        sensorManager.unregisterListener(this);
    }
}