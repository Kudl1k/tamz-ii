package cz.kudladev.tamziikmp.permissions.delegate

import cz.kudladev.tamziikmp.permissions.model.PermissionState

internal interface PermissionDelegate {
    fun getPermissionState(): PermissionState
    suspend fun providePermission()
    fun openSettingPage()
}