package com.example.habitconnect.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.habitconnect.data.Comment
import com.example.habitconnect.data.Response
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.DateFormat
import java.util.*


class ActivityCommunityViewModel (application: Application) : AndroidViewModel(application) {
    private lateinit var firebaseAuth: FirebaseAuth
    var listaCommenti:MutableList<Comment> = mutableListOf() // istanzio inizialmente una lista vuota

    fun getResponseAllComments(): MutableLiveData<Response> {
        // prende tutta la collection commenti
        listaCommenti = mutableListOf()
        val mutableLiveData = MutableLiveData<Response>()
        FirebaseFirestore.getInstance().collection("commenti").limit(30)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) { // result è una lista e document è il singolo item
                    // faccio il mapping key value del documento su una map
                    var mapping = mapOf("user" to document.data["user"].toString(), "timestamp" to document.data["timestamp"] as Timestamp, "testo" to document.data["testo"].toString())
                    // assegno i dati al costruttore della data class Comment e aggiungo l'istanza alla lista
                    listaCommenti.add(Comment(mapping["user"] as String, mapping["timestamp"] as Timestamp,
                        mapping["testo"] as String
                    ))
                    Log.d(TAG, "${document.id} => ${document.data}") // id e data del documento
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
                // listener che restituisce la risposta al completamento del fetch asincrono
            .addOnCompleteListener{ task ->
                val response = Response() // definita nel package data
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.products = result.documents.mapNotNull { snapShot ->
                            snapShot.toObject(Comment::class.java)
                }
            }
        } else {
            response.exception = task.exception
        }
        mutableLiveData.value = response
    }
    return mutableLiveData
    }


    fun insertComment(testo:String){
        firebaseAuth = FirebaseAuth.getInstance()
        val comment = hashMapOf( // il nome utente è lo username usato per accedere
            "user" to firebaseAuth.currentUser?.email,
            "testo" to testo,
            "timestamp" to FieldValue.serverTimestamp()
            // inserisce il timestamp prendendolo dal server (ora locale del serer)
        )
        FirebaseFirestore.getInstance().collection("commenti").add(comment)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(getApplication(), "Comment uploaded!", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}