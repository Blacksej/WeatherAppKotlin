package dev.danieltm.weatherapp

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationProvider {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var latAndLong: ArrayList<String> = ArrayList<String>()

    suspend fun getLocation(activity: Activity, context: Context): ArrayList<String>
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        // Check location permission
        if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return ArrayList()
        }

        // get latitude and longitude
        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if(it!=null){
                latAndLong.add(it.latitude.toString())
                latAndLong.add(it.longitude.toString())
            }
        }
        return latAndLong
    }
}