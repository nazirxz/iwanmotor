package com.projek.iwanmotor.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

/**
 * Entity data class represents a single row in the database.
 */
@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "namaLengkap")
    val namaLengkap: String,
    @ColumnInfo(name = "alamatEmail")
    val alamatEmail: String,
    @ColumnInfo(name = "password")
    val password: String,
)