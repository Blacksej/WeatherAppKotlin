package dev.danieltm.weatherapp

import android.app.Activity
import android.content.Context

interface LocationService {
    suspend fun getLocation(activity: Activity, context: Context): List<String>
}