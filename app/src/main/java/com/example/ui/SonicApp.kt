package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.FloatingTradingWidget
import com.example.ui.components.OrderTicketDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MetaTraderScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SignalsScreen
import com.example.ui.theme.SonicBorder
import com.example.ui.theme.SonicCyan
import com.example.ui.theme.SonicGreen
import com.example.ui.theme.SonicRed
import com.example.ui.theme.SonicSurface
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.SonicTradingViewModel

@Composable
fun SonicApp(
    viewModel: SonicTradingViewModel = viewModel()
) {
    var currentScreen by remember { mutableIntStateOf(0) } // 0: Home, 1: MetaTrader, 2: Signals, 3: Profile

    val openPositions by viewModel.openPositions.collectAsStateWithLifecycle()
    val closedPositions by viewModel.closedPositions.collectAsStateWithLifecycle()
    val signals by viewModel.signals.collectAsStateWithLifecycle()
    val account by viewModel.account.collectAsStateWithLifecycle()
    val eaSettings by viewModel.eaSettings.collectAsStateWithLifecycle()
    val activeSymbol by viewModel.activeSymbol.collectAsStateWithLifecycle()
    val currentBid by viewModel.currentBid.collectAsStateWithLifecycle()
    val isExecuting by viewModel.isExecuting.collectAsStateWithLifecycle()
    val executionBannerText by viewModel.executionBannerText.collectAsStateWithLifecycle()
    val currentExecutionStep by viewModel.currentExecutionStep.collectAsStateWithLifecycle()
    val ticketPopup by viewModel.ticketPopup.collectAsStateWithLifecycle()
    val isFloatingWidgetVisible by viewModel.isFloatingWidgetVisible.collectAsStateWithLifecycle()
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val scanProgress by viewModel.scanProgress.collectAsStateWithLifecycle()
    val scanResult by viewModel.scanResult.collectAsStateWithLifecycle()
    val candles by viewModel.candles.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("sonic_app_root"),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("sonic_bottom_nav"),
                containerColor = Color(0xFF0F131E),
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentScreen == 0,
                    onClick = { currentScreen = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = {
                        Text(
                            text = "Home",
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SonicRed,
                        selectedTextColor = SonicRed,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = SonicRed.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == 1,
                    onClick = { currentScreen = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CandlestickChart,
                            contentDescription = "MetaTrader"
                        )
                    },
                    label = {
                        Text(
                            text = "MetaTrader",
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SonicCyan,
                        selectedTextColor = SonicCyan,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = SonicCyan.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == 2,
                    onClick = { currentScreen = 2 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = "Signals"
                        )
                    },
                    label = {
                        Text(
                            text = "Signals",
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == 2) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SonicGreen,
                        selectedTextColor = SonicGreen,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = SonicGreen.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == 3,
                    onClick = { currentScreen = 3 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = {
                        Text(
                            text = "Profile",
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == 3) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFF59E0B),
                        selectedTextColor = Color(0xFFF59E0B),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = Color(0xFFF59E0B).copy(alpha = 0.15f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                0 -> HomeScreen(
                    eaSettings = eaSettings,
                    activeSymbol = activeSymbol,
                    currentBid = currentBid,
                    isExecuting = isExecuting,
                    executionBannerText = executionBannerText,
                    currentExecutionStep = currentExecutionStep,
                    isScanning = isScanning,
                    scanProgress = scanProgress,
                    scanResult = scanResult,
                    onToggleAutoTrading = { viewModel.toggleAutoTrading() },
                    onSelectSymbol = { viewModel.setActiveSymbol(it) },
                    onSelectEa = { viewModel.setEa(it) },
                    onTriggerExecution = { sym, act, cnt -> viewModel.triggerSignalExecution(sym, act, cnt) },
                    onScanChart = { viewModel.scanChart() },
                    onNavigateToTrade = { currentScreen = 1 },
                    onNavigateToSignals = { currentScreen = 2 }
                )

                1 -> MetaTraderScreen(
                    account = account,
                    openPositions = openPositions,
                    closedPositions = closedPositions,
                    candles = candles,
                    currentBid = currentBid,
                    activeSymbol = activeSymbol,
                    onClosePosition = { viewModel.closePosition(it) },
                    onCloseAllPositions = { viewModel.closeAllPositions() },
                    onTriggerExecution = { sym, act, cnt -> viewModel.triggerSignalExecution(sym, act, cnt) }
                )

                2 -> SignalsScreen(
                    signals = signals,
                    onExecuteSignal = { sym, act, cnt ->
                        viewModel.triggerSignalExecution(sym, act, cnt)
                        currentScreen = 1
                    }
                )

                3 -> ProfileScreen(
                    account = account,
                    eaSettings = eaSettings,
                    isFloatingWidgetVisible = isFloatingWidgetVisible,
                    onToggleAutoTrading = { viewModel.toggleAutoTrading() },
                    onToggleFloatingWidget = { viewModel.toggleFloatingWidget() },
                    onSetLotSize = { viewModel.setLotSize(it) },
                    onSetTradesCount = { viewModel.setTradesPerSignal(it) }
                )
            }

            // Floating Draggable Robot Widget (as seen on phone screen in video)
            if (isFloatingWidgetVisible) {
                FloatingTradingWidget(
                    isAutoTrading = eaSettings.isAutoTrading,
                    lastSignalText = "SELL PRO @ ${String.format("%.2f", currentBid)}",
                    onToggleAutoTrading = { viewModel.toggleAutoTrading() },
                    onOpenSignals = { currentScreen = 2 },
                    onOpenDetails = { currentScreen = 1 },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 12.dp)
                )
            }

            // Order Execution Ticket Dialog (Ticket #282101886 | sell 0.01 BTCUSDm Accepted)
            ticketPopup?.let { pos ->
                OrderTicketDialog(
                    position = pos,
                    onDismiss = { viewModel.dismissTicketPopup() }
                )
            }
        }
    }
}
