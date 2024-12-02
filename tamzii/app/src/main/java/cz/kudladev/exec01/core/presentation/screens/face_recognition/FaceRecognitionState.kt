package cz.kudladev.exec01.core.presentation.screens.face_recognition

import android.net.Uri

data class FaceRecognitionState(
    val permissionGranted: Boolean = false,
    val selectedImageUri: Uri? = null,
)
