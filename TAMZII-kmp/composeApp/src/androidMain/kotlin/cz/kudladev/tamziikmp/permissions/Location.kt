package cz.kudladev.tamziikmp.permissions

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.mp.KoinPlatform.getKoin
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual suspend fun getLocation(): Pair<Double, Double>? {
    val context: Context = getKoin().get()
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    return try {
        val location = getLastLocation(fusedLocationClient)
        location?.let {
            Pair(it.latitude, it.longitude)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@SuppressLint("MissingPermission")
private suspend fun getLastLocation(fusedLocationClient: FusedLocationProviderClient): Location? =
    suspendCancellableCoroutine { continuation ->
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    continuation.resume(location)
                } else {
                    continuation.resumeWithException(Exception("Location is null"))
                }
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }