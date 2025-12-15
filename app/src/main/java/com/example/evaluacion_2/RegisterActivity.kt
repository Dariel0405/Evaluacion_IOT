package com.example.evaluacion_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val btnBackRegister = findViewById<TextView>(R.id.btnBackRegister)
        val etUser = findViewById<EditText>(R.id.etRegisterUsername) // ✅ NUEVO
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
            val username = etUser.text.toString().trim() // ✅ NUEVO
            val email = etEmail.text.toString().trim()
            val pass = etPass.text.toString().trim()
            val passConfirm = etPassConfirm.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || passConfirm.isEmpty()) {
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

                        val uid = auth.currentUser?.uid

                        if (uid != null) {
                            // ✅ Crear usuario en colección "users" (separada de "news")
                            val userData = hashMapOf(
                                "name" to username, // ✅ AHORA LO DEFINE EL USUARIO
                                "email" to email,
                                "createdAt" to FieldValue.serverTimestamp()
                            )

                            db.collection("users").document(uid).set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this,
                                        "Cuenta creada, pero no se pudo guardar en users: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                        } else {
                            Toast.makeText(this, "Cuenta creada, pero UID no disponible", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }

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
