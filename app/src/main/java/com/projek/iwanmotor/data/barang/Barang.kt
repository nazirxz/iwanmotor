package com.projek.iwanmotor.data.barang

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat
import java.util.*

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "Barang")
data class Barang(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "namaProduk")
    val namaProduk: String,
    @ColumnInfo(name = "hargaModal")
    val hargaModal: Double,
    @ColumnInfo(name = "hargaJual")
    val hargaJual: Double,
    @ColumnInfo(name = "stok")
    val stok: Int,
    @ColumnInfo(name = "tglMasuk")
    val tglMasuk: String,
)

//fun Barang.getFormattedPrice(): String =
//    NumberFormat.getCurrencyInstance(Locale("id","ID")).format(hargaModal)
//fun Barang.getFormattedPrice2(): String =
//    NumberFormat.getCurrencyInstance(Locale("id","ID")).format(hargaJual)
