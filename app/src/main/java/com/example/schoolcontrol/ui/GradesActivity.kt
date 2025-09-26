package com.example.schoolcontrol.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.EnrollmentDto
import com.example.schoolcontrol.models.GradeDto
import com.example.schoolcontrol.ui.adapters.GradeAdapter
import com.example.schoolcontrol.ui.dialogs.AddGradeDialog
import kotlinx.coroutines.launch

class GradesActivity : AppCompatActivity() {

    private lateinit var rvGrades: RecyclerView
    private var evaluationId: Int = 0
    private var subjectId: Int = 0
    private var userRole: String? = null

    private lateinit var tvSubjectName: TextView
    private lateinit var tvSubjectDescription: TextView
    private lateinit var tvTeacherName: TextView
    private lateinit var btnAddGrade: Button

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
        btnAddGrade = findViewById(R.id.btnAddGrade)

        if (evaluationId < 0 || subjectId < 0) {
            Toast.makeText(this, "Error: datos incompletos" + evaluationId + " " + subjectId, Toast.LENGTH_SHORT).show()
            finish()
        }

        // Botón solo para TEACHER
        if (userRole == "TEACHER") {
            btnAddGrade.visibility = Button.VISIBLE
            btnAddGrade.setOnClickListener {
                openAddGradeDialog()
            }
        } else {
            btnAddGrade.visibility = Button.GONE
        }

        loadGrades()
        loadEvaluationInfo()
    }

    private fun loadGrades() {
        lifecycleScope.launch {
            try {
                val grades: List<GradeDto> = ApiClient.apiService.getGradesByEvaluation(evaluationId)
                val adapter = GradeAdapter(grades.toMutableList(), userRole == "TEACHER")
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
                    tvSubjectDescription.text = "Descripción: ${subject.description}"
                }

            } catch (e: Exception) {
                Toast.makeText(this@GradesActivity, "Error cargando info: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openAddGradeDialog() {
        lifecycleScope.launch {
            try {
                // Traer estudiantes inscritos en la materia
                val enrollments: List<EnrollmentDto> = ApiClient.apiService.getEnrollmentsBySubject(subjectId)

                if (enrollments.isEmpty()) {
                    Toast.makeText(this@GradesActivity, "No hay estudiantes inscritos", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Abrir un diálogo para elegir estudiante + nota
                val dialog = AddGradeDialog.newInstance(evaluationId, enrollments)
                dialog.show(supportFragmentManager, "AddGradeDialog")

            } catch (e: Exception) {
                Toast.makeText(this@GradesActivity, "Error cargando estudiantes: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
