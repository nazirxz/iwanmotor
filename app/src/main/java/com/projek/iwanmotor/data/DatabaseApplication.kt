package com.projek.iwanmotor.data

import android.app.Application

class DatabaseApplication : Application() {
    // Using by lazy so the database is only created when needed
    // rather than when the application starts
    val database: IwanMotorDatabase by lazy { IwanMotorDatabase.getDatabase(this) }
}
