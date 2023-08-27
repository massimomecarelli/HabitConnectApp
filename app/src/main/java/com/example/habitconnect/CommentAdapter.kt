package com.example.habitconnect

import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habitconnect.data.Comment
import com.example.habitconnect.db.model.Reminder
import com.google.firebase.Timestamp
import java.text.DateFormat
import java.util.*


// adapter per i singoli commenti nella recyclerview della community
class CommentAdapter(private val comments: MutableList<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    // assegnamento dei valori del singolo item al layout di row di cui ho fatto l'inflate
    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val user: TextView = itemView.findViewById(R.id.user)
        val comment: TextView = itemView.findViewById(R.id.testo_commento)
        val timestamp: TextView = itemView.findViewById(R.id.timestamp)
    }

    // inflate del layout di row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.community_row, parent, false)
        // rende il testo del commento scrollabile (vedi anche proprietà di scroll nell'xml)
        view.findViewById<TextView>(R.id.testo_commento).movementMethod = ScrollingMovementMethod()
        return CommentViewHolder(view)
    }

    // bind degli item della recyclerview (con le loro posizioni in lista) con un holder dell'elemento
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.user.text = comment.user
        holder.comment.text = comment.testo
        val dateFormat: Date? = comment.timestamp?.toDate() // converte timestamp in Date
        holder.timestamp.text = dateFormat.toString() // converte in stringa per inserirla nella TextView
    }

    override fun getItemCount() = comments.size

    // notifica il cambiamento LiveData, attivato con observe nella CommunityActivity
    fun setComments() {
        notifyDataSetChanged()
    }

}