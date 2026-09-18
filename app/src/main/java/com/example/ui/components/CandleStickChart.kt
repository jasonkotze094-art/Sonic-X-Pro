package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CandleStick
import com.example.ui.theme.SonicBorder
import com.example.ui.theme.SonicGreen
import com.example.ui.theme.SonicRed
import com.example.ui.theme.SonicSurfaceVariant
import com.example.ui.theme.TextSecondary

@Composable
fun CandleStickChart(
    candles: List<CandleStick>,
    currentPrice: Double,
    symbol: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(SonicSurfaceVariant, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$symbol, M1",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = " • Candlestick",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Text(
                    text = String.format("%.2f", currentPrice),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = SonicGreen
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 8.dp)
            ) {
                if (candles.isNotEmpty()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val minPrice = candles.minOf { it.low } - 5f
                        val maxPrice = candles.maxOf { it.high } + 5f
                        val priceRange = if (maxPrice - minPrice > 0f) maxPrice - minPrice else 1f

                        val candleWidth = (size.width / candles.size.toFloat()).coerceAtLeast(6f)
                        val spacing = candleWidth * 0.25f
                        val bodyWidth = candleWidth - spacing

                        // Draw background grid lines
                        val gridLines = 4
                        for (i in 0..gridLines) {
                            val y = (size.height / gridLines) * i
                            drawLine(
                                color = SonicBorder.copy(alpha = 0.5f),
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = 1f
                            )
                        }

                        // Draw moving average path
                        val maPath = Path()
                        var maStarted = false

                        candles.forEachIndexed { index, candle ->
                            val x = index * candleWidth + (bodyWidth / 2f)

                            val openY = size.height - ((candle.open - minPrice) / priceRange) * size.height
                            val closeY = size.height - ((candle.close - minPrice) / priceRange) * size.height
                            val highY = size.height - ((candle.high - minPrice) / priceRange) * size.height
                            val lowY = size.height - ((candle.low - minPrice) / priceRange) * size.height

                            val isBullish = candle.close >= candle.open
                            val candleColor = if (isBullish) SonicGreen else SonicRed

                            // Draw wick
                            drawLine(
                                color = candleColor,
                                start = Offset(x, highY),
                                end = Offset(x, lowY),
                                strokeWidth = 1.5f
                            )

                            // Draw body
                            val topY = minOf(openY, closeY)
                            val bodyHeight = (kotlin.math.abs(openY - closeY)).coerceAtLeast(2f)

                            drawRect(
                                color = candleColor,
                                topLeft = Offset(x - bodyWidth / 2f, topY),
                                size = Size(bodyWidth, bodyHeight)
                            )

                            // Moving average curve point
                            val midY = (openY + closeY) / 2f
                            if (!maStarted) {
                                maPath.moveTo(x, midY)
                                maStarted = true
                            } else {
                                maPath.lineTo(x, midY)
                            }
                        }

                        // Draw smooth MA line
                        drawPath(
                            path = maPath,
                            color = Color(0xFF00E5FF).copy(alpha = 0.7f),
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // Draw live current price line
                        val currentY = size.height - ((currentPrice.toFloat() - minPrice) / priceRange) * size.height
                        drawLine(
                            color = SonicRed,
                            start = Offset(0f, currentY),
                            end = Offset(size.width, currentY),
                            strokeWidth = 1.5f
                        )
                    }
                }
            }
        }
    }
}
