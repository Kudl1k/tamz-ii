package cz.kudladev.tamziikmp.permissions.delegate

import android.content.Context
import android.location.LocationManager
import android.provider.Settings
import cz.kudladev.tamziikmp.permissions.model.Permission
import cz.kudladev.tamziikmp.permissions.model.PermissionState
import cz.kudladev.tamziikmp.permissions.util.CannotOpenSettingsException
import cz.kudladev.tamziikmp.permissions.util.openPage

internal class LocationServicePermissionDelegate(
    private val context: Context,
    private val locationManager: LocationManager,
) : PermissionDelegate {
    override fun getPermissionState(): PermissionState {
        val granted = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return if (granted)
            PermissionState.GRANTED else PermissionState.DENIED
    }

    override suspend fun providePermission() {
        openSettingPage()
    }

    override fun openSettingPage() {
        context.openPage(
            action = Settings.ACTION_LOCATION_SOURCE_SETTINGS,
            onError = { throw CannotOpenSettingsException(Permission.LOCATION_SERVICE_ON.name) }
        )
    }
}