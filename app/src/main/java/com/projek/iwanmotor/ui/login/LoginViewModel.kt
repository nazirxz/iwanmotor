package com.projek.iwanmotor.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import android.content.Intent
import android.os.Bundle
import com.projek.iwanmotor.data.user.UserDao

class LoginViewModel(private val userDao: UserDao) : ViewModel() {

}