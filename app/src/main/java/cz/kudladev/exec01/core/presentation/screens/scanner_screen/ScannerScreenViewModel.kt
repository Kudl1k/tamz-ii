package cz.kudladev.exec01.core.presentation.screens.scanner_screen

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScannerScreenViewModel(

): ViewModel() {

    private val _state = MutableStateFlow(ScannerScreenState())
    val state = _state.asStateFlow()

    fun onEvent(event: ScannerScreenEvent) {
        when(event) {
            is ScannerScreenEvent.setCodeText -> {
                if (event.codeText.length > 11) return
                if (!event.codeText.isDigitsOnly()) return
                _state.value = _state.value.copy(
                    codeText = event.codeText
                )
            }
            ScannerScreenEvent.CreateBarcode -> {
                _state.value = _state.value.copy(
                    createBarcode = !_state.value.createBarcode
                )
            }
        }
    }
}