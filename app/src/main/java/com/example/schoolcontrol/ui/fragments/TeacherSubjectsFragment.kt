package com.example.schoolcontrol.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R

class TeacherSubjectsFragment : Fragment() {

    private lateinit var rvSubjects: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_teacher_subjects, container, false)

        rvSubjects = view.findViewById(R.id.rvSubjects)
        rvSubjects.layoutManager = LinearLayoutManager(requireContext())
        // TODO: Set adapter con las materias del profesor desde API
        // Al click en una materia -> abrir detalle para ver estudiantes y evaluaciones

        return view
    }
}
