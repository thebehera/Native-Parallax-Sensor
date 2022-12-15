package com.thebehera.sensor

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageView = ActivityCompat.requireViewById<ImageView>(this, R.id.image_view)
        (imageView.parent as ViewGroup).clipChildren = false
        lifecycle.addObserver(SensorLifecycleObserver(this) { xRotation, yRotation ->
            //-180 degrees is 0, -90 degrees is -1, 0 degrees is 0, 90 degrees is 1, 180 is 0
            imageView.translationX = yRotation * 180f
            imageView.translationY = xRotation * 180f
        })
    }
}


