package com.example.habitconnect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.habitconnect.db.model.Task
import com.example.habitconnect.viewmodel.FragmentTaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class NewTaskSheet : BottomSheetDialogFragment()
{
    private lateinit var viewModel: FragmentTaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.dialog_add_task, container, false)
        val application = requireActivity().application
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[FragmentTaskViewModel::class.java]
        v.findViewById<MaterialButton>(R.id.saveButton).setOnClickListener {
            saveAction(v)
        }
        return v
    }


    private fun saveAction(view:View)
    {
        var newTask = Task(0,"No name",1,false)

        val nome = view.findViewById<TextInputEditText>(R.id.testo).text.toString()
        if (!nome.isNullOrEmpty()){
            newTask.nome = nome
        }
        val obiettivo = view.findViewById<TextInputEditText>(R.id.obiettivo).text.toString()
        if (!obiettivo.isNullOrEmpty()){
            newTask.obiettivo = obiettivo.toInt()
        }
        viewModel.insertTask(newTask)
        dismiss()
    }

}