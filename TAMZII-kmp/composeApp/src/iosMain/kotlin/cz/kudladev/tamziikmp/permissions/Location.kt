package cz.kudladev.tamziikmp.permissions

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getLocation(): Pair<Double, Double>? {
    val locationManager = CLLocationManager()
    return try {
        val location = getLastLocation(locationManager)
        location?.let {
            Pair(it.coordinate.useContents { latitude }, it.coordinate.useContents { longitude })
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private suspend fun getLastLocation(locationManager: CLLocationManager): CLLocation? =
    suspendCancellableCoroutine { continuation ->
        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                val location = didUpdateLocations.lastOrNull() as? CLLocation
                if (location != null) {
                    continuation.resume(location)
                } else {
                    continuation.resumeWithException(Exception("Location is null"))
                }
                manager.stopUpdatingLocation()
            }

            override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                continuation.resumeWithException(didFailWithError as Exception)
            }
        }

        locationManager.delegate = delegate
        when (CLLocationManager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedAlways, kCLAuthorizationStatusAuthorizedWhenInUse -> {
                locationManager.startUpdatingLocation()
            }
            kCLAuthorizationStatusNotDetermined -> {
                locationManager.requestWhenInUseAuthorization()
                locationManager.startUpdatingLocation()
            }
            else -> {
                continuation.resumeWithException(Exception("Location permission denied"))
            }
        }
    }