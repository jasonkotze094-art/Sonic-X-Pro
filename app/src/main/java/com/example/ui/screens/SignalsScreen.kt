package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TradeSignal
import com.example.ui.theme.SonicBorder
import com.example.ui.theme.SonicCyan
import com.example.ui.theme.SonicGreen
import com.example.ui.theme.SonicRed
import com.example.ui.theme.SonicSurface
import com.example.ui.theme.SonicSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SignalsScreen(
    signals: List<TradeSignal>,
    onExecuteSignal: (String, String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0D14))
            .padding(16.dp)
    ) {
        // Signals Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "VIP Signals Feed",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = "AI-Driven Algorithmic Signals with 90%+ Win Rate",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .background(SonicGreen.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = SonicGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "VIP Active",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SonicGreen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Preset / Room Signals List
        val displaySignals = if (signals.isNotEmpty()) signals else listOf(
            TradeSignal(
                id = "SIG-9821",
                symbol = "BTCUSDm",
                action = "SELL",
                entryPrice = 61811.48,
                tp1 = 61453.16,
                tp2 = 61200.00,
                stopLoss = 62040.48,
                confidence = 96,
                tradesCount = 10,
                timeframe = "M1",
                strategy = "Sonic X Pro Scalper"
            ),
            TradeSignal(
                id = "SIG-9822",
                symbol = "XAUUSD",
                action = "BUY",
                entryPrice = 2341.50,
                tp1 = 2355.00,
                tp2 = 2368.00,
                stopLoss = 2332.00,
                confidence = 94,
                tradesCount = 10,
                timeframe = "H1",
                strategy = "Institutional Gold Breaker"
            ),
            TradeSignal(
                id = "SIG-9823",
                symbol = "EURUSD",
                action = "SELL",
                entryPrice = 1.08450,
                tp1 = 1.08100,
                tp2 = 1.07850,
                stopLoss = 1.08700,
                confidence = 92,
                tradesCount = 10,
                timeframe = "M15",
                strategy = "Smart Money Divergence"
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displaySignals, key = { it.id }) { sig ->
                SignalItemCard(
                    signal = sig,
                    onExecute = {
                        onExecuteSignal(sig.symbol, sig.action, sig.tradesCount)
                    }
                )
            }
        }
    }
}

@Composable
fun SignalItemCard(
    signal: TradeSignal,
    onExecute: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("signal_card_${signal.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SonicSurface),
        border = BorderStroke(1.dp, SonicBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = signal.symbol,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                if (signal.action == "SELL") SonicRed else SonicGreen,
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = signal.action,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (signal.action == "SELL") Color.White else Color.Black
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = null,
                        tint = SonicCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${signal.confidence}% Win Rate",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SonicCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Strategy: ${signal.strategy} (${signal.timeframe})",
                fontSize = 11.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Price Levels Grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SonicSurfaceVariant, RoundedCornerShape(10.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Entry", fontSize = 10.sp, color = TextSecondary)
                    Text(
                        text = "${signal.entryPrice}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TextPrimary
                    )
                }

                Column {
                    Text(text = "TP 1", fontSize = 10.sp, color = TextSecondary)
                    Text(
                        text = "${signal.tp1}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = SonicGreen
                    )
                }

                Column {
                    Text(text = "TP 2", fontSize = 10.sp, color = TextSecondary)
                    Text(
                        text = "${signal.tp2}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = SonicGreen
                    )
                }

                Column {
                    Text(text = "Stop Loss", fontSize = 10.sp, color = TextSecondary)
                    Text(
                        text = "${signal.stopLoss}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = SonicRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onExecute,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("execute_signal_button_${signal.id}"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (signal.action == "SELL") SonicRed else SonicGreen
                )
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = null,
                    tint = if (signal.action == "SELL") Color.White else Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Execute ${signal.action} (${signal.tradesCount} Trades on MT5)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (signal.action == "SELL") Color.White else Color.Black
                )
            }
        }
    }
}
