package cz.kudladev.exec01.core.presentation.screens.scanner_screen

sealed class ScannerScreenEvent {

    data class setCodeText(val codeText: String): ScannerScreenEvent()

    object CreateBarcode: ScannerScreenEvent()

}