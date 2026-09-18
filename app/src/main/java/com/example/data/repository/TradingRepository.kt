package com.example.data.repository

import com.example.data.local.PositionEntity
import com.example.data.local.SignalEntity
import com.example.data.local.TradingDao
import com.example.data.model.TradePosition
import com.example.data.model.TradeSignal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TradingRepository(private val tradingDao: TradingDao) {

    val openPositions: Flow<List<TradePosition>> = tradingDao.getOpenPositions().map { list ->
        list.map { it.toTradePosition() }
    }

    val closedPositions: Flow<List<TradePosition>> = tradingDao.getClosedPositions().map { list ->
        list.map { it.toTradePosition() }
    }

    val allSignals: Flow<List<TradeSignal>> = tradingDao.getAllSignals().map { list ->
        list.map { it.toTradeSignal() }
    }

    suspend fun insertPosition(position: TradePosition) {
        tradingDao.insertPosition(PositionEntity.fromTradePosition(position))
    }

    suspend fun insertPositions(positions: List<TradePosition>) {
        tradingDao.insertPositions(positions.map { PositionEntity.fromTradePosition(it) })
    }

    suspend fun updatePosition(position: TradePosition) {
        tradingDao.updatePosition(PositionEntity.fromTradePosition(position))
    }

    suspend fun closePosition(ticketId: Long) {
        tradingDao.closePosition(ticketId)
    }

    suspend fun closeAllPositions() {
        tradingDao.closeAllPositions()
    }

    suspend fun insertSignal(signal: TradeSignal) {
        tradingDao.insertSignal(SignalEntity.fromTradeSignal(signal))
    }
}
