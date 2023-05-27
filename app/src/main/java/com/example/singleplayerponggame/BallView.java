package com.example.singleplayerponggame;

import static java.sql.Types.NULL;

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
import android.text.method.Touch;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnTouchListener;

public class BallView extends View implements  SensorEventListener, OnTouchListener {

    private boolean touchingDisc;
    private float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];
    private SensorManager sensorManager;
    private RectF raacket;
    private Paint paint = new Paint();
    private float x ;// X coordinate of the ball
    private float y ; // Y coordinate of the ball
    private float dx ; // Change in X coordinate per frame
    private float dy ; // Change in Y coordinate per frame
    private float discX ;
    private float discY ;
    private float discWidth ;
    private float discHeight ;
    private float currentYaw ; // Current angle around the z-axis (yaw)

    private  float dxDisc;
    private TextView textView;
    private float centerX,centerY;
    private  float rotationZ;
    private float rocketAngle;
    private float xAcceleration;
    private Canvas canvas;
    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        x = -10f; // X coordinate of the ball
        y = -10f; // Y coordinate of the ball
        dx = 10f; // Change in X coordinate per frame
        dy = 30f; // Change in Y coordinate per frame
        discX = -10f;
        discY = -10f;
        discWidth = -10f;
        discHeight = 15f;
        currentYaw = 0f; // Current angle around the z-axis (yaw)
        dxDisc = 0;
        // Get an instance of the SensorManager system service
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        setOnTouchListener(this);


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Check if the touch event occurred within the bounds of the disc

                // Set the flag indicating that the user is touching the disc
                touchingDisc = true;


            // Save the default position of the disc


        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // Reset the disc position if the user released the touch within the bounds of the disc
            if (touchingDisc && touchX >= discX - 15 && touchX <= discX + discWidth + 15
                    && touchY >= discY - 15 && touchY <= discY + discHeight + 15) {
                x = -10f; // X coordinate of the ball
                y = -10f; // Y coordinate of the ball
                dx = 10f; // Change in X coordinate per frame
                dy = 30f; // Change in Y coordinate per frame
                discX = -10f;
                discY = -10f;
                discWidth = -10f;
                discHeight = 15f;
                currentYaw = 0f; // Current angle around the z-axis (yaw)
                dxDisc = 0;
            }
            // Resetthe flag indicating that the user is touching the disc
            touchingDisc = false;
        }

        return true;
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
        canvas.drawText(String.valueOf(xAcceleration), 20, 70, paint);
        if(discX + dxDisc > getWidth() - 1 || discX + dxDisc < -discWidth)
        {
            centerX += 0;
            discX += 0;
        }
        else {
            centerX += dxDisc;
            discX += dxDisc;
        }
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
        if (event.sensor.getType() ==  Sensor.TYPE_LINEAR_ACCELERATION) {
            // Apply high-pass filter to remove gravity component
            gravity[0] = 1f * gravity[0] + (1 - 1f) * event.values[0];
            gravity[1] = 1f * gravity[1] + (1 - 1f) * event.values[1];
            gravity[2] = 1f * gravity[2] + (1 - 1f) * event.values[2];

            linearAcceleration[0] = event.values[0] - gravity[0];
            linearAcceleration[1] = event.values[1] - gravity[1];
            linearAcceleration[2] = event.values[2] - gravity[2];

            // Use the filtered linear acceleration values to move the ball
            // For example:
            xAcceleration = linearAcceleration[0];
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


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    public void start() {
        // Register the SensorEventListener with the SensorManager to start receiving gyroscope sensor events
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),sensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        // Unregister the SensorEventListener when you no longer need to receive gyroscope sensor events
        sensorManager.unregisterListener(this);
        sensorManager.unregisterListener(this);
    }
}

