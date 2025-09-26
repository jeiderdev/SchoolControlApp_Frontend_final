package com.example.schoolcontrol.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.CreateGradeDto
import com.example.schoolcontrol.models.EnrollmentDto
import kotlinx.coroutines.launch

class AddGradeDialog : DialogFragment() {

    private var evaluationId: Int = 0
    private lateinit var enrollments: List<EnrollmentDto>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        evaluationId = requireArguments().getInt("EVALUATION_ID")
        enrollments = requireArguments().getSerializable("ENROLLMENTS") as List<EnrollmentDto>

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_grade, null)
        val spStudents: Spinner = view.findViewById(R.id.spStudents)
        val etScore: EditText = view.findViewById(R.id.etScore)

        // Llenar spinner con nombres de estudiantes
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            enrollments.map { it.student?.name }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStudents.adapter = adapter

        return AlertDialog.Builder(requireContext())
            .setTitle("Agregar Nota")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val pos = spStudents.selectedItemPosition
                val studentId = enrollments[pos].studentId
                val score = etScore.text.toString().toDoubleOrNull()

                if (score == null) {
                    Toast.makeText(requireContext(), "Ingrese una nota válida", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    try {
                        ApiClient.apiService.createGrade(
                            CreateGradeDto(
                                evaluationId = evaluationId,
                                studentId = studentId,
                                score = score
                            )
                        )
                        Toast.makeText(requireContext(), "Nota creada", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    companion object {
        fun newInstance(evaluationId: Int, enrollments: List<EnrollmentDto>): AddGradeDialog {
            val args = Bundle().apply {
                putInt("EVALUATION_ID", evaluationId)
                putSerializable("ENROLLMENTS", ArrayList(enrollments))
            }
            val fragment = AddGradeDialog()
            fragment.arguments = args
            return fragment
        }
    }
}
