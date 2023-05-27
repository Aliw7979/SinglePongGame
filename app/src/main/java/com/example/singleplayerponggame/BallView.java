package com.example.singleplayerponggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnTouchListener;

public class BallView extends View implements  SensorEventListener, OnTouchListener {
    private float distance;
    private boolean touchingDisc;
    private float[] linearAcceleration = new float[3];
    private float[] gravity = new float[3];
    private SensorManager sensorManager;
    private Paint paint = new Paint();
    private float score = 0;
    private float x ;// X coordinate of the ball
    private float y ; // Y coordinate of the ball
    private float dx ;
    private long lastCallTime = 0;
    private  float aBall = 10;
    // Change in X coordinate per frame
    private float dy ; // Change in Y coordinate per frame
    private float discX ;
    private float radius = 20f;
    private float discY ;
    private float discWidth ;
    private float discHeight ;
    private  float dxDisc;
    private TextView textView;
    private float centerX,centerY;
    private  float rotationZ;
    private float rocketAngle;
    private float xAcceleration;
    private float zAcceleration;
    private float yAcceleration;
    private Canvas canvas;
    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        x = -10f; // X coordinate of the ball
        y = -10f; // Y coordinate of the ball
        dx = 0f; // Change in X coordinate per frame
        dy = 0f; // Change in Y coordinate per frame
        discX = -10f;
        discY = -10f;
        discWidth = -10f;
        discHeight = 15f;

        dxDisc = 0;
        // Get an instance of the SensorManager system service
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        setOnTouchListener(this);


    }

    public boolean checkLineCircleCollision(double lineStartX, double lineStartY, double lineEndX, double lineEndY,
                                            double circleX, double circleY, double radius) {
        double dx = lineEndX - lineStartX;
        double dy = lineEndY - lineStartY;
        double lineLength = Math.sqrt(dx * dx + dy * dy);
        double unitDx = dx / lineLength;
        double unitDy = dy/ lineLength;

        double cx = circleX - lineStartX;
        double cy = circleY - lineStartY;

        double projection = cx * unitDx + cy * unitDy;

        double closestPointX, closestPointY;

        if (projection < 0) {
            closestPointX = lineStartX;
            closestPointY = lineStartY;
        } else if (projection > lineLength) {
            closestPointX = lineEndX;
            closestPointY = lineEndY;
        } else {
            closestPointX = lineStartX + projection * unitDx;
            closestPointY = lineStartY + projection * unitDy;
        }

        distance = (float) Math.sqrt(Math.pow(closestPointX - circleX, 2) + Math.pow(closestPointY - circleY, 2));

        return distance <= radius;
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
                score = 0;
                x = -10f; // X coordinate of the ball
                y = -10f; // Y coordinate of the ball
                dx = 0f; // Change in X coordinate per frame
                dy = 0f; // Change in Y coordinate per frame
                discX = -10f;
                discY = -10f;
                discWidth = -10f;
                discHeight = 15f;
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
        paint.setColor(Color.BLACK);

        // Update the ball position
        x += dx;
        y += dy;

        // Check if the ball hits the edges of the view
        if (x > getWidth() - radius || x < radius) {
            dx = -dx;
        }
        if (y > getHeight() - radius || y < radius) {
            dy = -dy;
        }

        dy += aBall/200;

        float radian = ( float) Math.toRadians(-rocketAngle + rotationZ);
        float discX1 = centerX - (((float) Math.cos(radian)) * discWidth/2);
        float discX2 = centerX + (((float) Math.cos(radian)) * discWidth/2);
        float discY1 = centerY - (((float) Math.sin(radian)) * discWidth/2);
        float discY2 = centerY + (((float) Math.sin(radian)) * discWidth/2);
        if (checkLineCircleCollision(discX2,discY2,discX1,discY1,x,y,radius)) {
            dy = - (float) Math.cos(radian) * dy + (float) Math.sin(radian) * dx;
            dx =   (float) Math.cos(radian) * dx - (float) Math.sin(radian) * dy;
            if (!(dy < (float) Math.cos(radian) * (yAcceleration)) )
                dy += ((float) Math.cos(radian) * (yAcceleration));
            dx += (float) Math.sin(radian) * (yAcceleration);
            zAcceleration = yAcceleration;
            score += 1;
        }

        // Check for collision with walls
        if (x - radius < 0 || x + radius > getWidth()) {
            canvas.drawText(String.valueOf((x - radius)), 20, 150, paint);
            //dx = -dx;
        }

        canvas.drawCircle(x, y, radius , paint);
        canvas.drawText("Angle: " +String.valueOf(rotationZ - rocketAngle), 20, 30, paint);
        canvas.drawText("rAngle: " + String.valueOf((rotationZ)), 20, 70, paint);
        canvas.drawText("xacc: " + String.valueOf(xAcceleration), 20, 110, paint);
        canvas.drawText("zacc: "+ String.valueOf((zAcceleration)), 20, 150, paint);
        canvas.drawText(String.valueOf((score)), 510, 150, paint);
        if(discX + dxDisc > getWidth() - 1 || discX + dxDisc < -discWidth)
        {
            centerX += 0;
            discX += 0;
        }
        else {
            centerX += dxDisc;
            discX += dxDisc;
        }
        canvas.rotate(-rocketAngle+rotationZ, centerX, centerY);
        canvas.drawRect(discX, discY,discX + discWidth , discHeight + discY, paint);
        paint.setTextSize(30);


        invalidate();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() ==  Sensor.TYPE_LINEAR_ACCELERATION) {
            final float alpha = 0.8f;

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linearAcceleration[0] = event.values[0] - gravity[0];
            linearAcceleration[1] = event.values[1] - gravity[1];
            linearAcceleration[2] = event.values[2] - gravity[2];


            xAcceleration = linearAcceleration[0];
            yAcceleration = linearAcceleration[2];

            if(-xAcceleration > 0 && -xAcceleration > 2)
            {
                dxDisc = 15;
            }
            else if (-xAcceleration < 0 && -xAcceleration < -2){
               dxDisc = -15;
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
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),sensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        // Unregister the SensorEventListener when you no longer need to receive gyroscope sensor events
        sensorManager.unregisterListener(this);
        sensorManager.unregisterListener(this);
    }
}

