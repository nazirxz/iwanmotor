package com.projek.iwanmotor.data.kwitansi

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigInteger

@Entity
data class Kwitansi(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "diterima")
    val diterima: String,
    @ColumnInfo(name = "alamat")
    val alamat: String,
    @ColumnInfo(name = "nohp")
    val nohp: String,
    @ColumnInfo(name = "uangSejumlah")
    val uangSejumlah: Double,
    @ColumnInfo(name = "untukPembayaran")
    val untukPembayaran: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "noRangka")
    val noRangka: String,
    @ColumnInfo(name = "noMesin")
    val noMesin: String,
    @ColumnInfo(name = "noPol")
    val noPol: String,
    @ColumnInfo(name = "tahun")
    val tahun: Int,
    @ColumnInfo(name = "warna")
    val warna: String,
    @ColumnInfo(name = "tanggal")
    val tanggal : String,
)