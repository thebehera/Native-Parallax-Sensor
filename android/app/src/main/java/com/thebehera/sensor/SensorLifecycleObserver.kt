package com.thebehera.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Register and unregister the sensor listener based on the lifecycle state. Only calls the
 * changeCallback when in a started state.
 */
class SensorLifecycleObserver(
    context: Context,
    changeCallback: (Float, Float) -> Unit
) : DefaultLifecycleObserver {

    private val manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val xyAxisSensorEventListener = DeltaXYAxisSensorEventListener(changeCallback)

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        xyAxisSensorEventListener.resetInitialMatrix()
        manager.registerListener(
            xyAxisSensorEventListener,
            manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        manager.unregisterListener(xyAxisSensorEventListener)
    }
}