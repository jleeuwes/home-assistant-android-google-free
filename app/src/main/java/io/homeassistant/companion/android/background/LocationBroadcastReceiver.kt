package io.homeassistant.companion.android.background

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import io.homeassistant.companion.android.common.dagger.GraphComponentAccessor
import io.homeassistant.companion.android.domain.integration.IntegrationUseCase
import io.homeassistant.companion.android.domain.integration.UpdateLocation
import io.homeassistant.companion.android.sensors.SensorWorker
import io.homeassistant.companion.android.util.PermissionManager
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LocationBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_REQUEST_LOCATION_UPDATES =
            "io.homeassistant.companion.android.background.REQUEST_UPDATES"
        const val ACTION_REQUEST_ACCURATE_LOCATION_UPDATE =
            "io.homeassistant.companion.android.background.REQUEST_ACCURATE_UPDATE"
        const val ACTION_PROCESS_LOCATION =
            "io.homeassistant.companion.android.background.PROCESS_UPDATES"
        const val ACTION_PROCESS_GEO =
            "io.homeassistant.companion.android.background.PROCESS_GEOFENCE"

        private const val TAG = "LocBroadcastReceiver"

        private const val MINIMUM_ACCURACY = 200
    }

    @Inject
    lateinit var integrationUseCase: IntegrationUseCase

    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onReceive(context: Context, intent: Intent) {
        ensureInjected(context)

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            ACTION_REQUEST_LOCATION_UPDATES -> setupLocationTracking(context)
            ACTION_PROCESS_LOCATION -> handleLocationUpdate(context, intent)
            ACTION_PROCESS_GEO -> handleGeoUpdate(context, intent)
            ACTION_REQUEST_ACCURATE_LOCATION_UPDATE -> requestSingleAccurateLocation(context)
            else -> Log.w(TAG, "Unknown intent action: ${intent.action}!")
        }
    }

    private fun ensureInjected(context: Context) {
        if (context.applicationContext is GraphComponentAccessor) {
            DaggerReceiverComponent.builder()
                .appComponent((context.applicationContext as GraphComponentAccessor).appComponent)
                .build()
                .inject(this)
        } else {
            throw Exception("Application Context passed is not of our application!")
        }
    }

    private fun setupLocationTracking(context: Context) {
        if (!PermissionManager.hasLocationPermissions(context)) {
            Log.w(TAG, "Not starting location reporting because of permissions.")
            return
        }

        mainScope.launch {
            try {
                removeAllLocationUpdateRequests(context)

                if (integrationUseCase.isBackgroundTrackingEnabled())
                    requestLocationUpdates(context)
                if (integrationUseCase.isZoneTrackingEnabled())
                    requestZoneUpdates(context)
            } catch (e: Exception) {
                Log.e(TAG, "Issue setting up location tracking", e)
            }
        }
    }

    private fun removeAllLocationUpdateRequests(context: Context) {
        Log.d(TAG, "STUB Removing all location requests.")
    }

    private fun requestLocationUpdates(context: Context) {
        Log.d(TAG, "STUB Registering for location updates.")
    }

    private suspend fun requestZoneUpdates(context: Context) {
        Log.d(TAG, "STUB Registering for zone based location updates")
    }

    private fun handleLocationUpdate(context: Context, intent: Intent) {
        Log.d(TAG, "STUB Received location update.")
    }

    private fun handleGeoUpdate(context: Context, intent: Intent) {
        Log.d(TAG, "STUB Received geofence update.")
    }

    private fun sendLocationUpdate(location: Location, context: Context) {
        // Anytime we send a location we should start the sensor worker.
        // This is cause it to send sensors then restart the 15 minute interval.
        SensorWorker.start(context)

        Log.d(
            TAG, "Last Location: " +
                    "\nCoords:(${location.latitude}, ${location.longitude})" +
                    "\nAccuracy: ${location.accuracy}" +
                    "\nBearing: ${location.bearing}"
        )
        val updateLocation = UpdateLocation(
            "",
            arrayOf(location.latitude, location.longitude),
            location.accuracy.toInt(),
            getBatteryLevel(context),
            location.speed.toInt(),
            location.altitude.toInt(),
            location.bearing.toInt(),
            if (Build.VERSION.SDK_INT >= 26) location.verticalAccuracyMeters.toInt() else 0
        )

        mainScope.launch {
            try {
                integrationUseCase.updateLocation(updateLocation)
            } catch (e: Exception) {
                Log.e(TAG, "Could not update location.", e)
            }
        }
    }

    private fun requestSingleAccurateLocation(context: Context) {
    }

    private fun getBatteryLevel(context: Context): Int? {
        val batteryIntent =
            context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        if (level == -1 || scale == -1) {
            Log.e(TAG, "Issue getting battery level!")
            return null
        }

        return (level.toFloat() / scale.toFloat() * 100.0f).toInt()
    }
}
