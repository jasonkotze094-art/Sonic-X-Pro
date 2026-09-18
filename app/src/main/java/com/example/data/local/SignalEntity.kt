package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.TradeSignal

@Entity(tableName = "signals")
data class SignalEntity(
    @PrimaryKey val id: String,
    val symbol: String,
    val action: String,
    val entryPrice: Double,
    val tp1: Double,
    val tp2: Double,
    val stopLoss: Double,
    val confidence: Int,
    val tradesCount: Int = 10,
    val timeframe: String = "M1",
    val strategy: String = "Sonic X Pro Scalper",
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toTradeSignal() = TradeSignal(
        id = id,
        symbol = symbol,
        action = action,
        entryPrice = entryPrice,
        tp1 = tp1,
        tp2 = tp2,
        stopLoss = stopLoss,
        confidence = confidence,
        tradesCount = tradesCount,
        timeframe = timeframe,
        strategy = strategy,
        timestamp = timestamp
    )

    companion object {
        fun fromTradeSignal(sig: TradeSignal) = SignalEntity(
            id = sig.id,
            symbol = sig.symbol,
            action = sig.action,
            entryPrice = sig.entryPrice,
            tp1 = sig.tp1,
            tp2 = sig.tp2,
            stopLoss = sig.stopLoss,
            confidence = sig.confidence,
            tradesCount = sig.tradesCount,
            timeframe = sig.timeframe,
            strategy = sig.strategy,
            timestamp = sig.timestamp
        )
    }
}
