package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.EASettings
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
fun HomeScreen(
    eaSettings: EASettings,
    activeSymbol: String,
    currentBid: Double,
    isExecuting: Boolean,
    executionBannerText: String?,
    currentExecutionStep: String?,
    isScanning: Boolean,
    scanProgress: Float,
    scanResult: TradeSignal?,
    onToggleAutoTrading: () -> Unit,
    onSelectSymbol: (String) -> Unit,
    onSelectEa: (String) -> Unit,
    onTriggerExecution: (String, String, Int) -> Unit,
    onScanChart: () -> Unit,
    onNavigateToTrade: () -> Unit,
    onNavigateToSignals: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var showSymbolDropdown by remember { mutableStateOf(false) }

    val availableSymbols = listOf("BTCUSDm", "EURUSD", "XAUUSD", "GBPUSD", "US30", "NAS100")
    val eaModes = listOf(
        "Sonic X Pro Scalper",
        "Sonic Trend Rider",
        "iTradeBot Grid Pro",
        "Sonic AI Momentum"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        // Status & Connectivity Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F131E))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(SonicGreen, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "MT5 Connected: Exness (435738583)",
                    fontSize = 11.sp,
                    color = SonicGreen,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier
                    .background(Color(0xFF1E293B), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Both",
                    fontSize = 10.sp,
                    color = SonicRed,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Hero Banner with Knuckles Character (exact visual from video)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1E0A12), Color(0xFF121622), Color(0xFF0C0F17))
                    )
                )
                .border(BorderStroke(1.dp, SonicBorder), RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Character image
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_sonic_knuckles_hero),
                        contentDescription = "Sonic X Pro Knuckles Bot",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Online live indicator on top corner
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(Color(0xCC000000), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(SonicGreen, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "ONLINE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = SonicGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "SONIC X PRO",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = TextPrimary
                )

                Text(
                    text = "Powered By iTradeBot.org",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SonicCyan
                )
            }
        }

        // Actions Section (exact controls as seen in the video)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Actions",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // STOP / START button
                Button(
                    onClick = onToggleAutoTrading,
                    modifier = Modifier
                        .weight(1.2f)
                        .height(54.dp)
                        .testTag("action_stop_start_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (eaSettings.isAutoTrading) SonicRed else SonicGreen
                    )
                ) {
                    Icon(
                        imageVector = if (eaSettings.isAutoTrading) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = if (eaSettings.isAutoTrading) "STOP" else "START",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (eaSettings.isAutoTrading) "STOP" else "START",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                // SYMBOLS button
                Box(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = { showSymbolDropdown = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("action_symbols_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SonicSurfaceVariant),
                        border = BorderStroke(1.dp, SonicBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CurrencyExchange,
                            contentDescription = "Symbols",
                            tint = SonicCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SYMBOLS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    DropdownMenu(
                        expanded = showSymbolDropdown,
                        onDismissRequest = { showSymbolDropdown = false },
                        modifier = Modifier.background(SonicSurface)
                    ) {
                        availableSymbols.forEach { sym ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = sym,
                                        fontWeight = if (sym == activeSymbol) FontWeight.Bold else FontWeight.Normal,
                                        color = if (sym == activeSymbol) SonicCyan else TextPrimary
                                    )
                                },
                                onClick = {
                                    onSelectSymbol(sym)
                                    showSymbolDropdown = false
                                }
                            )
                        }
                    }
                }

                // SIGNALS button
                Button(
                    onClick = onNavigateToSignals,
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .testTag("action_signals_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48))
                ) {
                    Icon(
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = "Signals",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SIGNALS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Select EA to Start section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Select EA to Start",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(end = 16.dp)
            ) {
                items(eaModes) { mode ->
                    val isSelected = mode == eaSettings.eaName
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectEa(mode) },
                        label = {
                            Text(
                                text = mode,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SonicRed,
                            selectedLabelColor = Color.White,
                            containerColor = SonicSurfaceVariant,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) SonicRed else SonicBorder,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Execution & Status Live Progress Card (seen in video overlay)
        if (executionBannerText != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("execution_banner_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141926)),
                border = BorderStroke(1.dp, if (isExecuting) SonicCyan else SonicBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isExecuting) Icons.Default.Sensors else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (isExecuting) SonicCyan else SonicGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = executionBannerText,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }

                    if (isExecuting) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = SonicCyan,
                            trackColor = SonicSurfaceVariant
                        )
                    }

                    if (currentExecutionStep != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentExecutionStep,
                            fontSize = 11.sp,
                            color = SonicGreen,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // AI Chart Scanner Card with "NEW" Badge (exact component from video)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("ai_chart_scanner_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SonicSurface),
            border = BorderStroke(1.dp, SonicBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(SonicCyan.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = "AI Scanner",
                                tint = SonicCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "AI Chart Scanner",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(SonicCyan, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "NEW",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black
                                    )
                                }
                            }
                            Text(
                                text = "Scan any chart & get instant signals",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Interactive Scanner Trigger
                if (isScanning) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SonicSurfaceVariant, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Analyzing Order Flow & Wyckoff Reversals...",
                                fontSize = 11.sp,
                                color = SonicCyan
                            )
                            Text(
                                text = "${(scanProgress * 100).toInt()}%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SonicCyan
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { scanProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = SonicCyan,
                            trackColor = Color(0xFF0F172A)
                        )
                    }
                } else {
                    Button(
                        onClick = onScanChart,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("scan_now_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        border = BorderStroke(1.dp, SonicCyan.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = SonicCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scan $activeSymbol Chart Now",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SonicCyan
                        )
                    }
                }

                // Display Scan Result if generated
                if (scanResult != null && !isScanning) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF111827), RoundedCornerShape(12.dp))
                            .border(BorderStroke(1.dp, SonicGreen.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${scanResult.symbol} • ${scanResult.action}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (scanResult.action == "SELL") SonicRed else SonicGreen
                                )
                                Text(
                                    text = "${scanResult.confidence}% Win Probability",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SonicGreen
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Entry: ${scanResult.entryPrice} | TP1: ${scanResult.tp1} | SL: ${scanResult.stopLoss}",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    onTriggerExecution(scanResult.symbol, scanResult.action, scanResult.tradesCount)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .testTag("execute_scanned_signal_button"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SonicRed)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FlashOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Execute Signal (10 Trades on MT5)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Quick 1-Tap Manual Execution Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { onTriggerExecution(activeSymbol, "SELL", 10) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("manual_sell_10_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SonicRed)
            ) {
                Text(
                    text = "SELL 10x $activeSymbol",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Button(
                onClick = { onTriggerExecution(activeSymbol, "BUY", 10) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("manual_buy_10_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SonicGreen)
            ) {
                Text(
                    text = "BUY 10x $activeSymbol",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}
