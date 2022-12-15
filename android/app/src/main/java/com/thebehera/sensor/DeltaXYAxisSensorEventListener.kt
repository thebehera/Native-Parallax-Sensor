package com.thebehera.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * Provides the x and y axis rotation of the device with a range from -1 to 1 in respect to the
 * first sensor event. Requires TYPE_GAME_ROTATION_VECTOR.
 */
class DeltaXYAxisSensorEventListener(private val xyAxisChangeCallback: (Float, Float) -> Unit) :
    SensorEventListener {
    private val initial = FloatArray(9)
    private val current = FloatArray(9)
    private var isInitialMatrixSet = false

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_GAME_ROTATION_VECTOR) return
        if (isInitialMatrixSet) {
            SensorManager.getRotationMatrixFromVector(current, event.values)
            xyAxisChangeCallback(
                // Inspired by SensorManger.getAngleChange
                -(initial[2] * current[1] + initial[5] * current[4] + initial[8] * current[7]),
                -(initial[2] * current[0] + initial[5] * current[3] + initial[8] * current[6])
            )
        } else {
            SensorManager.getRotationMatrixFromVector(initial, event.values)
            isInitialMatrixSet = true
        }
    }

    fun resetInitialMatrix() {
        isInitialMatrixSet = false
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) = Unit
}