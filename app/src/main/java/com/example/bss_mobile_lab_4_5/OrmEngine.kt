package com.example.bss_mobile_lab_4_5

import android.content.ContentValues
import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate.now
import java.util.Calendar
import java.util.Date
import java.util.Locale




class OrmEngine(private val dbHelper: DBHelper, private val logTag: String) {

    private val idColumnName = "id"
    private val nameColumnName = "name"
    private val dateColumnName = "date"
    private val emailColumnName = "email"

    fun add(dbObject: DbObject) {
        val cv = ContentValues()
        val db = dbHelper.writableDatabase

        val c: Date = Calendar.getInstance().getTime()
        println("Current time => $c")

        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

        dbObject.date  = df.format(c)
        Log.d(logTag, "--- Insert in mytable: ---")
        cv.put(nameColumnName, dbObject.name)
        cv.put(emailColumnName, dbObject.email)
        cv.put(dateColumnName, dbObject.date)
        val rowID = db.insert("mytable", null, cv)
        Log.d(logTag, "row inserted, $idColumnName = $rowID")

        dbHelper.close()
    }

    fun read(limit: Int = -1, skip: Int = 0): List<DbObject> {
        val db = dbHelper.writableDatabase

        Log.d(logTag, "--- Rows in mytable: ---")

        var result = ArrayList<DbObject>()

        var curSkip = skip
        var curLimit = limit

        val c = db.query("mytable", null, null, null, null, null, null)
        if (c.moveToFirst()) {
            val idColIndex = c.getColumnIndex(idColumnName)
            val nameColIndex = c.getColumnIndex(nameColumnName)
            val emailColIndex = c.getColumnIndex(emailColumnName)
            val dateColIndex = c.getColumnIndex(dateColumnName)
            do {

                if (curSkip != 0) {
                    curSkip -= 1
                    continue
                }

                if (curLimit == 0) {
                    break
                }

                var obj = DbObject(
                    c.getInt(idColIndex),
                    c.getString(nameColIndex),
                    c.getString(emailColIndex),
                    c.getString(dateColIndex)
                )
                Log.d(
                    logTag,
                    "$idColumnName = ${obj.id}, " +
                            "$nameColumnName = ${obj.name}, " +
                            "$emailColumnName = ${obj.email}, " +
                            "$dateColumnName = ${obj.date}"
                )
                curLimit -= 1
                result.add(obj)
            } while (c.moveToNext())
        } else Log.d(logTag, "0 rows")
        c.close()

        dbHelper.close()

        return result
    }

    fun clear() {
        Log.d(logTag, "--- Clear mytable: ---")
        val db = dbHelper.writableDatabase
        val clearCount = db.delete("mytable", null, null)
        Log.d(logTag, "deleted rows count = $clearCount")

        dbHelper.close()
    }
}