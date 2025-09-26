package com.example.schoolcontrol.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.EvaluationDto
import com.example.schoolcontrol.models.SubjectDto
import com.example.schoolcontrol.ui.adapters.EvaluationAdapter
import kotlinx.coroutines.launch

class SubjectDetailActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvTeacher: TextView
    private lateinit var rvEvaluations: RecyclerView
    private lateinit var btnAddEvaluation: Button

    private var subjectId: Int = 0
    private var userRole: String? = null
    private var evaluationAdapter: EvaluationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_detail)

        tvName = findViewById(R.id.tvSubjectName)
        tvDescription = findViewById(R.id.tvSubjectDescription)
        tvTeacher = findViewById(R.id.tvSubjectTeacher)
        rvEvaluations = findViewById(R.id.rvEvaluations)
        btnAddEvaluation = findViewById(R.id.btnAddEvaluation)

        rvEvaluations.layoutManager = LinearLayoutManager(this)

        // Obtener rol y subjectId
        val prefs = getSharedPreferences("APP_PREFS", 0)
        userRole = prefs.getString("USER_ROLE", null)
        subjectId = intent.getIntExtra("SUBJECT_ID", 0)

        Toast.makeText(this, "Rol: $userRole", Toast.LENGTH_SHORT).show()

        // Mostrar botón solo a TEACHER
        btnAddEvaluation.visibility = if (userRole == "TEACHER") View.VISIBLE else View.GONE

        btnAddEvaluation.setOnClickListener {
            val intent = Intent(this, CreateEvaluationActivity::class.java)
            intent.putExtra("SUBJECT_ID", subjectId)
            startActivity(intent)
        }

        loadSubjectInfo()
        loadEvaluations()
    }

    private fun loadSubjectInfo() {
        lifecycleScope.launch {
            try {
                val subject: SubjectDto = ApiClient.apiService.getSubjectById(subjectId) // necesitas este endpoint
                tvName.text = subject.name
                tvDescription.text = subject.description ?: "Sin descripción"
                tvTeacher.text = subject.teacher?.name ?: "Sin asignar"
            } catch (e: Exception) {
                Toast.makeText(this@SubjectDetailActivity, "Error cargando materia: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadEvaluations() {
        lifecycleScope.launch {
            try {
                val grades = ApiClient.apiService.getGradesBySubject(subjectId)
                val evaluations: List<EvaluationDto> = ApiClient.apiService.getEvaluationsBySubject(subjectId)
                evaluationAdapter = EvaluationAdapter(evaluations, grades, userRole)
                rvEvaluations.adapter = evaluationAdapter
            } catch (e: Exception) {
                Toast.makeText(this@SubjectDetailActivity, "Error cargando evaluaciones: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadEvaluations()
    }

}
