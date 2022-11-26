package com.projek.iwanmotor.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.projek.iwanmotor.data.barang.Barang
import com.projek.iwanmotor.data.barang.BarangDao
import com.projek.iwanmotor.data.transaksi.Transaksi
import com.projek.iwanmotor.data.transaksi.TransaksiDao
import com.projek.iwanmotor.data.user.User
import com.projek.iwanmotor.data.user.UserDao

@Database(entities = [User::class, Barang::class, Transaksi::class], version = 1, exportSchema = false)
abstract class IwanMotorDatabase : RoomDatabase() {
    abstract fun barangDao(): BarangDao
    abstract fun transaksiDao(): TransaksiDao
    abstract fun userDao() : UserDao

    companion object {
        @Volatile
        private var INSTANCE: IwanMotorDatabase? = null

        fun getDatabase(context: Context): IwanMotorDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IwanMotorDatabase::class.java,
                    "item_database"
                )
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