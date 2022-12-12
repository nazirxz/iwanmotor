/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.projek.iwanmotor.data.barang

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.projek.iwanmotor.data.transaksi.Transaksi
import com.projek.iwanmotor.data.transaksi.TransaksiDao

/**
 * Database class with a singleton INSTANCE object.
 */
@Database(entities = [Barang::class,Transaksi::class], version = 2, exportSchema = false)
abstract class BarangRoomDatabase : RoomDatabase() {

    abstract fun barangDao(): BarangDao
    abstract fun transaksiDao(): TransaksiDao

    companion object {
        @Volatile
        private var INSTANCE: BarangRoomDatabase? = null

        fun getDatabase(context: Context): BarangRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BarangRoomDatabase::class.java,
                    "barangtrak_database"
                ).allowMainThreadQueries()
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}