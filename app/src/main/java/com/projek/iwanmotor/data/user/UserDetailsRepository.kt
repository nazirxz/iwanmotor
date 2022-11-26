package com.projek.iwanmotor.data.user

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.projek.iwanmotor.data.IwanMotorDatabase

class UserDetailsRepository(application: Application) {


    private var daoAccess: UserDao? = null
    private var allData: LiveData<List<User>>? = null

    init {
        //fetching user database
        val db = IwanMotorDatabase.getDatabase(application)
        daoAccess = db?.userDao()
        allData = daoAccess?.getDetails()

    }


    fun getAllData(): LiveData<List<User>>? {
        return allData
    }

    fun insertData(data: User) {
        daoAccess?.let { LoginInsertion(it).execute(data) }
    }

    private class LoginInsertion(private val daoAccess: UserDao) :
        AsyncTask<User, Void, Void>() {

        override fun doInBackground(vararg data: User): Void? {

            daoAccess.insertUserData(data[0])
            return null

        }

    }
}