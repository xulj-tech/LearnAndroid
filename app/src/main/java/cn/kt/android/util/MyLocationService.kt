package cn.kt.app.jetpack

import androidx.lifecycle.LifecycleService
import cn.kt.app.jetpack.MyLocationObserver

class MyLocationService : LifecycleService() {
    init {
        val observer = MyLocationObserver(this)
        lifecycle.addObserver(observer)
    }
}