package com.example.habitconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
        val badge = bottomNavigationView.getOrCreateBadge(R.id.Tasks)
        badge.isVisible = true
        badge.number = 90

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
                    Toast.makeText(this,"tasks", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentTask())
                        .commit()
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