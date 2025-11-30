package com.example.evaluacion_2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.evaluacion_2.news.News
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateNewsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_news)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etSubtitle = findViewById<EditText>(R.id.etSubtitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnBack = findViewById<ImageButton>(R.id.btnBackNewsList)

        // Flecha volver
        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val subtitle = etSubtitle.text.toString().trim()
            val content = etContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Título y contenido son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentUser = auth.currentUser
            if (currentUser == null) {
                Toast.makeText(this, "Debes iniciar sesión para crear noticias", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val uid = currentUser.uid

            // 1) Obtener el nombre del usuario desde la colección "users"
            val userDoc = db.collection("users").document(uid)
            userDoc.get()
                .addOnSuccessListener { snapshot ->
                    val authorName = snapshot.getString("name") ?: "Usuario"

                    // 2) Crear documento en "news"
                    val newsCollection = db.collection("news")
                    val newDoc = newsCollection.document() // genera id
                    val id = newDoc.id

                    val news = News(
                        id = id,
                        title = title,
                        subtitle = subtitle,
                        content = content,
                        authorId = uid,
                        authorName = authorName,
                        status = "pending"
                    )

                    newDoc.set(news)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Tu noticia se encuentra en revisión",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Error al guardar la noticia",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "No se pudo obtener el nombre de usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}
