package com.example.evaluacion_2

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.evaluacion_2.news.News
import com.google.firebase.firestore.FirebaseFirestore

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        tvTitle = findViewById(R.id.tvDetailTitle)
        tvContent = findViewById(R.id.tvDetailContent)

        val btnBack = findViewById<ImageButton>(R.id.btnBackToList)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val id = intent.getStringExtra("news_id")
        if (id.isNullOrEmpty()) {
            finish()
            return
        }

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("news").document(id)

        docRef.get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val news = doc.toObject(News::class.java)
                    tvTitle.text = news?.title ?: "Noticia"
                    tvContent.text = news?.content ?: ""
                } else {
                    Toast.makeText(this, "La noticia no existe", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar noticia", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}
