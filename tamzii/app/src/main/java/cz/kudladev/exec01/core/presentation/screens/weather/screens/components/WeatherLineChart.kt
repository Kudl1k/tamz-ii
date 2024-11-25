package cz.kudladev.exec01.core.presentation.screens.weather.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun WeatherLineChart(
    modifier: Modifier = Modifier,
    data: List<Pair<Float, Double>>,
    time: List<String>
) {
    val context = LocalContext.current
    val chart = remember { LineChart(context) }
    val color1 = MaterialTheme.colorScheme.primary.toArgb()
    val textColor = MaterialTheme.colorScheme.onBackground.toArgb()

    // Format the time list to only display hours (e.g., "14:00") if needed
    val formattedTime = time.map { timeString ->
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Adjust format to your input
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        try {
            outputFormat.format(inputFormat.parse(timeString) ?: timeString)
        } catch (e: Exception) {
            timeString // Fallback to the original string if parsing fails
        }
    }

    // Update chart data when data changes
    LaunchedEffect(data) {
        val entries = data.mapIndexed { index, pair ->
            Entry(index.toFloat(), pair.second.toFloat())
        }

        if (entries.isNotEmpty()) {
            val dataSet = LineDataSet(entries, "Temperature").apply {
                valueTextSize = 12f
                color = color1
                valueTextColor = textColor
                setDrawValues(false)
                setDrawCircles(false)
            }

            val lineData = LineData(dataSet)
            chart.data = lineData

            // Configure x-axis with custom labels (formatted hours)
            chart.xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(formattedTime)
                granularity = 1f // Ensures one label per entry
                setDrawGridLines(false)
                position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            }

            // Configure other chart properties
            chart.description.isEnabled = false
            chart.axisLeft.setDrawGridLines(false)
            chart.axisRight.setDrawGridLines(false)
            chart.axisLeft.textColor = textColor
            chart.axisLeft.isEnabled = true
            chart.axisRight.isEnabled = false

            chart.setTouchEnabled(false)
            // Refresh the chart
            chart.invalidate()
        }
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { chart }
    )
}