package com.projek.iwanmotor.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.projek.iwanmotor.data.user.User
import com.projek.iwanmotor.databinding.ActivityLoginBinding
import com.projek.iwanmotor.ui.daftar.DaftarActivity
import com.projek.iwanmotor.ui.dashboard.DashboardActivity

class LoginActivity : AppCompatActivity() {
    var isExist = false

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userDetailsRepository = ViewModelProvider(this).get(LoginViewModel::class.java)


        binding.tvClick.setOnClickListener{
            startActivity(Intent(this, DaftarActivity::class.java))
        }

        binding.btnSignin.setOnClickListener{
            if (validation()) {
                userDetailsRepository.getGetAllData().observe(this, object : Observer<List<User>> {
                    override fun onChanged(t: List<User>) {
                        var userObject = t
                        for (i in userObject.indices) {
                            if (userObject[i].email?.equals(binding.etEmail.text.toString())!!) {
                                if (userObject[i].password?.equals(binding.etPassword.text.toString())!!) {
                                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                        .putExtra("UserDetials", userObject[i])
                                    // start your next activity
                                    startActivity(intent)

                                } else {
                                    Toast.makeText(this@LoginActivity, " Password is Incorrect ", Toast.LENGTH_LONG)
                                        .show()
                                }
                                isExist = true
                                break

                            } else {
                                isExist = false
                            }
                        }
                        if (isExist) {
                        } else {
                            Toast.makeText(this@LoginActivity, " User Not Registered ", Toast.LENGTH_LONG).show()
                        }

                    }

                })
            }
        }

    }
    private fun validation(): Boolean {

        if (binding.etEmail.text.isNullOrEmpty()) {
            Toast.makeText(this@LoginActivity, " Masukkan Email ", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.etPassword.text.isNullOrEmpty()) {
            Toast.makeText(this@LoginActivity, " Masukkan Password ", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.etPassword.text.toString().length < 6) {
            Toast.makeText(this@LoginActivity, " Masukkan Password lebih dari 6 karakter ", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}