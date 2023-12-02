package com.example.bss_mobile_lab_4_5


import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    val LOG_TAG = "myLogs"
    var mBtnAdd: Button? = null
    var mBtnRead: Button? = null
    var mBtnClear:Button? = null
    var mEdtName: EditText? = null
    var mEdtEmail:EditText? = null
    var ormEngine: OrmEngine? = null
    var display: TextView? = null
    var recordsVisualizer: RecordsVisualizer? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBtnAdd = findViewById<View>(R.id.buttonAdd) as Button
        mBtnAdd!!.setOnClickListener { v: View ->
            onClick(
                v
            )
        }
        mBtnRead = findViewById(R.id.buttonRead)
        mBtnRead!!.setOnClickListener { v: View ->
            onClick(
                v
            )
        }
        mBtnClear = findViewById(R.id.buttonClear)
        mBtnClear!!.setOnClickListener { v: View ->
            onClick(
                v
            )
        }

        mEdtName = findViewById(R.id.editTextName)
        mEdtEmail = findViewById(R.id.editTextEmail)
        display = findViewById(R.id.display)

        recordsVisualizer = RecordsVisualizer(display!!)
        ormEngine = OrmEngine(DBHelper(this, LOG_TAG), LOG_TAG)

    }

    @SuppressLint("NonConstantResourceId")
    fun onClick(v: View) {
        if (v.id == R.id.buttonAdd) {
            val name = mEdtName!!.text.toString()
            val email: String = mEdtEmail!!.text.toString()

            var obj = DbObject(name = name, email = email)

            ormEngine!!.add(obj)

        }
        if (v.id == R.id.buttonRead) {
            var result = ormEngine!!.read()

            recordsVisualizer!!.show(result)
        }
        if (v.id == R.id.buttonClear) {
            ormEngine!!.clear()
        }
    }
}