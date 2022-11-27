package com.projek.iwanmotor.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.projek.iwanmotor.data.user.User
import com.projek.iwanmotor.data.user.UserDao

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class IwanMotorDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        private var INSTANCE: IwanMotorDatabase? = null
        fun getDatabase(context: Context): IwanMotorDatabase? {
            if (INSTANCE == null) synchronized(IwanMotorDatabase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context, IwanMotorDatabase::class.java, "IwanMotorDatabase"
                    ).allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE

        }
    }
}