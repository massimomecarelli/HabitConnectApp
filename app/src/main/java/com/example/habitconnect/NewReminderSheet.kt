package com.example.habitconnect


import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.habitconnect.db.model.Reminder
import com.example.habitconnect.viewmodel.FragmentRemindersViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.DateFormat


class NewReminderSheet : BottomSheetDialogFragment() {
    private lateinit var viewModel: FragmentRemindersViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonDate: MaterialButton = view.findViewById(R.id.date)
        val buttonTime: MaterialButton = view.findViewById(R.id.time)
        buttonDate.setOnClickListener{
            openDatePicker(view) // Open date picker dialog
        }

        buttonTime.setOnClickListener {
            openTimePicker(view) //Open time picker dialog
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.dialog_add_reminder, container, false)
        val application = requireActivity().application
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[FragmentRemindersViewModel::class.java]
        v.findViewById<MaterialButton>(R.id.saveButton).setOnClickListener {
            saveAction(v)
        }
        return v
    }

    // funzione che apre il date picker e setta il testo nel bottone della data con la data selezionata
    private fun openDatePicker(view: View) {
        val datePickerDialog = DatePickerDialog(
            view.context,
            OnDateSetListener { datePicker, year, month, day -> //Mostra il valore selezionato nel testo del bottone
                view.findViewById<MaterialButton>(R.id.date).text = "$year.$month.$day"
            }, 2023, 8, 20
        )
        datePickerDialog.show()
    }

    // funzione che apre il time picker e setta il testo nel bottone dell'orario con l'orario selezionato
    private fun openTimePicker(view: View) {
        val timePickerDialog = TimePickerDialog(
            view.context,
            OnTimeSetListener { timePicker, hour, minute -> //Showing the picked value in the textView
                view.findViewById<MaterialButton>(R.id.time).text = "$hour:$minute"
            }, 10, 30, true
        )
        timePickerDialog.show()
    }


    // funzione chiamata dal click sul pulsante save per salvare il reminder inserito
    private fun saveAction(view: View) {
        var newReminder = Reminder(
            0,
            "Reminder",
            DateFormat.getDateInstance().toString(),
            DateFormat.getTimeInstance().toString()
        ) // reminder con testo di default

        val testo = view.findViewById<TextInputEditText>(R.id.testo).text.toString()
        if (!testo.isNullOrEmpty()) {
            newReminder.testo = testo
        }

        val data = view.findViewById<MaterialButton>(R.id.date).text.toString()
        val time = view.findViewById<MaterialButton>(R.id.time).text.toString()
        //vengono fatti controlli, se data e ora sono state inserite allora procede con l'inserimento nel db
        if (!data.isNullOrEmpty()) {
            newReminder.data = data
            if (!time.isNullOrEmpty()) {
                newReminder.time = time
                viewModel.insertReminder(newReminder)
                dismiss()
            } else {
                Toast.makeText(context, "Inserisci l'orario", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Inserisci la data", Toast.LENGTH_SHORT).show()
        }
    }
}