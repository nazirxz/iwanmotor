package com.projek.iwanmotor.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.projek.iwanmotor.data.user.User
import com.projek.iwanmotor.data.user.UserDao

/**
 * Database class with a singleton INSTANCE object.
 */
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class RoomDatabase : RoomDatabase() {

    abstract fun itemDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDatabase? = null

        fun getDatabase(context: Context): RoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabase::class.java,
                    "database"
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