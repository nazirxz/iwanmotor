package com.projek.iwanmotor.data.transaksi

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface TransaksiDao {

    @Query("SELECT * from transaksi ORDER BY namaProduk ASC")
    fun getTransaksis(): Flow<List<Transaksi>>

    @Query("SELECT * from transaksi WHERE invoice = :invoice")
    fun getTransaksi(invoice: String): Flow<Transaksi>

    // Specify the conflict strategy as IGNORE, when the Barang tries to add an
    // existing Barang into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaksi: Transaksi)

    @Update
    suspend fun update(transaksi: Transaksi)

    @Delete
    suspend fun delete(transaksi: Transaksi)
}
