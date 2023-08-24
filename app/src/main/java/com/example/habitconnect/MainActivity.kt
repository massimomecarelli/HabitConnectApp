package com.example.habitconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.habitconnect.databinding.ActivityMainBinding
import com.example.habitconnect.viewmodel.FragmentTaskViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView = binding.bottomNavigationView
        //viene controllato se savedInstanceState è null cioè significa che è la prima volta che l'activity viene creata
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, FragmentTask())
                .commit()
        }
        //alla creazione dell'activity aggiorno il bage della sezione task che è la home di avvio
        var viewModelTask = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[FragmentTaskViewModel::class.java]
        val badgeTask = bottomNavigationView.getOrCreateBadge(R.id.Tasks) // qui crea il badge
        badgeTask.isVisible = true
        for (i in viewModelTask.getNonCompleti()){
            badgeTask.number +=1 // il badge indica i task non completati
        }
        bottomNavigationView.setSelectedItemId(R.id.Tasks)

        bottomNavigationView.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.Tutorial ->{
                    true
                }
                R.id.Focus ->{
                    val timerActivityIntent = Intent(this, TimerActivity::class.java)
                    startActivity(timerActivityIntent)
                    true
                }
                R.id.Tasks ->{
                    // faccio la transaction per sostituire il fragment nel FrameLayout
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentTask())
                        .commit()

                    // al click sul nome del menu faccio l'update del badge che indica i task non completati
                    badgeTask.number = 0
                    for (i in viewModelTask.getNonCompleti()){
                        badgeTask.number +=1
                    }
                    true
                }
                R.id.Reminders ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentReminders())
                        .commit()
                    true
                }
                R.id.Community ->{
                    true
                }
                else-> {
                    false
                }
            }
        }
    }

    /* quando l'activity è nella fase di resume (era stato fatto un intent verso un'altra activity e
    si è tornati indietro) ricarica la main activity in modo che ci si trovi nella sezione Tasks
    con l'item Tasks nella bottom nav bar selezionato */
    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "resumed")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FragmentTask())
            .commit()
        bottomNavigationView.setSelectedItemId(R.id.Tasks)
    }

    /* logs per debug */
    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "started")
    }
    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "paused")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("MainActivity", "restarted")
    }
}