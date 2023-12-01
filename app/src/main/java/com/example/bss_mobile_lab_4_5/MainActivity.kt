package com.example.bss_mobile_lab_4_5


import android.R.attr.orientation
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), SensorEventListener {

    var mAccelerometerX: TextView? = null
    var mAccelerometerY: TextView? = null
    var mAccelerometerZ: TextView? = null
    var mMagneticX: TextView? = null
    var mMagneticY: TextView? = null
    var mMagneticZ: TextView? = null
    var mProximity: TextView? = null
    var mLight: TextView? = null
    var sensorManager: SensorManager? = null
    var mAccelerometerSensor: Sensor? = null
    var mProximitySensor: Sensor? = null
    var mMagneticSensor: Sensor? = null
    var mLightSensor: Sensor? = null
    var mMaxValue = 0f
    var mValue = 0f

    var compass: ImageView? = null
    var boll: ImageView? = null
    var test: ImageView? = null

    var minX: Float = 0F
    var maxX: Float = 0F
    var minY: Float = 0F
    var maxY: Float = 0F

    private var lastAccelerometer: FloatArray = FloatArray(3)
    private var lastMagnetometer: FloatArray = FloatArray(3)
    private var lastAccelerometerSet: Boolean = false
    private var lastMagnetometerSet: Boolean = false
    private var rotationMatrix: FloatArray = FloatArray(9)
    private var orientation: FloatArray = FloatArray(3)

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        test = findViewById(R.id.imageView3)

        minX = 50F
        maxX = 200F
        minY = 50F
        maxY = 200F

        mAccelerometerX = findViewById(R.id.textView)
        mAccelerometerY = findViewById(R.id.textView2)
        mAccelerometerZ = findViewById(R.id.textView3)
        mMagneticX = findViewById(R.id.textView5)
        mMagneticY = findViewById(R.id.textView6)
        mMagneticZ = findViewById(R.id.textView7)
        mProximity = findViewById(R.id.textView9)
        mLight = findViewById(R.id.textView11)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometerSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagneticSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        mProximitySensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        mLightSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        mMaxValue = mLightSensor!!.maximumRange
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent) {

//        test!!.rotation += 10F

        val outMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = display
            display?.getRealMetrics(outMetrics)
        } else {
            val display = windowManager.defaultDisplay
            display.getMetrics(outMetrics)
        }


        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.size)
            lastMagnetometerSet = true
        } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.size)
            lastAccelerometerSet = true
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            SensorManager.getRotationMatrix(
                rotationMatrix,
                null,
                lastAccelerometer,
                lastMagnetometer
            )
            SensorManager.getOrientation(rotationMatrix, orientation)
            val azimuthInRadians: Float = orientation[0]
            val azimuthInDegrees =
                (Math.toDegrees(azimuthInRadians.toDouble()) + 360).toFloat() % 360

            test!!.rotation = azimuthInDegrees
        }


        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {

            var xVal = (event.values[0] / 10F) * -1
            var yVal = (event.values[1] / 10F) * 1

            if (test!!.x + xVal <= minX) {
                test!!.x = minX
            } else if (test!!.x + xVal >= maxX) {
                test!!.x = maxX
            } else {
                test!!.x += xVal
            }

            if (test!!.y + yVal <= minY) {
                test!!.y = minY
            } else if (test!!.y + yVal >= maxY) {
                test!!.y = maxY
            } else {
                test!!.y += yVal
            }

            mAccelerometerX!!.text = event.values[0].toString()
            mAccelerometerY!!.text = event.values[1].toString()
            mAccelerometerZ!!.text = event.values[2].toString()
        }
        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            mMagneticX!!.text = event.values[0].toString()
            mMagneticY!!.text = event.values[1].toString()
            mMagneticZ!!.text = event.values[2].toString()
        }
        if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
            mProximity!!.text = event.values[0].toString()
        }
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            mLight!!.text = event.values[0].toString()
            mValue = event.values[0]
            val layout = window.attributes
            layout.screenBrightness = (255f * mValue / mMaxValue).toInt().toFloat()
            window.attributes = layout
        }
    }

    override fun onStart() {
        super.onStart()
        sensorManager?.registerListener(
            this, mAccelerometerSensor,
            SensorManager.SENSOR_DELAY_FASTEST
        )
        sensorManager?.registerListener(
            this, mMagneticSensor,
            SensorManager.SENSOR_DELAY_FASTEST
        )
        sensorManager?.registerListener(
            this, mProximitySensor,
            SensorManager.SENSOR_DELAY_FASTEST
        )
        sensorManager?.registerListener(
            this, mLightSensor,
            SensorManager.SENSOR_DELAY_FASTEST
        )
    }

    override fun onStop() {
        super.onStop()
        super.onStop()
        sensorManager?.unregisterListener(this, mAccelerometerSensor)
        sensorManager?.unregisterListener(this, mMagneticSensor)
        sensorManager?.unregisterListener(this, mProximitySensor)
        sensorManager?.unregisterListener(this, mLightSensor)
    }

    override fun onAccuracyChanged(sensor: Sensor?, i: Int) {
        sensorManager?.unregisterListener(this, mLightSensor)
    }
}