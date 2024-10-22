package cz.kudladev.exec01.core.presentation.screens.scanner_screen

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

object BarcodeView {
    val L = intArrayOf(0x0D, 0x19, 0x13, 0x3D, 0x23, 0x31, 0x2F, 0x3B, 0x37, 0x0B)
    val R = intArrayOf(0x72, 0x66, 0x6C, 0x42, 0x5C, 0x5E, 0x50, 0x44, 0x48, 0x74)
    const val BARCODE_WIDTH = 600
    const val BARCODE_HEIGHT = 200
    const val BARCODE_LINE_WIDTH = 4
    const val QUIET_ZONE = 10 // Quiet zone around the barcode
}

@Composable
fun BarcodeView(barcodeString: String, modifier: Modifier = Modifier) {
    if (barcodeString.length != 12) return // Ensure UPC-A is 12 digits

    val bitmap = Bitmap.createBitmap(BarcodeView.BARCODE_WIDTH, BarcodeView.BARCODE_HEIGHT, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    drawBarcode(canvas, barcodeString)

    Canvas(modifier = modifier) {
        drawIntoCanvas { nativeCanvas ->
            nativeCanvas.nativeCanvas.drawBitmap(bitmap, 0f, 0f, null)
        }
    }
}

fun drawBarcode(canvas: Canvas, barcodeString: String) {
    val bgPaint = Paint().apply { color = Color.WHITE }
    val barPaint = Paint().apply { color = Color.BLACK; strokeWidth = BarcodeView.BARCODE_LINE_WIDTH.toFloat() }

    // Draw background
    canvas.drawRect(Rect(0, 0, BarcodeView.BARCODE_WIDTH, BarcodeView.BARCODE_HEIGHT), bgPaint)

    val totalBarModules = 95 // Total width of bars in standard UPC-A

    var startX = 10 // Start position for the first bar

    drawBar(canvas,barPaint,startX, 5, BarcodeView.BARCODE_LINE_WIDTH, 125)
    startX += BarcodeView.BARCODE_LINE_WIDTH
    drawBar(canvas,bgPaint,startX, 5 , BarcodeView.BARCODE_LINE_WIDTH , 125)
    startX += BarcodeView.BARCODE_LINE_WIDTH
    drawBar(canvas,barPaint,startX, 5, BarcodeView.BARCODE_LINE_WIDTH, 125)
    startX += BarcodeView.BARCODE_LINE_WIDTH

}

// Function to draw the center guard bar
private fun drawCenterGuardBar(canvas: Canvas, paint: Paint, x: Int, height: Int) {
    val barWidth = BarcodeView.BARCODE_LINE_WIDTH.toFloat()
    val currentX = x + (barWidth / 2) // Center the guard

    // Draw the center guard pattern with full height
    canvas.drawRect(currentX, 0f, currentX + barWidth, height.toFloat(), paint) // Narrow bar
    canvas.drawRect(currentX + barWidth, 0f, currentX + 2 * barWidth, height * 0.5f, paint) // Narrow space
    canvas.drawRect(currentX + 2 * barWidth, 0f, currentX + 3 * barWidth, height.toFloat(), paint) // Narrow bar
    canvas.drawRect(currentX + 3 * barWidth, 0f, currentX + 4 * barWidth, height * 0.5f, paint) // Narrow space
    canvas.drawRect(currentX + 4 * barWidth, 0f, currentX + 5 * barWidth, height.toFloat(), paint) // Narrow bar
}

// Helper functions
private fun drawBar(canvas: Canvas, paint: Paint, x: Int, y: Int, width: Int, height: Int) {
    canvas.drawRect(Rect(x, y, x + width, height), paint)
}

private fun drawPattern(canvas: Canvas, paint: Paint, x: Int, pattern: Int, width: Int, height: Int) {
    var currentX = x
    val barWidth = width / 7 // Calculate individual bar width for the pattern

    for (i in 0..6) {
        if ((pattern shr (6 - i)) and 1 == 1) {
            drawBar(canvas, paint, currentX, 0, barWidth, height)
        }
        currentX += barWidth
    }
}
