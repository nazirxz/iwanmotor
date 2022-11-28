package com.projek.iwanmotor.data.transaksi

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaksiDao {

    @Query("SELECT * from transaksi ORDER BY namaProduk ASC")
    fun getTransaksis(): Flow<List<Transaksi>>

    @Query("SELECT * from transaksi WHERE id = :id")
    fun getTransaksi(id: Int): Flow<Transaksi>

    @Query("SELECT SUM(subtotal) FROM transaksi")
    fun getTotalKasMasuk():Flow<Int>

    @Query("SELECT COUNT(id) From transaksi")
    fun getTotalPenjualan(): Flow<Int>

    // Specify the conflict strategy as IGNORE, when the Barang tries to add an
    // existing Barang into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaksi: Transaksi)

    @Update
    suspend fun update(transaksi: Transaksi)

    @Delete
    suspend fun delete(transaksi: Transaksi)

}
