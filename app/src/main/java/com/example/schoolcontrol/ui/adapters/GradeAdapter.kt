package com.example.schoolcontrol.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.GradeDto
import com.example.schoolcontrol.models.UpdateGradeDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GradeAdapter(
    private val grades: MutableList<GradeDto>,
    private val isTeacher: Boolean
) : RecyclerView.Adapter<GradeAdapter.GradeViewHolder>() {

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
        var grade = grades[position]

        holder.tvStudentName.text = grade.student?.name
        holder.etGrade.setText(grade.score.toString())

        if (isTeacher) {
            holder.etGrade.isEnabled = true
            holder.btnSave.visibility = View.VISIBLE
            holder.btnSave.setOnClickListener {
                val newValue = holder.etGrade.text.toString().toDoubleOrNull()
                if (newValue != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            ApiClient.apiService.updateGrade(
                                grade.id,
                                UpdateGradeDto(score = newValue)
                            )
                            grade = grade.copy(score = newValue)
                            launch(Dispatchers.Main) {
                                Toast.makeText(holder.itemView.context, "Nota actualizada", Toast.LENGTH_SHORT).show()
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
            // Estudiante: solo puede ver
            holder.etGrade.isEnabled = false
            holder.btnSave.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = grades.size
}
