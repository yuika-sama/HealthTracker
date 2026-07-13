package com.yuika.healthtracker.ui.features.main_features.trends.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.ui.features.main_features.trends.ChartDataPoint
import com.yuika.healthtracker.ui.theme.Emerald

@Composable
fun NetCaloriesChart(
    modifier: Modifier = Modifier,
    dataPoints: List<ChartDataPoint>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Net Calories Trend",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                if (dataPoints.isEmpty()) return@Canvas
                
                val width = size.width
                val height = size.height
                
                val maxVal = dataPoints.maxOfOrNull { it.value }?.coerceAtLeast(100f) ?: 100f
                val minVal = dataPoints.minOfOrNull { it.value }?.coerceAtMost(0f) ?: 0f
                val range = (maxVal - minVal).coerceAtLeast(1f)
                
                val points = dataPoints.mapIndexed { index, dataPoint ->
                    val x = if (dataPoints.size > 1) {
                        (width / (dataPoints.size - 1)) * index
                    } else {
                        width / 2f
                    }
                    val normalizedY = 1f - ((dataPoint.value - minVal) / range)
                    val y = height * normalizedY
                    Offset(x, y)
                }

                // If we only have 1 point, just draw a line in the middle
                if (points.size == 1) {
                    val p = points.first()
                    drawCircle(color = Emerald, radius = 6.dp.toPx(), center = p)
                    return@Canvas
                }

                // Create smooth path using cubic bezier curves
                val strokePath = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (i in 0 until points.size - 1) {
                        val p1 = points[i]
                        val p2 = points[i + 1]
                        val cx = (p1.x + p2.x) / 2f
                        cubicTo(cx, p1.y, cx, p2.y, p2.x, p2.y)
                    }
                }

                val fillPath = Path().apply {
                    addPath(strokePath)
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Emerald.copy(alpha = 0.2f),
                            Emerald.copy(alpha = 0.0f)
                        ),
                        startY = 0f,
                        endY = height
                    )
                )

                drawPath(
                    path = strokePath,
                    color = Emerald,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Draw Dots on intermediate points
                points.forEach { point ->
                    drawCircle(
                        color = Color.White,
                        radius = 6.dp.toPx(),
                        center = point
                    )
                    drawCircle(
                        color = Emerald,
                        radius = 6.dp.toPx(),
                        center = point,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
        }
    }
}
