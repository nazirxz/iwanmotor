package com.projek.iwanmotor.ui.daftar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projek.iwanmotor.databinding.ActivityRegisterBinding
import com.projek.iwanmotor.ui.login.LoginActivity

class DaftarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvClick.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}