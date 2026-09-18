package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TradingDao {
    @Query("SELECT * FROM positions WHERE status = 'OPEN' ORDER BY ticketId DESC")
    fun getOpenPositions(): Flow<List<PositionEntity>>

    @Query("SELECT * FROM positions WHERE status = 'CLOSED' ORDER BY ticketId DESC")
    fun getClosedPositions(): Flow<List<PositionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosition(position: PositionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPositions(positions: List<PositionEntity>)

    @Update
    suspend fun updatePosition(position: PositionEntity)

    @Query("UPDATE positions SET status = 'CLOSED' WHERE ticketId = :ticketId")
    suspend fun closePosition(ticketId: Long)

    @Query("UPDATE positions SET status = 'CLOSED' WHERE status = 'OPEN'")
    suspend fun closeAllPositions()

    @Query("SELECT * FROM signals ORDER BY timestamp DESC")
    fun getAllSignals(): Flow<List<SignalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignal(signal: SignalEntity)

    @Query("DELETE FROM positions")
    suspend fun clearPositions()
}
