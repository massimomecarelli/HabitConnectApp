package com.example.habitconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.habitconnect.db.model.Task
import com.example.habitconnect.viewmodel.ActivityCommunityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class NewCommentSheet : BottomSheetDialogFragment()
{
    private lateinit var viewModel: ActivityCommunityViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.dialog_add_comment, container, false)
        val application = requireActivity().application
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[ActivityCommunityViewModel::class.java]
        v.findViewById<MaterialButton>(R.id.publish).setOnClickListener {
            publish(v)
        }
        return v
    }


    // publish() prende la view così da poter recuperare il testo nel campo di input
    private fun publish(view: View)
    {
        val testo = view.findViewById<TextInputEditText>(R.id.testo).text.toString()
        viewModel.insertComment(testo)
        dismiss()
    }

}