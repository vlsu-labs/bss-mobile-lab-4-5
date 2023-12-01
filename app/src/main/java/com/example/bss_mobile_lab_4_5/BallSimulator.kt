package com.example.bss_mobile_lab_4_5

import android.widget.ImageView

class BallSimulator {
    private val scale = 0.1F
    private val padding: Int = -10
    private val ball: ImageView
    private val minX: Int
    private val maxX: Int
    private val minY: Int
    private val maxY: Int

    constructor(ball: ImageView, height: Int, width: Int) {
        this.ball = ball
        minX = 0 + padding
        minY = 0 + padding

        val params = ball.layoutParams

        maxX = width - (padding + params.width)
        maxY = height - (padding)
    }

    fun moveBallOn(x: Float, y: Float) {

        var xVal = x / scale * -1
        var yVal = y / scale * 1


        if (ball.x + xVal <= minX) {
            ball.x = minX.toFloat()
        } else if (ball.x + xVal >= maxX) {
            ball.x = maxX.toFloat()
        } else {
            ball.x += xVal
        }

        if (ball.y + yVal <= minY) {
            ball.y = minY.toFloat()
        } else if (ball.y + yVal >= maxY) {
            ball.y = maxY.toFloat()
        } else {
            ball.y += yVal
        }
    }
}