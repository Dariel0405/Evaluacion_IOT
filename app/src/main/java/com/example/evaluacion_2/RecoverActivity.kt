package com.example.evaluacion_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RecoverActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.etEmailRecover)
        val btnRecover = findViewById<Button>(R.id.btnRecover)
        val btnBack = findViewById<ImageButton>(R.id.btnBackLogin)

        // Flecha volver → Login
        btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Enviar correo de recuperación (Firebase Auth)
        btnRecover.setOnClickListener {
            val emailText = email.text.toString().trim()

            if (emailText.isEmpty()) {
                Toast.makeText(this, "Ingresa un correo válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(emailText)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Se envió un enlace a $emailText", Toast.LENGTH_LONG).show()
                    } else {
                        val msg = task.exception?.localizedMessage ?: "Error al enviar el correo"
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
