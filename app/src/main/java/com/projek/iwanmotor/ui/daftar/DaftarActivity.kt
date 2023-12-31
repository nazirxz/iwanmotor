package com.projek.iwanmotor.ui.daftar

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.projek.iwanmotor.data.IwanMotorDatabase
import com.projek.iwanmotor.data.user.User
import com.projek.iwanmotor.databinding.ActivityRegisterBinding
import com.projek.iwanmotor.ui.HomeActivity
import com.projek.iwanmotor.ui.login.LoginActivity
import com.projek.iwanmotor.ui.login.LoginViewModel


class DaftarActivity : AppCompatActivity() {
    var isExist = false
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val userDetailsRepository = ViewModelProvider(this@DaftarActivity)[LoginViewModel::class.java]

        binding.tvClick.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            if (validation()) {
                userDetailsRepository.getGetAllData().observe(this
                ) { t ->
                    var email: String = ""
                    var userObject = t
                    for (i in userObject.indices) {
                        if (userObject[i].email?.equals(binding.etEmail.text.toString())!!) {
                            isExist = true
                            Toast.makeText(this@DaftarActivity, "User sudah ada", Toast.LENGTH_LONG)
                                .show()
                            break

                        } else {
                            isExist = false
                            continue

                        }
                    }
                    if (isExist) {
                        Toast.makeText(this@DaftarActivity, "Daftar berhasil", Toast.LENGTH_LONG)
                            .show()


                    } else {
                        val user = User()
                        user.namaLengkap = binding.etNama.text.toString()
                        user.email = binding.etEmail.text.toString()
                        user.password = binding.etPassword.text.toString()
                        val userDatabase = IwanMotorDatabase
                        userDatabase.getDatabase(this@DaftarActivity)?.userDao()
                            ?.insertUserData(user)
                        startActivity(Intent(this@DaftarActivity, HomeActivity::class.java))
                    }
                }
            }
        }
    }
    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validation(): Boolean {
        if (binding.etNama.text.isNullOrEmpty()) {
            Toast.makeText(this@DaftarActivity, " Masukkan Nama Lengkap ", Toast.LENGTH_LONG).show()
            return false
        }

        if (binding.etEmail.text.isNullOrEmpty()) {
            Toast.makeText(this@DaftarActivity, " Masukkan Alamat Email ", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.etPassword.text.isNullOrEmpty()) {
            Toast.makeText(this@DaftarActivity, " Masukkan Password ", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.etPassword.text.toString().length < 6) {
            Toast.makeText(this@DaftarActivity, " Masukkan Password lebih dari 6 karakter ", Toast.LENGTH_LONG).show()
            return false
        }
        if (!isValidEmail(binding.etEmail.text.toString())) {
            Toast.makeText(this@DaftarActivity, " Format alamat email salah ", Toast.LENGTH_LONG).show()
            return false
        }
      return true
    }

}