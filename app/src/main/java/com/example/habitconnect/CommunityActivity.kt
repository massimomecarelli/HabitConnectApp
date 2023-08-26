package com.example.habitconnect

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.habitconnect.databinding.ActivityCommunityBinding
import com.example.habitconnect.util.PrefUtil
import com.example.habitconnect.viewmodel.ActivityCommunityViewModel
import com.example.habitconnect.viewmodel.FragmentRemindersViewModel
import com.google.firebase.auth.FirebaseAuth

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

        recyclerView = binding.recycleComments

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ActivityCommunityViewModel::class.java]

        viewModel.getAllComments()
        val commentList = viewModel.listaCommenti

        // assegna il CommentAdapter come adapter della recyclerView per popolarla
        adapter = CommentAdapter(commentList)
        recyclerView.adapter = adapter

        // apre la bottom sheet dialog per inserire un nuovo commento
        binding.floatingActionButton.setOnClickListener {
            NewCommentSheet().show(
                supportFragmentManager, "DialogAddReminder"
            )
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