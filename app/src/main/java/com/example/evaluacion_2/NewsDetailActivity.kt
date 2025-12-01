package com.example.evaluacion_2

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        db = FirebaseFirestore.getInstance()

        val ivDetailImage = findViewById<ImageView>(R.id.ivDetailImage)
        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvContent = findViewById<TextView>(R.id.tvDetailContent)
        val btnBackDetail = findViewById<Button>(R.id.btnBackDetail)

        btnBackDetail.setOnClickListener {
            finish()
        }

        val newsId = intent.getStringExtra("news_id")
        if (newsId.isNullOrEmpty()) {
            Toast.makeText(this, "No se encontró la noticia", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        db.collection("news")
            .document(newsId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    val title = doc.getString("title") ?: ""
                    val content = doc.getString("content") ?: ""
                    val imageUrl = doc.getString("imageUrl") // 👉 leemos la URL

                    tvTitle.text = title
                    tvContent.text = content

                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(imageUrl)
                            .placeholder(R.drawable.logo_nuevo)
                            .error(R.drawable.logo_nuevo)
                            .into(ivDetailImage)
                    } else {
                        ivDetailImage.setImageResource(R.drawable.logo_nuevo)
                    }

                } else {
                    Toast.makeText(this, "La noticia ya no existe", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar la noticia", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}
