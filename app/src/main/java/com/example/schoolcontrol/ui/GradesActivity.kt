package com.example.schoolcontrol.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.GradeDto
import com.example.schoolcontrol.ui.adapters.GradeAdapter
import kotlinx.coroutines.launch

class GradesActivity : AppCompatActivity() {

    private lateinit var rvGrades: RecyclerView
    private var evaluationId: Int = 0
    private var subjectId: Int = 0
    private var userRole: String? = null
    private lateinit var tvSubjectName: TextView
    private lateinit var tvSubjectDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grades)

        rvGrades = findViewById(R.id.rvGrades)
        rvGrades.layoutManager = LinearLayoutManager(this)

        evaluationId = intent.getIntExtra("EVALUATION_ID", -1)
        subjectId = intent.getIntExtra("SUBJECT_ID", -1)

        val prefs = getSharedPreferences("APP_PREFS", 0)
        userRole = prefs.getString("USER_ROLE", null)

        tvSubjectName = findViewById(R.id.tvSubjectName)
        tvSubjectDescription = findViewById(R.id.tvSubjectDescription)

        if (evaluationId == 0) {
            Toast.makeText(this, "Error: evaluación no recibida", Toast.LENGTH_SHORT).show()
            finish()
        }

        loadGrades()
        loadEvaluationInfo()
    }

    private fun loadGrades() {
        lifecycleScope.launch {
            try {
                val grades: List<GradeDto> =
                    ApiClient.apiService.getGradesByEvaluation(evaluationId)
                val enrolls = ApiClient.apiService.getEnrollmentsBySubject(subjectId)

                val adapter = GradeAdapter(grades.toMutableList(), userRole == "TEACHER", enrolls, evaluationId)
                rvGrades.adapter = adapter

            } catch (e: Exception) {
                Toast.makeText(
                    this@GradesActivity,
                    "Error al cargar notas: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
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
                    tvSubjectDescription.text =
                        "Descripción: ${subject.description ?: "Sin descripción"}"
                }


            } catch (e: Exception) {
                Toast.makeText(
                    this@GradesActivity,
                    "Error cargando info: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
