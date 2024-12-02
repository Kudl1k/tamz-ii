package cz.kudladev.exec01.core.presentation.screens.face_recognition

import android.net.Uri

sealed class FaceRecognitionEvents {

    data class permissionGranted(val granted: Boolean) : FaceRecognitionEvents()

    data class ImageSelected(val uri: Uri) : FaceRecognitionEvents()
}