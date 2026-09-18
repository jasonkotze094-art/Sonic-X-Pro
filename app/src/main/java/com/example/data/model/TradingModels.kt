package com.example.data.model

data class TradePosition(
    val ticketId: Long,
    val symbol: String,
    val type: String, // "SELL" or "BUY"
    val volume: Double,
    val openPrice: Double,
    var currentPrice: Double,
    val stopLoss: Double,
    val takeProfit: Double,
    var profit: Double,
    val swap: Double = 0.0,
    val openTime: String,
    val comment: String = "SONIC X PRO-iTradeBot",
    val status: String = "OPEN" // "OPEN", "CLOSED"
)

data class TradeSignal(
    val id: String,
    val symbol: String,
    val action: String, // "SELL" or "BUY"
    val entryPrice: Double,
    val tp1: Double,
    val tp2: Double,
    val stopLoss: Double,
    val confidence: Int,
    val tradesCount: Int = 10,
    val timeframe: String = "M1",
    val strategy: String = "Sonic X Pro Scalper",
    val timestamp: Long = System.currentTimeMillis()
)

data class TradingAccount(
    val broker: String = "Exness Technologies Ltd",
    val accountNumber: String = "435738583",
    val name: String = "SONIC X CODERS",
    val server: String = "Exness-MT5",
    val accountType: String = "Hedge",
    val balance: Double = 733.90,
    val equity: Double = 741.00,
    val margin: Double = 50.84,
    val freeMargin: Double = 690.16,
    val marginLevel: Double = 1467.51,
    val currency: String = "ZAR",
    val isConnected: Boolean = true
)

data class EASettings(
    val eaName: String = "Sonic X Pro Scalper",
    val isAutoTrading: Boolean = true,
    val lotSize: Double = 0.01,
    val tradesPerSignal: Int = 10,
    val takeProfitPips: Int = 45,
    val stopLossPips: Int = 30,
    val trailingStop: Boolean = true,
    val riskPercent: Double = 2.0
)

data class ExecutionLog(
    val id: Long = System.currentTimeMillis(),
    val message: String,
    val type: String, // "EXECUTION", "SUCCESS", "SIGNAL", "SYSTEM"
    val timestamp: String
)

data class CandleStick(
    val time: Long,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float
)
