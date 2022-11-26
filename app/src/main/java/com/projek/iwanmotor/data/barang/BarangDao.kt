package com.projek.iwanmotor.data.barang

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface BarangDao {

    @Query("SELECT * from barang ORDER BY namaProduk ASC")
    fun getBarangs(): Flow<List<Barang>>

    @Query("SELECT * from Barang WHERE id = :id")
    fun getBarang(id: Int): Flow<Barang>

    // Specify the conflict strategy as IGNORE, when the Barang tries to add an
    // existing Barang into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(Barang: Barang)

    @Update
    suspend fun update(Barang: Barang)

    @Delete
    suspend fun delete(Barang: Barang)
}