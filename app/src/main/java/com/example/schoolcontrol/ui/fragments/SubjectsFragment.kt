package com.example.schoolcontrol.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R

class SubjectsFragment : Fragment() {

    private lateinit var rvSubjects: RecyclerView
    private lateinit var btnAddSubject: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subjects, container, false)

        rvSubjects = view.findViewById(R.id.rvSubjects)
        btnAddSubject = view.findViewById(R.id.btnAddSubject)

        rvSubjects.layoutManager = LinearLayoutManager(requireContext())
        // TODO: Set adapter con la lista de materias desde API

        btnAddSubject.setOnClickListener {
            // TODO: Abrir CreateSubjectActivity o dialog para crear materia
        }

        return view
    }
}
