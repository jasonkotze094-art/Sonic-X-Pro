package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CandleStick
import com.example.data.model.EASettings
import com.example.data.model.ExecutionLog
import com.example.data.model.TradePosition
import com.example.data.model.TradeSignal
import com.example.data.model.TradingAccount
import com.example.data.repository.TradingRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicLong
import kotlin.random.Random

class SonicTradingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TradingRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = TradingRepository(db.tradingDao())
    }

    val openPositions: StateFlow<List<TradePosition>> = repository.openPositions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val closedPositions: StateFlow<List<TradePosition>> = repository.closedPositions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val signals: StateFlow<List<TradeSignal>> = repository.allSignals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _account = MutableStateFlow(TradingAccount())
    val account: StateFlow<TradingAccount> = _account.asStateFlow()

    private val _eaSettings = MutableStateFlow(EASettings())
    val eaSettings: StateFlow<EASettings> = _eaSettings.asStateFlow()

    private val _activeSymbol = MutableStateFlow("BTCUSDm")
    val activeSymbol: StateFlow<String> = _activeSymbol.asStateFlow()

    private val _currentBid = MutableStateFlow(61749.15)
    val currentBid: StateFlow<Double> = _currentBid.asStateFlow()

    private val _currentAsk = MutableStateFlow(61753.18)
    val currentAsk: StateFlow<Double> = _currentAsk.asStateFlow()

    private val _isExecuting = MutableStateFlow(false)
    val isExecuting: StateFlow<Boolean> = _isExecuting.asStateFlow()

    private val _executionBannerText = MutableStateFlow<String?>("Processing signal: BTCUSDm / SELL (10 trades)")
    val executionBannerText: StateFlow<String?> = _executionBannerText.asStateFlow()

    private val _currentExecutionStep = MutableStateFlow<String?>("Trade 10/10 executed on MT5. Trade execution successful.")
    val currentExecutionStep: StateFlow<String?> = _currentExecutionStep.asStateFlow()

    private val _ticketPopup = MutableStateFlow<TradePosition?>(null)
    val ticketPopup: StateFlow<TradePosition?> = _ticketPopup.asStateFlow()

    private val _isFloatingWidgetVisible = MutableStateFlow(true)
    val isFloatingWidgetVisible: StateFlow<Boolean> = _isFloatingWidgetVisible.asStateFlow()

    private val _isFloatingWidgetExpanded = MutableStateFlow(false)
    val isFloatingWidgetExpanded: StateFlow<Boolean> = _isFloatingWidgetExpanded.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()

    private val _scanResult = MutableStateFlow<TradeSignal?>(null)
    val scanResult: StateFlow<TradeSignal?> = _scanResult.asStateFlow()

    private val _executionLogs = MutableStateFlow<List<ExecutionLog>>(emptyList())
    val executionLogs: StateFlow<List<ExecutionLog>> = _executionLogs.asStateFlow()

    private val _candles = MutableStateFlow<List<CandleStick>>(emptyList())
    val candles: StateFlow<List<CandleStick>> = _candles.asStateFlow()

    private val ticketCounter = AtomicLong(2821019310)
    private var priceTickJob: Job? = null
    private var autoBotJob: Job? = null

    init {
        initializeInitialData()
        startPriceSimulation()
        startAutoBotMonitoring()
    }

    private fun initializeInitialData() {
        viewModelScope.launch {
            // Seed sample initial signals if empty
            val initialSignal = TradeSignal(
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
            )
            repository.insertSignal(initialSignal)

            // Seed sample initial positions matching video if empty
            val baseTime = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(Date())
            val seedPositions = listOf(
                TradePosition(
                    ticketId = 2821019314,
                    symbol = "BTCUSDm",
                    type = "SELL",
                    volume = 0.01,
                    openPrice = 61814.02,
                    currentPrice = 61749.15,
                    stopLoss = 62040.48,
                    takeProfit = 61453.16,
                    profit = 24.38,
                    swap = 0.0,
                    openTime = "2026.06.07 13:29:13",
                    comment = "SONIC X PRO-iTradeBot"
                ),
                TradePosition(
                    ticketId = 2821019315,
                    symbol = "BTCUSDm",
                    type = "SELL",
                    volume = 0.01,
                    openPrice = 61811.48,
                    currentPrice = 61749.15,
                    stopLoss = 62040.48,
                    takeProfit = 61453.16,
                    profit = 21.47,
                    swap = 0.0,
                    openTime = "2026.06.07 13:29:14",
                    comment = "SONIC X PRO-iTradeBot"
                ),
                TradePosition(
                    ticketId = 2821019316,
                    symbol = "BTCUSDm",
                    type = "SELL",
                    volume = 0.01,
                    openPrice = 61802.30,
                    currentPrice = 61749.15,
                    stopLoss = 62040.48,
                    takeProfit = 61453.16,
                    profit = 18.00,
                    swap = 0.0,
                    openTime = "2026.06.07 13:29:15",
                    comment = "SONIC X PRO-iTradeBot"
                ),
                TradePosition(
                    ticketId = 2821019317,
                    symbol = "BTCUSDm",
                    type = "SELL",
                    volume = 0.01,
                    openPrice = 61791.09,
                    currentPrice = 61749.15,
                    stopLoss = 62040.48,
                    takeProfit = 61453.16,
                    profit = 14.04,
                    swap = 0.0,
                    openTime = "2026.06.07 13:29:16",
                    comment = "SONIC X PRO-iTradeBot"
                ),
                TradePosition(
                    ticketId = 2821019318,
                    symbol = "BTCUSDm",
                    type = "SELL",
                    volume = 0.01,
                    openPrice = 61789.03,
                    currentPrice = 61749.15,
                    stopLoss = 62040.48,
                    takeProfit = 61453.16,
                    profit = 11.07,
                    swap = 0.0,
                    openTime = "2026.06.07 13:29:17",
                    comment = "SONIC X PRO-iTradeBot"
                )
            )
            repository.insertPositions(seedPositions)

            // Generate initial candlestick dataset
            generateInitialCandles()
        }
    }

    private fun generateInitialCandles() {
        val list = mutableListOf<CandleStick>()
        var lastPrice = 61820f
        val now = System.currentTimeMillis()
        for (i in 30 downTo 0) {
            val delta = (Random.nextFloat() - 0.52f) * 45f
            val open = lastPrice
            val close = open + delta
            val high = maxOf(open, close) + Random.nextFloat() * 15f
            val low = minOf(open, close) - Random.nextFloat() * 15f
            list.add(CandleStick(time = now - i * 60000L, open = open, high = high, low = low, close = close))
            lastPrice = close
        }
        _candles.value = list
    }

    private fun startPriceSimulation() {
        priceTickJob?.cancel()
        priceTickJob = viewModelScope.launch {
            while (true) {
                delay(1200L)
                val delta = (Random.nextDouble() - 0.54) * 5.5 // slight bearish drift for high profit in short trades
                val newBid = (_currentBid.value + delta).coerceIn(61200.0, 62500.0)
                val newAsk = newBid + 4.03
                _currentBid.value = String.format(Locale.US, "%.2f", newBid).toDouble()
                _currentAsk.value = String.format(Locale.US, "%.2f", newAsk).toDouble()

                // Update open positions profit
                val currentPositions = openPositions.value
                var totalProfit = 0.0
                if (currentPositions.isNotEmpty()) {
                    currentPositions.forEach { pos ->
                        val priceDiff = if (pos.type == "SELL") pos.openPrice - newBid else newBid - pos.openPrice
                        val p = (priceDiff * pos.volume * 8.5) // Scaled profit formula matching MT5 ZAR
                        pos.currentPrice = newBid
                        pos.profit = String.format(Locale.US, "%.2f", p).toDouble()
                        totalProfit += pos.profit
                        repository.updatePosition(pos)
                    }
                }

                // Update account values
                val baseBalance = _account.value.balance
                val newEquity = baseBalance + totalProfit
                val calculatedMargin = currentPositions.size * 10.16
                val calculatedFreeMargin = maxOf(0.0, newEquity - calculatedMargin)
                val marginLevelVal = if (calculatedMargin > 0) (newEquity / calculatedMargin) * 100 else 9999.0

                _account.value = _account.value.copy(
                    equity = String.format(Locale.US, "%.2f", newEquity).toDouble(),
                    margin = String.format(Locale.US, "%.2f", calculatedMargin).toDouble(),
                    freeMargin = String.format(Locale.US, "%.2f", calculatedFreeMargin).toDouble(),
                    marginLevel = String.format(Locale.US, "%.2f", marginLevelVal).toDouble()
                )
            }
        }
    }

    private fun startAutoBotMonitoring() {
        autoBotJob?.cancel()
        autoBotJob = viewModelScope.launch {
            while (true) {
                delay(30000L)
                if (_eaSettings.value.isAutoTrading && !_isExecuting.value) {
                    // Occasionally auto-trigger high-probability signal trade batch
                    triggerSignalExecution(
                        symbol = _activeSymbol.value,
                        action = if (Random.nextBoolean()) "SELL" else "SELL",
                        trades = _eaSettings.value.tradesPerSignal
                    )
                }
            }
        }
    }

    fun toggleAutoTrading() {
        val current = _eaSettings.value.isAutoTrading
        val updated = !current
        _eaSettings.value = _eaSettings.value.copy(isAutoTrading = updated)
        if (updated) {
            _executionBannerText.value = "EA Active: Monitoring ${_activeSymbol.value} for institutional order blocks..."
        } else {
            _executionBannerText.value = "EA Stopped. Manual trading mode active."
        }
    }

    fun triggerSignalExecution(symbol: String = _activeSymbol.value, action: String = "SELL", trades: Int = 10) {
        if (_isExecuting.value) return
        viewModelScope.launch {
            _isExecuting.value = true
            _executionBannerText.value = "Processing signal: $symbol / $action ($trades trades)"

            val currentPrice = _currentBid.value
            val tp1 = if (action == "SELL") currentPrice - 350.0 else currentPrice + 350.0
            val tp2 = if (action == "SELL") currentPrice - 600.0 else currentPrice + 600.0
            val sl = if (action == "SELL") currentPrice + 230.0 else currentPrice - 230.0

            val timeFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())

            for (i in 1..trades) {
                delay(400L)
                val ticket = ticketCounter.incrementAndGet()
                val entryP = currentPrice + (Random.nextDouble() - 0.5) * 4.0
                val formattedEntry = String.format(Locale.US, "%.2f", entryP).toDouble()

                val pos = TradePosition(
                    ticketId = ticket,
                    symbol = symbol,
                    type = action,
                    volume = _eaSettings.value.lotSize,
                    openPrice = formattedEntry,
                    currentPrice = currentPrice,
                    stopLoss = sl,
                    takeProfit = tp1,
                    profit = 0.50,
                    swap = 0.0,
                    openTime = timeFormat.format(Date()),
                    comment = "SONIC X PRO-iTradeBot"
                )
                repository.insertPosition(pos)

                _currentExecutionStep.value = "Trade $i/$trades executed on MT5 (Account: ${_account.value.accountNumber})"
                if (i == 1 || i == trades) {
                    _ticketPopup.value = pos
                }
            }

            delay(1200L)
            _isExecuting.value = false
            _executionBannerText.value = "Active: $trades trades running on MT5 (${_account.value.broker})"
        }
    }

    fun dismissTicketPopup() {
        _ticketPopup.value = null
    }

    fun closePosition(ticketId: Long) {
        viewModelScope.launch {
            repository.closePosition(ticketId)
        }
    }

    fun closeAllPositions() {
        viewModelScope.launch {
            repository.closeAllPositions()
            _executionBannerText.value = "All active positions closed successfully."
        }
    }

    fun setActiveSymbol(symbol: String) {
        _activeSymbol.value = symbol
        val newBase = when (symbol) {
            "BTCUSDm" -> 61749.15
            "EURUSD" -> 1.08420
            "XAUUSD" -> 2341.50
            "GBPUSD" -> 1.27300
            "US30" -> 39120.0
            "NAS100" -> 18450.0
            else -> 61749.15
        }
        _currentBid.value = newBase
        _currentAsk.value = newBase + 4.0
        generateInitialCandles()
    }

    fun setEa(eaName: String) {
        _eaSettings.value = _eaSettings.value.copy(eaName = eaName)
    }

    fun setLotSize(size: Double) {
        _eaSettings.value = _eaSettings.value.copy(lotSize = size)
    }

    fun setTradesPerSignal(count: Int) {
        _eaSettings.value = _eaSettings.value.copy(tradesPerSignal = count)
    }

    fun toggleFloatingWidget() {
        _isFloatingWidgetVisible.value = !_isFloatingWidgetVisible.value
    }

    fun setFloatingWidgetExpanded(expanded: Boolean) {
        _isFloatingWidgetExpanded.value = expanded
    }

    fun scanChart() {
        if (_isScanning.value) return
        viewModelScope.launch {
            _isScanning.value = true
            _scanProgress.value = 0.1f
            _scanResult.value = null

            delay(500L)
            _scanProgress.value = 0.35f
            delay(500L)
            _scanProgress.value = 0.70f
            delay(600L)
            _scanProgress.value = 1.0f

            val generatedSignal = TradeSignal(
                id = "AI-SCAN-${Random.nextInt(1000, 9999)}",
                symbol = _activeSymbol.value,
                action = "SELL",
                entryPrice = _currentBid.value,
                tp1 = String.format(Locale.US, "%.2f", _currentBid.value - 360.0).toDouble(),
                tp2 = String.format(Locale.US, "%.2f", _currentBid.value - 620.0).toDouble(),
                stopLoss = String.format(Locale.US, "%.2f", _currentBid.value + 240.0).toDouble(),
                confidence = 97,
                tradesCount = _eaSettings.value.tradesPerSignal,
                timeframe = "H1",
                strategy = "Institutional Order Flow Breakout"
            )
            _scanResult.value = generatedSignal
            repository.insertSignal(generatedSignal)
            _isScanning.value = false
        }
    }
}
