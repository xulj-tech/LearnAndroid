package cn.kt.android.util

import androidx.lifecycle.LifecycleService

class MyLocationService : LifecycleService() {
    init {
        val observer = MyLocationObserver(this)
        lifecycle.addObserver(observer)
    }
}