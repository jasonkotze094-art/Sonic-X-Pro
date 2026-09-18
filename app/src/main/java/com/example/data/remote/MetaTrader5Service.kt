package com.example.data.remote

import com.example.data.remote.dto.ClosePositionRequest
import com.example.data.remote.dto.ModifyPositionRequest
import com.example.data.remote.dto.Mt5AccountInfoDto
import com.example.data.remote.dto.Mt5CandleDto
import com.example.data.remote.dto.Mt5DepthItemDto
import com.example.data.remote.dto.Mt5OrderResult
import com.example.data.remote.dto.Mt5PositionDto
import com.example.data.remote.dto.Mt5SymbolInfoDto
import com.example.data.remote.dto.Mt5TickDto
import com.example.data.remote.dto.TradeOrderRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit Service definition for the MetaTrader 5 (MT5) REST Gateway / Web API.
 * Handles real-time market data retrieval, trade order execution, and account tracking.
 */
interface MetaTrader5Service {

    // ========================================================================
    // TRADE EXECUTION ENDPOINTS
    // ========================================================================

    /**
     * Executes a single market or pending order on the MT5 terminal.
     * Used by the Sonic EA bot and manual 1-tap trade triggers.
     */
    @POST("api/v1/trade/order")
    suspend fun sendOrder(
        @Body request: TradeOrderRequest
    ): Response<Mt5OrderResult>

    /**
     * Executes a batch of multiple orders in parallel or sequential sequence.
     * (e.g. Sonic X Pro's signature 10x trade burst).
     */
    @POST("api/v1/trade/batch")
    suspend fun sendBatchOrders(
        @Body requests: List<TradeOrderRequest>
    ): Response<List<Mt5OrderResult>>

    /**
     * Closes an individual open position by ticket ID.
     */
    @POST("api/v1/trade/close")
    suspend fun closePosition(
        @Body request: ClosePositionRequest
    ): Response<Mt5OrderResult>

    /**
     * Emergency close for all active open positions or for a specific symbol.
     */
    @POST("api/v1/trade/close-all")
    suspend fun closeAllPositions(
        @Query("symbol") symbol: String? = null
    ): Response<List<Mt5OrderResult>>

    /**
     * Modifies the Stop Loss and Take Profit levels for an existing position.
     */
    @PUT("api/v1/trade/modify")
    suspend fun modifyPosition(
        @Body request: ModifyPositionRequest
    ): Response<Mt5OrderResult>

    // ========================================================================
    // MARKET DATA RETRIEVAL ENDPOINTS
    // ========================================================================

    /**
     * Fetches the latest real-time tick (Bid, Ask, Spread) for a given symbol.
     * Example: "BTCUSDm", "EURUSD", "XAUUSD"
     */
    @GET("api/v1/market/quote/{symbol}")
    suspend fun getSymbolQuote(
        @Path("symbol") symbol: String
    ): Response<Mt5TickDto>

    /**
     * Fetches quotes for multiple symbols simultaneously via comma-separated list.
     * Example: symbols="BTCUSDm,XAUUSD,EURUSD"
     */
    @GET("api/v1/market/quotes")
    suspend fun getMultipleQuotes(
        @Query("symbols") symbols: String
    ): Response<List<Mt5TickDto>>

    /**
     * Retrieves historical OHLCV candlestick bars for technical analysis and charts.
     *
     * @param symbol Market instrument (e.g. "BTCUSDm")
     * @param timeframe Chart timeframe: "M1", "M5", "M15", "H1", "D1"
     * @param count Number of bars to return (default: 100)
     */
    @GET("api/v1/market/candles/{symbol}")
    suspend fun getCandlesticks(
        @Path("symbol") symbol: String,
        @Query("timeframe") timeframe: String = "M1",
        @Query("count") count: Int = 100
    ): Response<List<Mt5CandleDto>>

    /**
     * Retrieves Level 2 Market Depth (Order Book) for high-frequency liquidity analysis.
     */
    @GET("api/v1/market/depth/{symbol}")
    suspend fun getMarketDepth(
        @Path("symbol") symbol: String
    ): Response<List<Mt5DepthItemDto>>

    /**
     * Lists all tradeable symbols with specification parameters (digits, min lot, etc.).
     */
    @GET("api/v1/market/symbols")
    suspend fun getTradeableSymbols(): Response<List<Mt5SymbolInfoDto>>

    // ========================================================================
    // ACCOUNT & POSITIONS MONITORING
    // ========================================================================

    /**
     * Retrieves live account metrics including balance, equity, margin, and free margin.
     */
    @GET("api/v1/account")
    suspend fun getAccountInfo(): Response<Mt5AccountInfoDto>

    /**
     * Fetches all currently open positions, optionally filtered by symbol.
     */
    @GET("api/v1/trade/positions")
    suspend fun getOpenPositions(
        @Query("symbol") symbol: String? = null
    ): Response<List<Mt5PositionDto>>

    /**
     * Fetches historical closed trades within a given timestamp range.
     */
    @GET("api/v1/trade/history")
    suspend fun getTradeHistory(
        @Query("from") fromTimestamp: Long? = null,
        @Query("to") toTimestamp: Long? = null,
        @Query("limit") limit: Int = 100
    ): Response<List<Mt5PositionDto>>
}
