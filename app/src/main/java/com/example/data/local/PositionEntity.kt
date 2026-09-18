package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.TradePosition

@Entity(tableName = "positions")
data class PositionEntity(
    @PrimaryKey val ticketId: Long,
    val symbol: String,
    val type: String,
    val volume: Double,
    val openPrice: Double,
    val currentPrice: Double,
    val stopLoss: Double,
    val takeProfit: Double,
    val profit: Double,
    val swap: Double = 0.0,
    val openTime: String,
    val comment: String = "SONIC X PRO-iTradeBot",
    val status: String = "OPEN"
) {
    fun toTradePosition() = TradePosition(
        ticketId = ticketId,
        symbol = symbol,
        type = type,
        volume = volume,
        openPrice = openPrice,
        currentPrice = currentPrice,
        stopLoss = stopLoss,
        takeProfit = takeProfit,
        profit = profit,
        swap = swap,
        openTime = openTime,
        comment = comment,
        status = status
    )

    companion object {
        fun fromTradePosition(pos: TradePosition) = PositionEntity(
            ticketId = pos.ticketId,
            symbol = pos.symbol,
            type = pos.type,
            volume = pos.volume,
            openPrice = pos.openPrice,
            currentPrice = pos.currentPrice,
            stopLoss = pos.stopLoss,
            takeProfit = pos.takeProfit,
            profit = pos.profit,
            swap = pos.swap,
            openTime = pos.openTime,
            comment = pos.comment,
            status = pos.status
        )
    }
}
