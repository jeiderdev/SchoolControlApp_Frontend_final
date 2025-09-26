package com.example.schoolcontrol.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.GradeCreate
import com.example.schoolcontrol.models.GradeResponse
import com.example.schoolcontrol.ui.adapters.NoteAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class NotasFragment : Fragment() {
    private lateinit var rv: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_notas, container, false)
        rv = v.findViewById(R.id.rvNotes)
        fabAdd = v.findViewById(R.id.fabAddNote)
        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter = NoteAdapter(mutableListOf(),
            onEdit = { note -> showEditDialog(note) },
            onDelete = { note -> deleteNoteConfirm(note.id) }
        )
        rv.adapter = adapter

        fabAdd.setOnClickListener { showCreateDialog() }

        loadGrades()
        return v
    }

    private fun loadGrades() {
        lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.getGrades(null)
                if (resp.isSuccessful) {
                    val grades = resp.body() ?: emptyList()
                    adapter.updateData(grades)
                } else {
                    Toast.makeText(requireContext(), "Error cargando notas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error cargando notas: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showCreateDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Agregar nota")
        val view = layoutInflater.inflate(R.layout.dialog_grade, null)
        val etStudent = view.findViewById<EditText>(R.id.etStudentId)
        val etTeacher = view.findViewById<EditText>(R.id.etTeacherId)
        val etSubject = view.findViewById<EditText>(R.id.etSubject)
        val etValue = view.findViewById<EditText>(R.id.etValue)
        val etTrimester = view.findViewById<EditText>(R.id.etTrimester)
        builder.setView(view)
        builder.setPositiveButton("Crear") { _, _ ->
            val studentId = etStudent.text.toString().toIntOrNull() ?: 0
            val teacherId = etTeacher.text.toString().toIntOrNull() ?: 0
            val subject = etSubject.text.toString()
            val value = etValue.text.toString().toDoubleOrNull() ?: 0.0
            val trimester = etTrimester.text.toString().toIntOrNull() ?: 1

            lifecycleScope.launch {
                try {
                    val req = GradeCreate(student_id = studentId, teacher_id = teacherId, subject = subject, grade_value = value, trimester = trimester)
                    val resp = ApiClient.apiService.createGrade(req)
                    if (resp.isSuccessful) {
                        Toast.makeText(requireContext(), "Nota creada", Toast.LENGTH_SHORT).show()
                        loadGrades()
                    } else {
                        Toast.makeText(requireContext(), "Error creando nota", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun showEditDialog(note: GradeResponse) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Editar nota")
        val view = layoutInflater.inflate(R.layout.dialog_grade, null)
        val etStudent = view.findViewById<EditText>(R.id.etStudentId)
        val etTeacher = view.findViewById<EditText>(R.id.etTeacherId)
        val etSubject = view.findViewById<EditText>(R.id.etSubject)
        val etValue = view.findViewById<EditText>(R.id.etValue)
        val etTrimester = view.findViewById<EditText>(R.id.etTrimester)

        etStudent.setText(note.student_id.toString())
        etTeacher.setText(note.teacher_id.toString())
        etSubject.setText(note.subject)
        etValue.setText(note.grade_value.toString())
        etTrimester.setText(note.trimester.toString())

        builder.setView(view)
        builder.setPositiveButton("Actualizar") { _, _ ->
            val studentId = etStudent.text.toString().toIntOrNull() ?: note.student_id
            val teacherId = etTeacher.text.toString().toIntOrNull() ?: note.teacher_id
            val subject = etSubject.text.toString()
            val value = etValue.text.toString().toDoubleOrNull() ?: note.grade_value
            val trimester = etTrimester.text.toString().toIntOrNull() ?: note.trimester

            lifecycleScope.launch {
                try {
                    val update = GradeCreate(student_id = studentId, teacher_id = teacherId, subject = subject, grade_value = value, trimester = trimester)
                    val resp = ApiClient.apiService.updateGrade(note.id, update)
                    if (resp.isSuccessful) {
                        Toast.makeText(requireContext(), "Nota actualizada", Toast.LENGTH_SHORT).show()
                        loadGrades()
                    } else {
                        Toast.makeText(requireContext(), "Error actualizando nota", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun deleteNoteConfirm(gradeId: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Eliminar nota")
        builder.setMessage("¿Seguro que deseas eliminar la nota?")
        builder.setPositiveButton("Sí") { _, _ ->
            lifecycleScope.launch {
                try {
                    val resp = ApiClient.apiService.deleteGrade(gradeId)
                    if (resp.isSuccessful) {
                        Toast.makeText(requireContext(), "Nota eliminada", Toast.LENGTH_SHORT).show()
                        loadGrades()
                    } else {
                        Toast.makeText(requireContext(), "Error eliminando nota", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }
}
