package cn.kt.android.util

import android.Manifest
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import android.location.LocationManager
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.Lifecycle
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log

class MyLocationObserver(private val mContext: Context) : LifecycleObserver {
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: MyLocationListener

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun startLocation() {
        Log.d(TAG, "-- startLocation")
        locationListener = MyLocationListener()
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3000,
            1f,
            locationListener
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun stopLocation() {
        Log.d(TAG, "-- stopLocation")
        locationManager.removeUpdates(locationListener)
    }

    internal class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d(TAG, location.toString())
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    companion object {
        private const val TAG = "MyLocationObserver"
    }
}