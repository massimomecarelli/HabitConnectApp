package com.example.habitconnect

import android.app.AlertDialog
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
import com.example.habitconnect.db.model.Reminder
import com.example.habitconnect.viewmodel.FragmentRemindersViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class FragmentReminders: Fragment(), ReminderAdapter.ClickListener {

    private lateinit var viewModel: FragmentRemindersViewModel
    private lateinit var adapter: ReminderAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // LayoutInflater.inflate ritorna una view
        val v: View = inflater.inflate(R.layout.fragment_reminders, container, false)

        // quindi posso chiamare elementi del layout del fragment con findViewById
        // in particolare chiamo la recyclerView
        recyclerView = v.findViewById(R.id.recycle_reminders)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val application =
            requireActivity().application //prende istanza applicazione (alternativa: getActivity())

        // dichiarazione del ViewModel
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[FragmentRemindersViewModel::class.java]

        // Faccio l'observe dei tasks del viewmodel
        // Ogni volta che i tasks nel database cambiano, l'observer sarà notificato e i nuovi task saranno
        // aggiunti all'adapter (si mette ad osservare, setTasks poi invierà una notify)
        viewModel.reminders.observe(this, Observer<List<Reminder>>() { remindersList ->
            adapter = ReminderAdapter(remindersList, this)
            recyclerView.adapter = adapter
            adapter.setReminders(remindersList)
        })


        val fab: FloatingActionButton = v.findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            NewReminderSheet().show(
                childFragmentManager, "DialogAddReminder"
            )
        }
        // ora ho quindi faccio l'inflate del fragment layout nel layout dell'activity, dentro il FrameLayout
        return v
    }

 // funzione per il click su un item della recyclerview che mostra i dettagli del testo
    override fun onItemClick(reminder: Reminder) {
        AlertDialog.Builder(context)
            .setTitle("INFO")
            .setMessage(reminder.testo) // Specifica un listener che permette di visualizzare il testo di una notifica
            // La dialog viene automaticamente chiusa quando si clicca sul bottone
            .setNeutralButton(
                android.R.string.ok
            ) { _, _ ->

            } //un listener null permette al bottone di chiudere la dialog senza azioni
            .setIcon(android.R.drawable.ic_dialog_info)
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.absoluteAdapterPosition
                val reminder = adapter.reminder_list[pos]

                viewModel.deleteReminder(reminder)

                Snackbar.make(view, "Reminder deleted", Snackbar.LENGTH_LONG).apply {
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recyclerView)
        }
    }

}