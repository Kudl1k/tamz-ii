import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import cz.kudladev.exec01.core.presentation.components.NavDrawer
import cz.kudladev.exec01.core.presentation.components.TopAppBarWithDrawer
import cz.kudladev.exec01.core.presentation.screens.face_recognition.FaceRecognitionEvents
import cz.kudladev.exec01.core.presentation.screens.face_recognition.FaceRecognitionState
import kotlinx.coroutines.launch

@Composable
fun FaceRecognition(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: FaceRecognitionState,
    onEvent: (FaceRecognitionEvents) -> Unit
) {
    val context = LocalContext.current

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val highAccuracyOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    val detector = FaceDetection.getClient(highAccuracyOpts)

    var faces by remember { mutableStateOf<List<Face>>(emptyList()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onEvent(FaceRecognitionEvents.ImageSelected(it))
            Toast.makeText(context, "Image selected: $uri", Toast.LENGTH_SHORT).show()
        }
    }

    NavDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBarWithDrawer(
                    title = "Rozpoznávání obličeje",
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
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(it)
            ) {
                Column(
                    modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            launcher.launch("image/*")
                        }
                    ) {
                        Text("Vybrat obrázek")
                    }
                    state.selectedImageUri?.let { uri ->
                        val bitmapImage = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
                        val image = InputImage.fromBitmap(bitmapImage, 0)
                        val imageBitmap = bitmapImage.asImageBitmap()

                        val overlayPainter = remember(faces) {
                            object : Painter() {
                                override val intrinsicSize: Size
                                    get() = Size.Unspecified

                                override fun DrawScope.onDraw() {
                                    drawImage(imageBitmap)
                                    faces.forEach { face ->

                                        // Draw landmarks
                                        face.allLandmarks.forEach { landmark ->
                                            drawCircle(
                                                color = Color.Blue,
                                                center = Offset(landmark.position.x, landmark.position.y),
                                                radius = 8f
                                            )
                                        }

                                        // Draw contours
                                        face.allContours.forEach { contour ->
                                            val path = Path().apply {
                                                contour.points.forEachIndexed { index, point ->
                                                    if (index == 0) moveTo(point.x, point.y)
                                                    else lineTo(point.x, point.y)
                                                }
                                            }
                                            drawPath(path, color = Color.Green, style = Stroke(width = 4f))
                                        }
                                    }
                                }
                            }
                        }

                        Image(
                            painter = overlayPainter,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().weight(1f)
                        )

                        Button(
                            onClick = {
                                detector.process(image)
                                    .addOnSuccessListener { detectedFaces ->
                                        faces = detectedFaces
                                        Toast.makeText(context, "Faces detected: ${detectedFaces.size}", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Face detection failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        ) {
                            Text("Rozpoznat obličej")
                        }
                    }
                }
            }
        }
    }
}