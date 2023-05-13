package com.example.singleplayerponggame
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class BallView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint()
    private var x = -10f // X coordinate of the ball
    private var y = -10f // Y coordinate of the ball
    private var dx = 0f // Change in X coordinate per frame
    private var dy = 100f // Change in Y coordinate per frame

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(x == -10f && y == -10f)
        {
            x = width / 2f
            y = height / 3f
        }
        // Set the background color to white
        canvas?.drawColor(Color.WHITE)

        // Set the paint color to white
        paint.color = Color.BLACK

        // Update the ball position
        x += dx
        y += dy

        // Check if the ball hits the edges of the view
        if (x > width - 25 || x < 25) {
            dx = -dx
        }
        if (y > height - 25 || y < 25) {
            dy = -dy
        }

        // Draw the ball at the updated position
        canvas?.drawCircle(x, y, 25f, paint)

        // Redraw the view on the next frame
        invalidate()
    }
}