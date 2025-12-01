package com.example.evaluacion_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // IDs según tu XML
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPass = findViewById<EditText>(R.id.etPass)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnBackLogin = findViewById<TextView>(R.id.btnBackLogin)

        // Iniciar sesión
        btnLogin.setOnClickListener {
            val correo = etEmail.text.toString().trim()
            val pass = etPass.text.toString().trim()

            if (correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa correo y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(correo, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Usuario autenticado → ya puedes leer noticias en Firestore
                        val intent = Intent(this, NewsListActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Error al iniciar sesión: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // Volver al inicio
        btnBackLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
