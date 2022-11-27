package com.projek.iwanmotor.ui.login

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.*
import com.projek.iwanmotor.data.user.User
import com.projek.iwanmotor.data.user.UserDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

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
    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _namaLengkap = MutableStateFlow("")

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setNamaLengkap(repeat: String) {
        _namaLengkap.value = repeat
    }
    val isSubmitEnabled: Flow<Boolean> =
        combine( _email, _namaLengkap,_password, ) { email, namaLengkap ,password, ->
            val isEmailCorrect = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            val isNamaLengkapCorrect = namaLengkap.isNotEmpty()
            val isPasswordCorrect = password.length >= 5
            return@combine isEmailCorrect and isNamaLengkapCorrect and isPasswordCorrect
        }



}
