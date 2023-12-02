package com.example.bss_mobile_lab_4_5

import android.widget.TextView
import java.lang.Integer.max

class RecordsVisualizer(private val display: TextView) {
    private val splitter = "|"
    private val fillers = arrayOf("0", "#", "#", "#")
    private val isFillFromPrefix = arrayOf(true, false, false, false)

    fun show(records: List<DbObject>) {
        var sb: StringBuilder = StringBuilder()

        var arr = Array(records.size) { _ -> Array(4) { _ -> "" } }

        for (i in records.indices) {
            arr[i][0] = records[i].id.toString()
            arr[i][1] = records[i].name
            arr[i][2] = records[i].email
            arr[i][3] = records[i].date.toString()
        }

        normalizeRecords(arr)

        for (i in records.indices) {
            sb.append(arr[i].joinToString(splitter))
            sb.append("\n")
        }


        display.text = sb.toString()
    }

    private fun normalizeRecords(records: Array<Array<String>>) {
        var maxLength = IntArray(records.size) { _ -> 0}

        for (i in records.indices) {
            for (y in records.indices) {
                maxLength[y] = max(maxLength[y], records[i][y].length)
            }
        }

        for (i in records.indices) {
            for (y in records.indices) {
                var delta = maxLength[y] - records[i][y].length
                for (x in 0 until delta) {
                    if (isFillFromPrefix[y]) {
                        records[i][y] = fillers[y] + records[i][y]
                    } else {
                        records[i][y] = records[i][y] + fillers[y]
                    }
                }
            }
        }
    }
}