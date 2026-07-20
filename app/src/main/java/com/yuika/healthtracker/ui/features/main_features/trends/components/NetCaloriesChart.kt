package com.yuika.healthtracker.ui.features.main_features.trends.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.R
import com.yuika.healthtracker.ui.features.main_features.trends.ChartDataPoint

@Composable
fun NetCaloriesChart(
    modifier: Modifier = Modifier,
    dataPoints: List<ChartDataPoint>,
    onPointClick: (ChartDataPoint) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.trends_net_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.trends_net_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        val lineColor = MaterialTheme.colorScheme.secondary
        val surfaceColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f)
        val dotColor = MaterialTheme.colorScheme.surfaceVariant
        val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)
        val zeroLineColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(surfaceColor)
                .padding(12.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(156.dp)) {
                if (dataPoints.isEmpty()) return@Canvas
                
                val width = size.width
                val height = size.height
                val chartPadding = 10.dp.toPx()
                
                val maxVal = dataPoints.maxOfOrNull { it.value }?.coerceAtLeast(0f) ?: 0f
                val minVal = dataPoints.minOfOrNull { it.value }?.coerceAtMost(0f) ?: 0f
                val range = (maxVal - minVal).coerceAtLeast(1f)
                val zeroY = chartPadding + (height - chartPadding * 2) * (1f - ((0f - minVal) / range))

                repeat(4) { index ->
                    val y = chartPadding + (height - chartPadding * 2) * index / 3f
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                drawLine(
                    color = zeroLineColor,
                    start = Offset(0f, zeroY),
                    end = Offset(width, zeroY),
                    strokeWidth = 1.dp.toPx()
                )
                
                val points = dataPoints.mapIndexed { index, dataPoint ->
                    val x = if (dataPoints.size > 1) {
                        chartPadding + ((width - chartPadding * 2) / (dataPoints.size - 1)) * index
                    } else {
                        width / 2f
                    }
                    val normalizedY = 1f - ((dataPoint.value - minVal) / range)
                    val y = chartPadding + (height - chartPadding * 2) * normalizedY
                    Offset(x, y)
                }

                if (points.size == 1) {
                    val p = points.first()
                    drawCircle(color = lineColor, radius = 5.dp.toPx(), center = p)
                    return@Canvas
                }

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
                    lineTo(points.last().x, zeroY)
                    lineTo(points.first().x, zeroY)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            lineColor.copy(alpha = 0.24f),
                            lineColor.copy(alpha = 0.02f)
                        ),
                        startY = 0f,
                        endY = height
                    )
                )

                drawPath(
                    path = strokePath,
                    color = lineColor,
                    style = Stroke(width = 3.dp.toPx())
                )

                points.forEachIndexed { index, point ->
                    if (dataPoints.size > 12 && index != 0 && index != points.lastIndex && index % 3 != 0) {
                        return@forEachIndexed
                    }
                    drawCircle(
                        color = dotColor,
                        radius = 5.dp.toPx(),
                        center = point
                    )
                    drawCircle(
                        color = lineColor,
                        radius = 5.dp.toPx(),
                        center = point,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dataPoints.forEachIndexed { index, point ->
                val showLabel = dataPoints.size <= 8 ||
                    index == 0 ||
                    index == dataPoints.lastIndex ||
                    index == dataPoints.lastIndex / 2
                Text(
                    text = if (showLabel) point.label else "",
                    modifier = Modifier
                        .weight(1f)
                        .clickable{onPointClick(point)},
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
