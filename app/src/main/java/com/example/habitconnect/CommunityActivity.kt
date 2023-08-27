package com.example.habitconnect

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitconnect.data.Comment
import com.example.habitconnect.databinding.ActivityCommunityBinding
import com.example.habitconnect.db.model.Reminder
import com.example.habitconnect.util.PrefUtil
import com.example.habitconnect.viewmodel.ActivityCommunityViewModel
import com.example.habitconnect.viewmodel.FragmentRemindersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CommunityActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCommunityBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var adapter: CommentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel:ActivityCommunityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // ritorno all'activity parent (main)
        firebaseAuth = FirebaseAuth.getInstance()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ActivityCommunityViewModel::class.java]

        recyclerView = binding.recycleComments
        recyclerView.layoutManager = LinearLayoutManager(this)

        getResponseUsingLiveData() // scarica tutti i commenti e li salva su una mutable list
            // gestito con un observer che quando riceve risposta al completamento del download
            // setta l'adapter, lo assegna alla recyclerview e notifica il cambiamento


        // apre la bottom sheet dialog per inserire un nuovo commento
        binding.floatingActionButton.setOnClickListener {
            NewCommentSheet().show(
                supportFragmentManager, "DialogAddReminder"
            )
        }
    }

    /*
    * Richiede tutti i commenti gestendo la lista di risposta come live data, agganciando un observer
    * alla funzione di get del viewmodel, questa ha un listener al completamento del fetch dei dati
    * (ASINCRONO) che ritorna la Response dal server
    * */
    private fun getResponseUsingLiveData() {
        binding.progressbar.visibility = View.VISIBLE
        viewModel.getResponseAllComments().observe(this) {
            adapter = CommentAdapter(viewModel.listaCommenti)
            // assegna il CommentAdapter come adapter della recyclerView per popolarla
            recyclerView.adapter = adapter
            adapter.setComments()
            binding.progressbar.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate del menu; aggiunge items all'action bar se presente.
        menuInflater.inflate(R.menu.menu_community, menu)
        return true
    }


    // selezione dell'item nel menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Al click sull'icona di sign out esegue signout e ritorna all'activity di sign in
        return when (item.itemId) {
            R.id.SignOut -> {
                firebaseAuth.signOut()
                startActivity(Intent(this, SignInActivity::class.java))
                true

            }
            R.id.refresh -> { // ricarica la pagina
                this.recreate()
                true

            }else -> super.onOptionsItemSelected(item)
        }
    }
}