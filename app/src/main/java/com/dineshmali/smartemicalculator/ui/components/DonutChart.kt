package com.dineshmali.smartemicalculator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dineshmali.smartemicalculator.ui.theme.InterestColor
import com.dineshmali.smartemicalculator.ui.theme.PrincipalColor
import com.dineshmali.smartemicalculator.utils.CurrencyFormatter

@Composable
fun DonutChart(
    principalAmount: Double,
    interestAmount: Double,
    principalPercentage: Float,
    interestPercentage: Float,
    modifier: Modifier = Modifier,
    chartSize: Dp = 170.dp,
    strokeWidth: Dp = 22.dp
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(chartSize),
            contentAlignment = Alignment.Center
        ) {
            val trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)

            Canvas(modifier = Modifier.size(chartSize)) {
                val principalSweep = (principalPercentage / 100f) * 360f
                val interestSweep = (interestPercentage / 100f) * 360f

                // Draw Base Track
                drawArc(
                    color = trackColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx())
                )

                // Draw Principal Arc
                if (principalSweep > 0f) {
                    drawArc(
                        color = PrincipalColor,
                        startAngle = -90f,
                        sweepAngle = principalSweep,
                        useCenter = false,
                        style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Draw Interest Arc
                if (interestSweep > 0f) {
                    drawArc(
                        color = InterestColor,
                        startAngle = -90f + principalSweep,
                        sweepAngle = interestSweep,
                        useCenter = false,
                        style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "PRINCIPAL",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Text(
                    text = "${principalPercentage.toInt()}%",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    ),
                    color = PrincipalColor
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Legends Pill Container
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(
                color = PrincipalColor,
                label = "Principal",
                value = CurrencyFormatter.formatInr(principalAmount),
                percentage = String.format("%.1f%%", principalPercentage)
            )

            LegendItem(
                color = InterestColor,
                label = "Interest",
                value = CurrencyFormatter.formatInr(interestAmount),
                percentage = String.format("%.1f%%", interestPercentage)
            )
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    value: String,
    percentage: String
) {
    Box(
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "$label ($percentage)",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = color
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
