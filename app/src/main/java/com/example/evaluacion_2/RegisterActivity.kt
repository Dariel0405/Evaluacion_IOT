package com.example.evaluacion_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val btnBackRegister = findViewById<TextView>(R.id.btnBackRegister)
        val etEmail = findViewById<EditText>(R.id.etRegisterEmail)
        val etPass = findViewById<EditText>(R.id.etRegisterPass)
        val etPassConfirm = findViewById<EditText>(R.id.etRegisterPassConfirm)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // 🔙 Volver al inicio (MainActivity)
        btnBackRegister.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            finish()
        }

        // Lógica de registro
        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPass.text.toString().trim()
            val passConfirm = etPassConfirm.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty() || passConfirm.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != passConfirm) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Error al crear la cuenta: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}
