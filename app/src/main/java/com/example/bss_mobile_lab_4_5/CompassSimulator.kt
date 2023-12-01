package com.example.bss_mobile_lab_4_5

import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import kotlin.math.atan2
import kotlin.math.sqrt

class CompassSimulator(private val compassView: ImageView) {
    private val ALPHA = 0.15f

    private val calculateRotateAngle: (FloatArray, FloatArray) -> Float = {
            a, b -> map180to360(convertRadtoDeg(calculateRadianAngle(a, b)))
    }

    private var angle = 0f
    private var oldAngle = 0f

    private var _accelerometerData = FloatArray(3)
    private var _magnetometerData = FloatArray(3)

    var accelerometerData: FloatArray
        set(value) {
            _accelerometerData = lowPassFilter(value, _accelerometerData)
        }
        get() = _accelerometerData

    var magnetometerData: FloatArray
        set(value) {
            _magnetometerData = lowPassFilter(value, _magnetometerData)
        }
        get() = _magnetometerData

    private fun calculateRadianAngle(accelerometerReading: FloatArray, magnetometerReading: FloatArray): Float {
        var Ax = accelerometerReading[0]
        var Ay = accelerometerReading[1]
        var Az = accelerometerReading[2]
        val Ex = magnetometerReading[0]
        val Ey = magnetometerReading[1]
        val Ez = magnetometerReading[2]

        var Hx = Ey * Az - Ez * Ay
        var Hy = Ez * Ax - Ex * Az
        var Hz = Ex * Ay - Ey * Ax

        val invH = 1.0f / sqrt((Hx * Hx + Hy * Hy + Hz * Hz).toDouble()).toFloat()
        Hx *= invH
        Hy *= invH
        Hz *= invH

        val invA = 1.0f / sqrt((Ax * Ax + Ay * Ay + Az * Az).toDouble()).toFloat()
        Ax *= invA
        Ay *= invA
        Az *= invA

        val Mx = Ay * Hz - Az * Hy
        val My = Az * Hx - Ax * Hz
        val Mz = Ax * Hy - Ay * Hx

        return atan2(Hy.toDouble(), My.toDouble()).toFloat()
    }

    private fun convertRadtoDeg(rad: Float): Float {
        return (rad / Math.PI).toFloat() * 180
    }

    private fun map180to360(angle: Float): Float {
        return (angle + 360) % 360
    }

    private fun lowPassFilter(input: FloatArray, output: FloatArray?): FloatArray {
        if (output == null) return input
        for (i in input.indices) {
            output[i] = output[i] + ALPHA * (input[i] - output[i])
        }
        return output
    }

    private fun rotateCompass() {
        val rotateAnimation = RotateAnimation(
            -oldAngle,
            -angle,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = 500
        rotateAnimation.fillAfter = true
        compassView.startAnimation(rotateAnimation)
    }

    fun updateCompassViewPosition() {
        oldAngle = angle
        angle = calculateRotateAngle(accelerometerData, magnetometerData)

        rotateCompass()
    }
}