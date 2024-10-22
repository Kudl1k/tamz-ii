package cz.kudladev.exec01.core.presentation.screens.scanner_screen

data class ScannerScreenState(
    val codeText: String = "",
    val createBarcode: Boolean = false,
)
