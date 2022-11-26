package com.projek.iwanmotor.data.barang

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

/**
 * Entity data class represents a single row in the database.
 */
@Entity
data class Barang(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "namaProduk")
    val namaProduk: String,
    @ColumnInfo(name = "hargaModal")
    val hargaModal: String,
    @ColumnInfo(name = "hargaJual")
    val hargaJual: String,
    @ColumnInfo(name = "stok")
    val stok: String,
    @ColumnInfo(name = "tglMasuk")
    val tglMasuk: String,
)