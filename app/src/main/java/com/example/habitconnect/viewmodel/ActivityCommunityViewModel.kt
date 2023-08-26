package com.example.habitconnect.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.habitconnect.data.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class ActivityCommunityViewModel (application: Application) : AndroidViewModel(application) {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var _commento: Comment
    var listaCommenti:MutableList<Comment> = mutableListOf() // istanzio inizialmente una lista vuota

    fun getAllComments() {
        // prende tutta la collection commenti
        FirebaseFirestore.getInstance().collection("commenti")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) { // result è una lista e document è il singolo item
                    // faccio il mapping key value del documento su una map
                    var mapping = mapOf("user" to document.data["user"].toString(), "timestamp" to document.data["timestamp"].toString(), "testo" to document.data["testo"].toString())
                    // assegno i dati al costruttore della data class Comment e aggiungo l'istanza alla lista
                    listaCommenti.add(Comment(mapping.get("user"), mapping.get("timestamp"), mapping.get("testo")))

                    Log.d(TAG, "${document.id} => ${document.data}") // id e data del documento
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }


    fun insertComment(testo:String){
        firebaseAuth = FirebaseAuth.getInstance()
        val comment = hashMapOf( // il nome utente è lo username usato per accedere
            "user" to firebaseAuth.currentUser?.email,
            "testo" to testo,
            "timestamp" to FieldValue.serverTimestamp()
            // inserisce il timestamp prendendolo dal server (ora locale del serer)
        )
        FirebaseFirestore.getInstance().collection("comments").add(comment)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(getApplication(), "Comment uploaded!", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}