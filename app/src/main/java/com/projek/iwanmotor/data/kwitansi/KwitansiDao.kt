package com.projek.iwanmotor.data.kwitansi

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface KwitansiDao {

    @Query("SELECT * from kwitansi ORDER BY id ASC")
    fun getKwitansis(): Flow<List<Kwitansi>>

    @Query("SELECT * from kwitansi  WHERE id = :id")
    fun getKwitansi(id: Int): Flow<Kwitansi>


    // Specify the conflict strategy as IGNORE, when the Barang tries to add an
    // existing Barang into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(kwitansi : Kwitansi)

    @Update
    suspend fun update(kwitansi : Kwitansi)

    @Delete
    suspend fun delete(kwitansi : Kwitansi)

}
