package com.example.bss_mobile_lab_4_5

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

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

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAccelerometerX = findViewById<View>(R.id.textView) as TextView
        mAccelerometerY = findViewById<View>(R.id.textView2) as TextView
        mAccelerometerZ = findViewById<View>(R.id.textView3) as TextView
        mMagneticX = findViewById<View>(R.id.textView5) as TextView
        mMagneticY = findViewById<View>(R.id.textView6) as TextView
        mMagneticZ = findViewById<View>(R.id.textView7) as TextView
        mProximity = findViewById<View>(R.id.textView9) as TextView
        mLight = findViewById<View>(R.id.textView11) as TextView
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometerSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagneticSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        mProximitySensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        mLightSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        mMaxValue = mLightSensor!!.maximumRange
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
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