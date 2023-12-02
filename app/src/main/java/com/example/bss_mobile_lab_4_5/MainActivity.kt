package com.example.bss_mobile_lab_4_5


import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    val LOG_TAG = "myLogs"
    var mBtnAdd: Button? = null
    var mBtnRead: Button? = null
    var mBtnClear:Button? = null
    var mEdtName: EditText? = null
    var mEdtEmail:EditText? = null
    var mDbHelper: DBHelper? = null

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
        mDbHelper = DBHelper(this,  LOG_TAG)
    }

    @SuppressLint("NonConstantResourceId")
    fun onClick(v: View) {
        val cv = ContentValues()
        val name = mEdtName!!.text.toString()
        val email: String = mEdtEmail!!.text.toString()
        val db = mDbHelper!!.writableDatabase
        if (v.id == R.id.buttonAdd) {
            Log.d(LOG_TAG, "--- Insert in mytable: ---")
            cv.put("name", name)
            cv.put("email", email)
            val rowID = db.insert("mytable", null, cv)
            Log.d(LOG_TAG, "row inserted, ID = $rowID")
        }
        if (v.id == R.id.buttonRead) {
            Log.d(LOG_TAG, "--- Rows in mytable: ---")
            val c = db.query("mytable", null, null, null, null, null, null)
            if (c.moveToFirst()) {
                val idColIndex = c.getColumnIndex("id")
                val nameColIndex = c.getColumnIndex("name")
                val emailColIndex = c.getColumnIndex("email")
                do {
                    Log.d(
                        LOG_TAG,
                        "ID = " + c.getInt(idColIndex) + ", name = "
                                + c.getString(nameColIndex) + ", email = "
                                + c.getString(emailColIndex)
                    )
                } while (c.moveToNext())
            } else Log.d(LOG_TAG, "0 rows")
            c.close()
        }
        if (v.id == R.id.buttonClear) {
            Log.d(LOG_TAG, "--- Clear mytable: ---")
            // удаляем все записи
            val clearCount = db.delete("mytable", null, null)
            Log.d(LOG_TAG, "deleted rows count = $clearCount")
        }
        // закрываем подключение к БД
        mDbHelper!!.close()
    }
}