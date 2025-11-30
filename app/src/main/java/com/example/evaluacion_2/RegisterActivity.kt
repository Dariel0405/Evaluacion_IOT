package com.example.evaluacion_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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

        val user = findViewById<EditText>(R.id.etUser)
        val email = findViewById<EditText>(R.id.etEmail)
        val pass = findViewById<EditText>(R.id.etPass)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnBack = findViewById<ImageButton>(R.id.btnBackLogin)

        // Volver al login
        btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnRegister.setOnClickListener {
            val userText = user.text.toString().trim()
            val emailText = email.text.toString().trim()
            val passText = pass.text.toString().trim()

            if (userText.isEmpty() || emailText.isEmpty() || passText.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passText.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(emailText, passText)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            val userData = hashMapOf(
                                "uid" to uid,
                                "name" to userText,
                                "email" to emailText,
                                "createdAt" to FieldValue.serverTimestamp()
                            )

                            db.collection("users").document(uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT)
                                        .show()
                                    val intent = Intent(this, NewsListActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this,
                                        "Error al guardar datos de usuario",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "No se pudo obtener el usuario",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        val msg =
                            task.exception?.localizedMessage ?: "Error al registrar usuario"
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
