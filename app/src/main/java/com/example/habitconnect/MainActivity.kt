package com.example.habitconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitconnect.viewmodel.FragmentTaskViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewModel: FragmentTaskViewModel
    private lateinit var adapter: TaskAdapter

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
                    // faccio la transaction per sostituire il fragment nel FrameLayout
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentTask())
                        .commit()



                    // ora ho quindi fatto l'inflate del fragment layout nel layout dell'activity, dentro il FrameLayout
                    // quindi posso chiamare elementi del layout del fragment con findViewById
                    // in particolare chiamo la recyclerView

                     //inizialmente inizializzo l'adapter con una lista vuota e lo assegno alla recyclerView
           /*         adapter = TaskAdapter()
                    val recyclerView: RecyclerView = findViewById(R.id.recycle_tasks)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = adapter

                    // dichiarazione del ViewModel
                    viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[FragmentTaskViewModel::class.java]
                    // Faccio l'observe dei tasks del viewmodel
                    // Ogni volta che i tasks nel database cambiano, l'observer sarà notificato e i nuovi task saranno
                    // aggiunti all'adapter
                    viewModel.tasks.observe(this) { tasks ->
                        adapter.setTasks(tasks)
                   }*/


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