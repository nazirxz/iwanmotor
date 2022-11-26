package com.projek.iwanmotor.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.projek.iwanmotor.databinding.ActivityLoginBinding
import com.projek.iwanmotor.ui.daftar.DaftarActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvClick.setOnClickListener{
            startActivity(Intent(this, DaftarActivity::class.java))
        }
    }
}