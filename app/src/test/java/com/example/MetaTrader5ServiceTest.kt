package com.example

import com.example.data.remote.MetaTraderApiClient
import com.example.data.remote.dto.ClosePositionRequest
import com.example.data.remote.dto.ModifyPositionRequest
import com.example.data.remote.dto.Mt5CandleDto
import com.example.data.remote.dto.Mt5OrderResult
import com.example.data.remote.dto.Mt5PositionDto
import com.example.data.remote.dto.Mt5TickDto
import com.example.data.remote.dto.TradeOrderRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MetaTrader5ServiceTest {

    @Test
    fun testServiceCreation() {
        val service = MetaTraderApiClient.createService(
            baseUrl = "https://api.itradebot.org/",
            apiKey = "test-token-12345",
            accountId = "435738583",
            isDebug = false
        )
        assertNotNull(service)
    }

    @Test
    fun testTradeOrderRequestSerialization() {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(TradeOrderRequest::class.java)

        val request = TradeOrderRequest(
            symbol = "BTCUSDm",
            action = "SELL",
            volume = 0.01,
            stopLoss = 62040.48,
            takeProfit = 61453.16,
            comment = "SONIC X PRO-iTradeBot"
        )

        val json = adapter.toJson(request)
        assertTrue(json.contains("\"symbol\":\"BTCUSDm\""))
        assertTrue(json.contains("\"action\":\"SELL\""))
        assertTrue(json.contains("\"volume\":0.01"))
    }

    @Test
    fun testMt5OrderResultParsing() {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(Mt5OrderResult::class.java)

        val rawJson = """
            {
                "retcode": 10009,
                "retcode_description": "Done",
                "ticket": 282101886,
                "deal": 49018241,
                "volume": 0.01,
                "price": 61811.48,
                "comment": "SONIC X PRO"
            }
        """.trimIndent()

        val result = adapter.fromJson(rawJson)
        assertNotNull(result)
        assertEquals(10009, result?.retcode)
        assertEquals("Done", result?.retcodeDescription)
        assertEquals(282101886L, result?.ticket)
        assertTrue(result?.isSuccess == true)
    }

    @Test
    fun testMt5TickDtoParsing() {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(Mt5TickDto::class.java)

        val rawJson = """
            {
                "symbol": "BTCUSDm",
                "bid": 61811.48,
                "ask": 61812.98,
                "spread": 1.5,
                "volume": 4.5
            }
        """.trimIndent()

        val tick = adapter.fromJson(rawJson)
        assertNotNull(tick)
        assertEquals("BTCUSDm", tick?.symbol)
        assertEquals(61811.48, tick?.bid ?: 0.0, 0.001)
        assertEquals(61812.98, tick?.ask ?: 0.0, 0.001)
    }

    @Test
    fun testMt5CandleDtoToDomainModel() {
        val candleDto = Mt5CandleDto(
            time = 1718000000L,
            open = 61800.0,
            high = 61950.0,
            low = 61750.0,
            close = 61910.0
        )
        val candle = candleDto.toCandleStick()
        assertEquals(1718000000L, candle.time)
        assertEquals(61800.0f, candle.open, 0.01f)
        assertEquals(61950.0f, candle.high, 0.01f)
        assertEquals(61750.0f, candle.low, 0.01f)
        assertEquals(61910.0f, candle.close, 0.01f)
    }
}
