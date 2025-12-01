package com.example.evaluacion_2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.evaluacion_2.news.News
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NewsListActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: NewsAdapter
    private val newsList = mutableListOf<News>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        // RecyclerView y adaptador
        rv = findViewById(R.id.rvNews)
        rv.layoutManager = LinearLayoutManager(this)

        adapter = NewsAdapter(newsList) { news ->
            val i = Intent(this, NewsDetailActivity::class.java)
            i.putExtra("news_id", news.id)
            startActivity(i)
        }
        rv.adapter = adapter

        // Firebase
        val db = FirebaseFirestore.getInstance()
        val newsCollection = db.collection("news")

        // Solo noticias aprobadas (ojo con el texto "aprovado" en Firestore)
        newsCollection
            .whereEqualTo("status", "aprovado")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    return@addSnapshotListener
                }

                newsList.clear()
                for (doc in snapshot.documents) {
                    val item = doc.toObject(News::class.java)
                    if (item != null) {
                        item.id = doc.id
                        newsList.add(item)
                    }
                }
                adapter.notifyDataSetChanged()
            }

        // Cerrar sesión
        val btnLogout = findViewById<TextView>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("Estás a punto de cerrar sesión. ¿Estás seguro?")
                .setPositiveButton("Sí") { _: DialogInterface, _: Int ->
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Botón para crear noticia (Button con texto "NUEVA NOTICIA")
        val btnAddNews = findViewById<Button>(R.id.btnAddNews)
        btnAddNews.setOnClickListener {
            startActivity(Intent(this, CreateNewsActivity::class.java))
        }
    }
}
