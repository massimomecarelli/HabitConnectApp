package com.example.habitconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.habitconnect.viewmodel.FragmentTaskViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
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
                    Toast.makeText(this,"tutorial", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.Focus ->{
                    Toast.makeText(this,"focus", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentFocus())
                        .commit()
                    true
                }
                R.id.Tasks ->{
                   // Toast.makeText(this,"tasks", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this,"reminders", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.Community ->{
                    Toast.makeText(this,"community", Toast.LENGTH_SHORT).show()
                    true
                }
                else-> {
                    false
                }
            }
        }
    }
}