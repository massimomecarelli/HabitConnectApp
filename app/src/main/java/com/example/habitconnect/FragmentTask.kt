package com.example.habitconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitconnect.viewmodel.FragmentTaskViewModel

class FragmentTask : Fragment() {

    private lateinit var viewModel: FragmentTaskViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // LayoutInflater.inflate ritorna una view (che posso usare dove voglio)
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

}