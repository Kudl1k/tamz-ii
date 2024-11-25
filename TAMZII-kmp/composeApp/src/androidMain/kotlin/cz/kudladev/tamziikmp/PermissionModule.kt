package cz.kudladev.tamziikmp

import android.content.Context
import android.location.LocationManager
import cz.kudladev.tamziikmp.permissions.delegate.LocationServicePermissionDelegate
import cz.kudladev.tamziikmp.permissions.delegate.PermissionDelegate
import cz.kudladev.tamziikmp.permissions.model.Permission
import org.koin.core.qualifier.named
import org.koin.dsl.module

val permissionsModule = module {
    single { get<Context>().getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    single<PermissionDelegate>(named(Permission.LOCATION_SERVICE_ON.name)) {
        LocationServicePermissionDelegate(
            context = get(),
            locationManager = get(),
        )
    }
}