package com.example.habitconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitconnect.db.model.Task
import com.example.habitconnect.viewmodel.FragmentTaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FragmentTask() : Fragment() {

    private lateinit var viewModel: FragmentTaskViewModel
    private lateinit var adapter: TaskAdapter

    private lateinit var recyclerView: RecyclerView

    val tt = Task(0,"prova1",100,false)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // LayoutInflater.inflate ritorna una view (che posso usare dove voglio)
        val v:View = inflater.inflate(R.layout.fragment_task, container, false)

        // quindi posso chiamare elementi del layout del fragment con findViewById
        // in particolare chiamo la recyclerView
        recyclerView = v.findViewById(R.id.recycle_tasks)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        //  recyclerView.adapter = adapter

        val application = requireActivity().application //prende istanza applicazione (alternativa: getActivity())

        // dichiarazione del ViewModel
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[FragmentTaskViewModel::class.java]
        val t = Task(0,"prova",10,false)
        viewModel.insertTask(t)
        // Faccio l'observe dei tasks del viewmodel
        // Ogni volta che i tasks nel database cambiano, l'observer sarà notificato e i nuovi task saranno
        // aggiunti all'adapter
        viewModel.tasks.observe(this, Observer<List<Task>>() { taskList ->
            adapter = TaskAdapter(taskList)
            recyclerView.adapter = adapter
            adapter.setTasks(taskList)
        })
        val fab: FloatingActionButton = v.findViewById(R.id.floatingActionButton)
        fab.setOnClickListener { viewModel.insertTask(tt) }

        // ora ho quindi faccio l'inflate del fragment layout nel layout dell'activity, dentro il FrameLayout
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //inizialmente inizializzo l'adapter con una lista vuota e lo assegno alla recyclerView


    }

}