package com.example.evaluacion_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RecoverActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover)

        auth = FirebaseAuth.getInstance()

        val btnBackRecover = findViewById<TextView>(R.id.btnBackRecover)
        val etEmail = findViewById<EditText>(R.id.etRecoverEmail)
        val btnRecover = findViewById<Button>(R.id.btnRecover)

        // 🔙 Volver al inicio (MainActivity)
        btnBackRecover.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            finish()
        }

        // Enviar correo de recuperación
        btnRecover.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresa tu correo electrónico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Se ha enviado un correo para restablecer tu contraseña",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Error al enviar el correo: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}
