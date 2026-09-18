package com.example.data.remote.dto

import com.example.data.model.CandleStick
import com.example.data.model.TradePosition
import com.example.data.model.TradingAccount
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Request payload to place a new trade order on MetaTrader 5.
 */
@JsonClass(generateAdapter = true)
data class TradeOrderRequest(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "action") val action: String, // "BUY" or "SELL"
    @Json(name = "volume") val volume: Double, // e.g. 0.01
    @Json(name = "order_type") val orderType: String = "ORDER_TYPE_MARKET",
    @Json(name = "price") val price: Double? = null,
    @Json(name = "stop_loss") val stopLoss: Double? = null,
    @Json(name = "take_profit") val takeProfit: Double? = null,
    @Json(name = "deviation") val deviation: Int = 10,
    @Json(name = "comment") val comment: String = "SONIC X PRO-iTradeBot",
    @Json(name = "magic") val magicNumber: Long = 982143L
)

/**
 * Request payload to close an open position by ticket ID.
 */
@JsonClass(generateAdapter = true)
data class ClosePositionRequest(
    @Json(name = "ticket") val ticket: Long,
    @Json(name = "volume") val volume: Double? = null, // null for full close
    @Json(name = "comment") val comment: String? = "Close by SONIC X PRO"
)

/**
 * Request payload to modify Stop Loss and Take Profit of an active position.
 */
@JsonClass(generateAdapter = true)
data class ModifyPositionRequest(
    @Json(name = "ticket") val ticket: Long,
    @Json(name = "stop_loss") val stopLoss: Double,
    @Json(name = "take_profit") val takeProfit: Double
)

/**
 * Response returned from MT5 order execution.
 */
@JsonClass(generateAdapter = true)
data class Mt5OrderResult(
    @Json(name = "retcode") val retcode: Int, // 10009 = TRADE_RETCODE_DONE
    @Json(name = "retcode_description") val retcodeDescription: String,
    @Json(name = "ticket") val ticket: Long? = null,
    @Json(name = "deal") val deal: Long? = null,
    @Json(name = "order") val order: Long? = null,
    @Json(name = "volume") val volume: Double? = null,
    @Json(name = "price") val price: Double? = null,
    @Json(name = "bid") val bid: Double? = null,
    @Json(name = "ask") val ask: Double? = null,
    @Json(name = "comment") val comment: String? = null,
    @Json(name = "request_id") val requestId: Long? = null
) {
    val isSuccess: Boolean
        get() = retcode == 10009 || retcode == 10008 || retcode == 0
}

/**
 * Represents an active or closed position from MT5 server.
 */
@JsonClass(generateAdapter = true)
data class Mt5PositionDto(
    @Json(name = "ticket") val ticket: Long,
    @Json(name = "symbol") val symbol: String,
    @Json(name = "type") val type: String, // "BUY" or "SELL"
    @Json(name = "volume") val volume: Double,
    @Json(name = "open_price") val openPrice: Double,
    @Json(name = "current_price") val currentPrice: Double,
    @Json(name = "stop_loss") val stopLoss: Double = 0.0,
    @Json(name = "take_profit") val takeProfit: Double = 0.0,
    @Json(name = "profit") val profit: Double,
    @Json(name = "swap") val swap: Double = 0.0,
    @Json(name = "commission") val commission: Double = 0.0,
    @Json(name = "open_time") val openTime: String,
    @Json(name = "comment") val comment: String? = null,
    @Json(name = "magic") val magic: Long? = null,
    @Json(name = "status") val status: String = "OPEN"
) {
    fun toTradePosition(): TradePosition {
        return TradePosition(
            ticketId = ticket,
            symbol = symbol,
            type = type.uppercase(),
            volume = volume,
            openPrice = openPrice,
            currentPrice = currentPrice,
            stopLoss = stopLoss,
            takeProfit = takeProfit,
            profit = profit,
            swap = swap,
            openTime = openTime,
            comment = comment ?: "SONIC X PRO-iTradeBot",
            status = status
        )
    }
}

/**
 * Real-time tick quote for a given market symbol.
 */
@JsonClass(generateAdapter = true)
data class Mt5TickDto(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "bid") val bid: Double,
    @Json(name = "ask") val ask: Double,
    @Json(name = "last") val last: Double? = null,
    @Json(name = "spread") val spread: Double = 0.0,
    @Json(name = "volume") val volume: Double = 0.0,
    @Json(name = "time") val time: Long = System.currentTimeMillis()
)

/**
 * Candlestick (OHLCV) bar data for charting and technical analysis.
 */
@JsonClass(generateAdapter = true)
data class Mt5CandleDto(
    @Json(name = "time") val time: Long,
    @Json(name = "open") val open: Double,
    @Json(name = "high") val high: Double,
    @Json(name = "low") val low: Double,
    @Json(name = "close") val close: Double,
    @Json(name = "tick_volume") val tickVolume: Long = 0L,
    @Json(name = "spread") val spread: Int = 0
) {
    fun toCandleStick(): CandleStick {
        return CandleStick(
            time = time,
            open = open.toFloat(),
            high = high.toFloat(),
            low = low.toFloat(),
            close = close.toFloat()
        )
    }
}

/**
 * Level 2 Market Depth (Order Book) entry.
 */
@JsonClass(generateAdapter = true)
data class Mt5DepthItemDto(
    @Json(name = "type") val type: String, // "BUY" (BID) or "SELL" (ASK)
    @Json(name = "price") val price: Double,
    @Json(name = "volume") val volume: Double
)

/**
 * MetaTrader 5 Trading Account details and margin metrics.
 */
@JsonClass(generateAdapter = true)
data class Mt5AccountInfoDto(
    @Json(name = "login") val login: Long,
    @Json(name = "name") val name: String,
    @Json(name = "broker") val broker: String,
    @Json(name = "server") val server: String,
    @Json(name = "account_type") val accountType: String = "Hedge",
    @Json(name = "currency") val currency: String = "ZAR",
    @Json(name = "balance") val balance: Double,
    @Json(name = "equity") val equity: Double,
    @Json(name = "margin") val margin: Double,
    @Json(name = "free_margin") val freeMargin: Double,
    @Json(name = "margin_level") val marginLevel: Double,
    @Json(name = "leverage") val leverage: Int = 100,
    @Json(name = "trade_allowed") val tradeAllowed: Boolean = true,
    @Json(name = "is_connected") val isConnected: Boolean = true
) {
    fun toTradingAccount(): TradingAccount {
        return TradingAccount(
            broker = broker,
            accountNumber = login.toString(),
            name = name,
            server = server,
            accountType = accountType,
            balance = balance,
            equity = equity,
            margin = margin,
            freeMargin = freeMargin,
            marginLevel = marginLevel,
            currency = currency,
            isConnected = isConnected
        )
    }
}

/**
 * Symbol specification info (contract size, digits, pip size, minimum lot).
 */
@JsonClass(generateAdapter = true)
data class Mt5SymbolInfoDto(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "description") val description: String,
    @Json(name = "digits") val digits: Int,
    @Json(name = "point") val point: Double,
    @Json(name = "min_lot") val minLot: Double = 0.01,
    @Json(name = "max_lot") val maxLot: Double = 100.0,
    @Json(name = "lot_step") val lotStep: Double = 0.01,
    @Json(name = "trade_mode") val tradeMode: String = "FULL"
)
