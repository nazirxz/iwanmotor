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
    @PrimaryKey
    @ColumnInfo(name = "invoice")
    val invoice: String= "",
    @ColumnInfo(name = "namaProduk")
    val namaProduk: String,
    @ColumnInfo(name = "harga")
    val harga: String,
    @ColumnInfo(name = "jumlah")
    val hjumlah: String,
    @ColumnInfo(name = "subtotal")
    val subtotal: String,
    @ColumnInfo(name = "tglPembelian")
    val tglPembelian: String,
)