package cz.kudladev.exec01.core.presentation.screens.scanner_screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import cz.kudladev.exec01.core.presentation.components.BottomAppNavBar
import cz.kudladev.exec01.core.presentation.components.NavDrawer
import cz.kudladev.exec01.core.presentation.components.TopAppBarWithDrawer
import kotlinx.coroutines.launch
import kotlin.times


@Composable
fun ScannerScreen(
    modifier: Modifier = Modifier,
    state: ScannerScreenState,
    onEvent: (ScannerScreenEvent) -> Unit,
    navController: NavController
) {

    val context = LocalContext.current

    var scanned by remember {
        mutableStateOf("")
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val barcodeLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        if (result.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            scanned = "Canceled"
        } else {
            Toast.makeText(context, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            onEvent(ScannerScreenEvent.setCodeText(result.contents.dropLast(1)))
        }
    }
    val options = ScanOptions()
    options.setOrientationLocked(false)
    options.setBeepEnabled(true)
    options.setDesiredBarcodeFormats("UPC_A")
    options.setPrompt("Scan a barcode")


    val transparentColor = androidx.compose.ui.graphics.Color.Transparent


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            barcodeLauncher.launch(options)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }



    NavDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBarWithDrawer(
                    title = "Scanner",
                    icon = Icons.Default.Menu,
                    onIconClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {



                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(5))
                        .background(if (state.codeText.length == 11) White else transparentColor)
                ) {
                    Canvas(modifier = modifier.fillMaxWidth().height(200.dp)) {
                        if (state.codeText.length == 11) {
                            val checksum = calculateUPCAChecksum(state.codeText)
                            val fullUPC = state.codeText + checksum
                            drawUPCABarcode(fullUPC)
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = state.codeText,
                        modifier = Modifier
                            .weight(1f),
                        onValueChange = {
                            onEvent(ScannerScreenEvent.setCodeText(it))
                        },
                        placeholder = {
                            Text(text = "Enter UPC code")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                        shape = RoundedCornerShape(
                            topStart = 30.dp,
                            bottomStart = 30.dp,
                            topEnd = 0.dp,
                            bottomEnd = 0.dp
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = transparentColor,
                            unfocusedIndicatorColor = transparentColor
                        )
                    )
                    Button(
                        onClick = {
                            val permissionCheckResult =
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA,
                                )
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {

                                barcodeLauncher.launch(options)
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier
                            .height(TextFieldDefaults.MinHeight),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            topEnd = 30.dp,
                            bottomEnd = 30.dp
                        )
                    ) {
                        Text("Scan")
                    }
                }
            }
        }
    }
}

@Throws(WriterException::class)
fun encodeAsBitmap(str: String?): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, 400, 400)

    val w = bitMatrix.width
    val h = bitMatrix.height
    val pixels = IntArray(w * h)
    for (y in 0 until h) {
        for (x in 0 until w) {
            pixels[y * w + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
        }
    }

    val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
    return bitmap
}


fun DrawScope.drawUPCABarcode(upc: String) {
    val startX = 50f
    val startY = 70f
    val barWidth = 10f // updated
    val barHeight = size.height * 0.75f // updated

    val upcBinary = getUPCABinary(upc)

    upcBinary.forEachIndexed { index, char ->
        val color = if (char == '1') Black else White
        drawRect(
            color = color,
            topLeft = androidx.compose.ui.geometry.Offset(startX + index * barWidth, startY),
            size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
        )
    }
}

fun calculateUPCAChecksum(upc: String): Char {
    val oddSum = upc.filterIndexed { index, _ -> index % 2 == 0 }.sumBy { it.toString().toInt() }
    val evenSum = upc.filterIndexed { index, _ -> index % 2 == 1 }.sumBy { it.toString().toInt() }

    val totalSum = oddSum * 3 + evenSum
    val checksum = (10 - (totalSum % 10)) % 10
    return checksum.toString().single()
}

fun getUPCABinary(upc: String): String {
    val leftCodes = mapOf(
        '0' to "0001101", '1' to "0011001", '2' to "0010011", '3' to "0111101",
        '4' to "0100011", '5' to "0110001", '6' to "0101111", '7' to "0111011",
        '8' to "0110111", '9' to "0001011"
    )
    val rightCodes = mapOf(
        '0' to "1110010", '1' to "1100110", '2' to "1101100", '3' to "1000010",
        '4' to "1011100", '5' to "1001110", '6' to "1010000", '7' to "1000100",
        '8' to "1001000", '9' to "1110100"
    )

    val leftPart = upc.substring(0, 6)
    val rightPart = upc.substring(6)

    // Guard patterns and middle
    val leftGuard = "101"
    val centerGuard = "01010"
    val rightGuard = "101"

    val leftBinary = leftPart.map { leftCodes[it] }.joinToString("")
    val rightBinary = rightPart.map { rightCodes[it] }.joinToString("")

    return leftGuard + leftBinary + centerGuard + rightBinary + rightGuard
}


