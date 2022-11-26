package com.projek.iwanmotor.ui.login

import android.app.Application
import androidx.lifecycle.*
import com.projek.iwanmotor.data.user.User
import com.projek.iwanmotor.data.user.UserDetailsRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: UserDetailsRepository
    private var getAllDatas: LiveData<List<User>>

    init {
        repository = UserDetailsRepository(application)
        getAllDatas = repository.getAllData()!!
    }

    fun insert(data: User) {
        repository.insertData(data)
    }
    fun getGetAllData(): LiveData<List<User>> {
        return getAllDatas
    }
}
