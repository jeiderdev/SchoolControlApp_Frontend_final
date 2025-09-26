package com.example.schoolcontrol.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.CreateGradeDto
import com.example.schoolcontrol.models.GradeDto
import com.example.schoolcontrol.ui.adapters.GradeAdapter
import kotlinx.coroutines.launch

class GradesActivity : AppCompatActivity() {

    private lateinit var rvGrades: RecyclerView
    private var evaluationId: Int = 0
    private var userRole: String? = null

    private lateinit var tvSubjectName: TextView
    private lateinit var tvSubjectDescription: TextView
    private lateinit var tvTeacherName: TextView
    private lateinit var btnAddGrade: Button

    private lateinit var adapter: GradeAdapter
    private var grades: MutableList<GradeDto> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grades)

        rvGrades = findViewById(R.id.rvGrades)
        rvGrades.layoutManager = LinearLayoutManager(this)

        tvSubjectName = findViewById(R.id.tvSubjectName)
        tvSubjectDescription = findViewById(R.id.tvSubjectDescription)
        btnAddGrade = findViewById(R.id.btnAddGrade)

        evaluationId = intent.getIntExtra("EVALUATION_ID", 0)
        val prefs = getSharedPreferences("APP_PREFS", 0)
        userRole = prefs.getString("USER_ROLE", null)

        if (evaluationId == 0) {
            Toast.makeText(this, "Error: evaluación no recibida", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Mostrar botón solo a TEACHER
        if (userRole == "TEACHER") {
            btnAddGrade.visibility = View.VISIBLE
            btnAddGrade.setOnClickListener { showAddGradeDialog() }
        }

        loadGrades()
        loadEvaluationInfo()
    }

    private fun loadGrades() {
        lifecycleScope.launch {
            try {
                grades = ApiClient.apiService.getGradesByEvaluation(evaluationId).toMutableList()
                adapter = GradeAdapter(grades, userRole == "TEACHER")
                rvGrades.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(this@GradesActivity, "Error al cargar notas: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadEvaluationInfo() {
        lifecycleScope.launch {
            try {
                val evaluation = ApiClient.apiService.getEvaluationById(evaluationId)
                val subject = evaluation.subject
                if (subject != null) {
                    tvSubjectName.text = "Materia: ${subject.name}"
                    tvSubjectDescription.text = "Descripción: ${subject.description ?: "Sin descripción"}"
                }
            } catch (e: Exception) {
                Toast.makeText(this@GradesActivity, "Error cargando info: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddGradeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_grade, null)
        val etStudentId = dialogView.findViewById<EditText>(R.id.etStudentId)
        val etScore = dialogView.findViewById<EditText>(R.id.etScore)

        AlertDialog.Builder(this)
            .setTitle("Agregar Nota")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val studentId = etStudentId.text.toString().toIntOrNull()
                val score = etScore.text.toString().toDoubleOrNull()

                if (studentId != null && score != null) {
                    lifecycleScope.launch {
                        try {
                            val newGrade = ApiClient.apiService.createGrade(
                                CreateGradeDto(
                                    evaluationId = evaluationId,
                                    studentId = studentId,
                                    score = score
                                )
                            )
                            grades.add(newGrade)
                            adapter.notifyItemInserted(grades.size - 1)
                            Toast.makeText(this@GradesActivity, "Nota agregada", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@GradesActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
