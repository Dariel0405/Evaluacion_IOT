package com.example.evaluacion_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnMainLogin = findViewById<Button>(R.id.btnMainLogin)
        val btnMainRegister = findViewById<Button>(R.id.btnMainRegister)
        val btnMainRecover = findViewById<Button>(R.id.btnMainRecover)

        btnMainLogin.setBackgroundResource(R.drawable.botonneon)
        btnMainRegister.setBackgroundResource(R.drawable.botonneon)
        btnMainRecover.setBackgroundResource(R.drawable.botonneon)

        btnMainLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnMainRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnMainRecover.setOnClickListener {
            startActivity(Intent(this, RecoverActivity::class.java))
        }
    }
}
