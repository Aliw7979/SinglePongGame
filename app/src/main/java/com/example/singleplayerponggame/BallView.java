package com.example.singleplayerponggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class BallView extends View implements SensorEventListener {

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
    private static final float ALPHA =1f;
    private float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private Sensor mGyro;

    private float dxDisc = 0f;
    private float dyDisc = 0f;


    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
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

        if(discX + dxDisc > getWidth() - 1 || discX + dxDisc < -discWidth)
        {
            discX += 0;
        }
        else {
            discX += dxDisc;
        }

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

        // Draw the ball at the updated position
        canvas.drawCircle(x, y, 25f, paint);
        canvas.drawText(String.valueOf(rotationZ - rocketAngle), 20, 30, paint);
        canvas.rotate(rocketAngle-rotationZ, centerX, centerY);
        canvas.drawRect(discX, discY, discX + discWidth, discHeight + discY, paint);
        paint.setTextSize(30);

        // Redraw the view on the next frame
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mGyro , SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mSensorManager.unregisterListener(this);
    }

        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                // Apply high-pass filter to remove gravity component
                gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * event.values[0];
                gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * event.values[1];
                gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * event.values[2];

                linearAcceleration[0] = event.values[0] - gravity[0];
                linearAcceleration[1] = event.values[1] - gravity[1];
                linearAcceleration[2] = event.values[2] - gravity[2];

                // Use the filtered linear acceleration values to move the ball
                // For example:
                float xAcceleration = linearAcceleration[0];
//                float yAcceleration = linearAcceleration[1];
                if(-xAcceleration > 0 && -xAcceleration > 2)
                {
                    dxDisc = 35;
                }
                else if (-xAcceleration < 0 && -xAcceleration < -2){
                    dxDisc = -35;
                }
                else {
                    dxDisc = 0;
                }
            }
            if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
                float[] rotationMatrix = new float[9];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                float[] orientation = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientation);
                rotationZ = (float) Math.toDegrees(orientation[0]) ;
            }


            // Adjust the ball position based on the accelerometer readings
            /*dyDisc = yAcceleration * 50f;*/
        }
/*dyDisc = yAcceleration * 50f;*/
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Not used
        }
    }



