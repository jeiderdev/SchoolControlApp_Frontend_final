package com.example.schoolcontrol.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.CreateGradeDto
import com.example.schoolcontrol.models.EnrollmentDto
import com.example.schoolcontrol.models.GradeDto
import com.example.schoolcontrol.models.UpdateGradeDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GradeAdapter(
    private val grades: MutableList<GradeDto>,
    private val isTeacher: Boolean,
    private val enrollments: List<EnrollmentDto>? = null,
    private val evaluationId: Int? = -1
) : RecyclerView.Adapter<GradeAdapter.GradeViewHolder>() {

    private val elementsToShow: MutableList<GradeDto>

    init {
        elementsToShow = if (isTeacher && enrollments != null && evaluationId != null && evaluationId > -1) {
            // Si es profesor, mostrar todos los estudiantes inscritos con su nota (o nota vacía)
            enrollments.map { enrollment ->
                grades.find { it.studentId == enrollment.student?.id }
                    ?: GradeDto(
                        id = -1,
                        studentId = enrollment.student?.id ?: 0,
                        evaluationId = evaluationId,
                        score = 0.0,
                        createdAt = "",
                        updatedAt = "",
                        student = enrollment.student
                    )
            }.toMutableList()
        } else {
            // Si es estudiante, mostrar solo las notas que recibió
            grades.toMutableList()
        }
    }

    inner class GradeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStudentName: TextView = view.findViewById(R.id.tvStudentName)
        val etGrade: EditText = view.findViewById(R.id.etGrade)
        val btnSave: Button = view.findViewById(R.id.btnSaveGrade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grade, parent, false)
        return GradeViewHolder(view)
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        var grade = elementsToShow[position]

        holder.tvStudentName.text = grade.student?.name ?: "Sin nombre"
        val nota = if (grade.id == -1) "" else grade.score.toString()
        holder.etGrade.setText(nota)

        if (isTeacher) {
            holder.etGrade.isEnabled = true
            holder.btnSave.visibility = View.VISIBLE

            holder.btnSave.setOnClickListener {
                val newValue = holder.etGrade.text.toString().toDoubleOrNull()
                if (newValue != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            if (grade.id == -1) {
                                // Crear nueva nota
                                val newGrade = ApiClient.apiService.createGrade(
                                    CreateGradeDto(
                                        evaluationId = grade.evaluationId,
                                        studentId = grade.studentId,
                                        score = newValue
                                    )
                                )
                                grades.add(newGrade)
                                elementsToShow[position] = newGrade
                                grade = newGrade
                            } else {
                                // Actualizar nota existente
                                ApiClient.apiService.updateGrade(
                                    grade.id,
                                    UpdateGradeDto(score = newValue)
                                )
                                grade = grade.copy(score = newValue)
                                elementsToShow[position] = grade
                            }

                            launch(Dispatchers.Main) {
                                Toast.makeText(holder.itemView.context, "Nota guardada", Toast.LENGTH_SHORT).show()
                                notifyItemChanged(position)
                            }
                        } catch (e: Exception) {
                            launch(Dispatchers.Main) {
                                Toast.makeText(holder.itemView.context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        } else {
            // Estudiante solo visualiza
            holder.etGrade.isEnabled = false
            holder.btnSave.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = elementsToShow.size
}
