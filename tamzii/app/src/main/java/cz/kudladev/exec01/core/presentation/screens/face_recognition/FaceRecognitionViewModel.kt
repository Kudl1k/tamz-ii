package cz.kudladev.exec01.core.presentation.screens.face_recognition

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FaceRecognitionViewModel: ViewModel() {
    private val _state = MutableStateFlow(FaceRecognitionState())
    val state = _state.asStateFlow()

    fun onEvent(event: FaceRecognitionEvents) {
        when(event) {
            is FaceRecognitionEvents.permissionGranted -> {
                _state.value = _state.value.copy(permissionGranted = event.granted)
            }
            is FaceRecognitionEvents.ImageSelected -> {
                _state.value = _state.value.copy(selectedImageUri = event.uri)
            }
        }
    }

}