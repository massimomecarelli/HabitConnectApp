package com.example.habitconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitconnect.db.model.Task
import com.example.habitconnect.viewmodel.FragmentTaskViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar


class FragmentTask() : Fragment() , TaskAdapter.ClickListener{

    private lateinit var viewModel: FragmentTaskViewModel
    private lateinit var adapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // LayoutInflater.inflate ritorna una view
        val v:View = inflater.inflate(R.layout.fragment_task, container, false)

        // quindi posso chiamare elementi del layout del fragment con findViewById
        // in particolare chiamo la recyclerView
        recyclerView = v.findViewById(R.id.recycle_tasks)
        recyclerView.layoutManager = LinearLayoutManager(activity)



        val application = requireActivity().application //prende istanza applicazione (alternativa: getActivity())

        // dichiarazione del ViewModel
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[FragmentTaskViewModel::class.java]

        // Faccio l'observe dei tasks del viewmodel
        // Ogni volta che i tasks nel database cambiano, l'observer sarà notificato e i nuovi task saranno
        // aggiunti all'adapter (si mette ad osservare, setTasks poi invierà una notify)
        viewModel.tasks.observe(this, Observer<List<Task>>() { taskList ->
            adapter = TaskAdapter(taskList, this)
            recyclerView.adapter = adapter
            adapter.setTasks(taskList)
        })


        // fab che apre la dialog per l'aggiunta di un task
        val fab: ExtendedFloatingActionButton = v.findViewById(R.id.floatingActionButton)
        fab.setOnClickListener { NewTaskSheet().show(
            childFragmentManager, "DialogAddTask") }

        // ora ho quindi faccio l'inflate del fragment layout nel layout dell'activity, dentro il FrameLayout
        return v
    }

    // click su un item della rv
    override fun onItemClick(task: Task) {
        // decrementa l'obiettivo del task fino a zero e a quel punto imposta completo=true
       if(task.obiettivo>0) {
            task.obiettivo -= 1
            if(task.obiettivo==0){
                task.completo = true
            }
        }
        viewModel.updateTask(task)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // inserisce una callback sugli item della recycler view
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true // nessuna implementazione
            }
            // implementazine dell'azione di swipe che elimina un task
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.absoluteAdapterPosition
                val task = adapter.task_list[pos]

                viewModel.deleteTask(task)
            // possibilità di UNDO dell'operazione di eliminazione
                Snackbar.make(view, "Task deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO"){
                        viewModel.insertTask(task)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recyclerView)
        }
    }

}