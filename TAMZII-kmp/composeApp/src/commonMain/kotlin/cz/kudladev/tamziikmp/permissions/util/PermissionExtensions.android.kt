package cz.kudladev.tamziikmp.permissions.util

import cz.kudladev.tamziikmp.permissions.delegate.PermissionDelegate
import cz.kudladev.tamziikmp.permissions.model.Permission
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

internal fun KoinComponent.getPermissionDelegate(permission: Permission): PermissionDelegate {
    val permissionDelegate by inject<PermissionDelegate>(named(permission.name))
    return permissionDelegate
}