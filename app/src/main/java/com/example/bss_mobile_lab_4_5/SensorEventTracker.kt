package com.example.bss_mobile_lab_4_5

import android.hardware.SensorEvent

typealias EventAction = (SensorEvent) -> UInt

class SensorEventTracker {
    private val _subscribes: MutableMap<Int, MutableList<EventAction>> = HashMap()

    fun subscribeAction(eventType: Int, action: EventAction) {
        if (!_subscribes.containsKey(eventType)) {
            _subscribes[eventType] = ArrayList()
        }

        _subscribes[eventType]!!.add(action)
    }

    fun triggerActions(eventType: Int, sensorEvent: SensorEvent) {
        if (!_subscribes.containsKey(eventType)) {
            return
        }

        for (action in _subscribes[eventType]!!.iterator()) {
            action(sensorEvent)
        }
    }
}