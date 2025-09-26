package com.example.schoolcontrol.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.SubjectDto
import com.example.schoolcontrol.ui.CreateSubjectActivity
import com.example.schoolcontrol.ui.adapters.SubjectAdapter
import kotlinx.coroutines.launch

class SubjectsFragment : Fragment() {

    private lateinit var rvSubjects: RecyclerView
    private lateinit var btnAddSubject: Button
    private var userRole: String? = null
    private var subjectAdapter: SubjectAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subjects, container, false)

        rvSubjects = view.findViewById(R.id.rvSubjects)
        btnAddSubject = view.findViewById(R.id.btnAddSubject)

        rvSubjects.layoutManager = LinearLayoutManager(requireContext())

        // Obtener rol del usuario
        val prefs = requireActivity().getSharedPreferences("APP_PREFS", 0)
        userRole = prefs.getString("USER_ROLE", null)

        // Mostrar botón "Agregar Materia" solo para ADMIN
        btnAddSubject.visibility = if (userRole == "ADMIN") View.VISIBLE else View.GONE

        btnAddSubject.setOnClickListener {
            val intent = Intent(requireContext(), CreateSubjectActivity::class.java)
            startActivity(intent)
        }

        loadSubjects()

        return view
    }

    private fun loadSubjects() {
        lifecycleScope.launch {
            try {
                val subjects: List<SubjectDto> = ApiClient.apiService.getSubjects()
                subjectAdapter = SubjectAdapter(subjects, userRole)
                rvSubjects.adapter = subjectAdapter
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error cargando materias: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar la lista cada vez que se vuelve al fragment
        loadSubjects()
    }
}
