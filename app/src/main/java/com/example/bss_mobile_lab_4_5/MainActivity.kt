package com.example.bss_mobile_lab_4_5


import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.display.DisplayManagerCompat


class MainActivity : AppCompatActivity(), SensorEventListener {
    private var sensorManager: SensorManager? = null

    private var compassSimulator: CompassSimulator? = null
    private var ballSimulator: BallSimulator? = null

    private var imageViewCompass: ImageView? = null
    private var ballView: ImageView? = null
    private val sensorEventTracker: SensorEventTracker = SensorEventTracker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageViewCompass = findViewById(R.id.imageView3)
        ballView = findViewById(R.id.ball)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        compassSimulator = CompassSimulator(imageViewCompass!!)


        val defaultDisplay = DisplayManagerCompat
            .getInstance(this)
            .getDisplay(Display.DEFAULT_DISPLAY)

        val displayContext = createDisplayContext(defaultDisplay!!)

        val width = displayContext.resources.displayMetrics.widthPixels
        val height = displayContext.resources.displayMetrics.heightPixels

        ballSimulator = BallSimulator(ballView!!, height, width)

        sensorEventTracker.subscribeAction(
            Sensor.TYPE_ACCELEROMETER,
            fun (x : SensorEvent): UInt {

                compassSimulator!!.accelerometerData = x.values
                compassSimulator!!.updateCompassViewPosition()
                return 0u
            }
        )

        sensorEventTracker.subscribeAction(
            Sensor.TYPE_MAGNETIC_FIELD,
            fun (x : SensorEvent): UInt {

                compassSimulator!!.magnetometerData = x.values
                compassSimulator!!.updateCompassViewPosition()
                return 0u
            }
        )

        sensorEventTracker.subscribeAction(
            Sensor.TYPE_ACCELEROMETER,
            fun (x : SensorEvent): UInt {
                var xVal = x.values[0]
                var yVal = x.values[1]
                ballSimulator!!.moveBallOn(xVal, yVal)
                return 0u
            }
        )
    }

    override fun onResume() {
        super.onResume()
        val accelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null) {
            sensorManager!!.registerListener(
                this, accelerometer,
                SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_GAME
            )
        }
        val magneticField = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        if (magneticField != null) {
            sensorManager!!.registerListener(
                this, magneticField,
                SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        sensorEventTracker.triggerActions(event.sensor.type, event)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}