package com.projek.iwanmotor.data.transaksi

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

/**
 * Entity data class represents a single row in the database.
 */
@Entity
data class Transaksi(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "invoice")
    val invoice: String,
    @ColumnInfo(name = "namaProduk")
    val namaProduk: String,
    @ColumnInfo(name = "harga")
    val harga: Double,
    @ColumnInfo(name = "jumlah")
    val jumlah: Int,
    @ColumnInfo(name = "subtotal")
    val subtotal: Double,
    @ColumnInfo(name = "tglPembelian")
    val tglPembelian: String,
)