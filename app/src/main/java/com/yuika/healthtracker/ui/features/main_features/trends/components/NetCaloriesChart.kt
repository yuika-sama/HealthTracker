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
import com.yuika.healthtracker.ui.theme.Emerald

@Composable
fun NetCaloriesChart(
    modifier: Modifier = Modifier
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
                val width = size.width
                val height = size.height
                
                // Mock points for a smooth curve
                val points = listOf(
                    Offset(0f, height * 0.8f),
                    Offset(width * 0.25f, height * 0.7f),
                    Offset(width * 0.5f, height * 0.4f),
                    Offset(width * 0.75f, height * 0.2f),
                    Offset(width, height * 0.25f)
                )

                // Draw Gradient Fill
                val fillPath = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    
                    // Simple cubic bezier curve mockup
                    cubicTo(
                        width * 0.125f, height * 0.8f,
                        width * 0.125f, height * 0.7f,
                        points[1].x, points[1].y
                    )
                    cubicTo(
                        width * 0.375f, height * 0.7f,
                        width * 0.375f, height * 0.4f,
                        points[2].x, points[2].y
                    )
                    cubicTo(
                        width * 0.625f, height * 0.4f,
                        width * 0.625f, height * 0.2f,
                        points[3].x, points[3].y
                    )
                    cubicTo(
                        width * 0.875f, height * 0.2f,
                        width * 0.875f, height * 0.25f,
                        points[4].x, points[4].y
                    )

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

                // Draw Stroke
                val strokePath = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    cubicTo(
                        width * 0.125f, height * 0.8f,
                        width * 0.125f, height * 0.7f,
                        points[1].x, points[1].y
                    )
                    cubicTo(
                        width * 0.375f, height * 0.7f,
                        width * 0.375f, height * 0.4f,
                        points[2].x, points[2].y
                    )
                    cubicTo(
                        width * 0.625f, height * 0.4f,
                        width * 0.625f, height * 0.2f,
                        points[3].x, points[3].y
                    )
                    cubicTo(
                        width * 0.875f, height * 0.2f,
                        width * 0.875f, height * 0.25f,
                        points[4].x, points[4].y
                    )
                }

                drawPath(
                    path = strokePath,
                    color = Emerald,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Draw Dots (at specified intermediate points)
                val dotPoints = listOf(points[1], points[2], points[3])
                dotPoints.forEach { point ->
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
