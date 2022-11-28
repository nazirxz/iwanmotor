package com.projek.iwanmotor.data.barang

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface BarangDao {

    @Query("SELECT * from Barang ORDER BY namaProduk ASC")
    fun getItems(): Flow<List<Barang>>

    @Query("SELECT * from Barang WHERE id = :id")
    fun getItem(id: Int): Flow<Barang>


    @Query("SELECT SUM(hargaModal) FROM barang")
    fun getTotalKasKeluar():Flow<Int>

    @Query("SELECT SUM(stok) From barang")
    fun getTotalStok(): Flow<Int>

//    @Query("SELECT COUNT(id) FROM barang")
//    fun getTotalStok(): LiveData<ArrayList<Barang>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(Barang: Barang)

    @Update
    suspend fun update(Barang: Barang)

    @Delete
    suspend fun delete(Barang: Barang)

    @Query("UPDATE barang SET stok=-1 WHERE namaProduk=:namaproduk")
    fun updateStok(namaproduk:String)

}

