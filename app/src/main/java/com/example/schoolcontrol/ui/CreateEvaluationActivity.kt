package com.example.schoolcontrol.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.CreateEvaluationDto
import kotlinx.coroutines.launch

class CreateEvaluationActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etPercentage: EditText
    private lateinit var btnSave: Button

    private var subjectId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_evaluation)

        etName = findViewById(R.id.etEvalName)
        etDescription = findViewById(R.id.etEvalDescription)
        etPercentage = findViewById(R.id.etEvalPercentage)
        btnSave = findViewById(R.id.btnSaveEvaluation)

        subjectId = intent.getIntExtra("SUBJECT_ID", 0)
        if (subjectId == 0) {
            Toast.makeText(this, "Error: no se recibió materia", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnSave.setOnClickListener { createEvaluation() }
    }

    private fun createEvaluation() {
        val name = etName.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val percentage = etPercentage.text.toString().toIntOrNull() ?: 0

        if (name.isEmpty() || percentage <= 0) {
            Toast.makeText(this, "Nombre y porcentaje son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val dto = CreateEvaluationDto(
            name = name,
            description = description,
            percentage = percentage,
            subjectId = subjectId
        )

        lifecycleScope.launch {
            try {
                ApiClient.apiService.createEvaluation(dto)
                Toast.makeText(this@CreateEvaluationActivity, "Evaluación creada", Toast.LENGTH_SHORT).show()
                finish() // volver al detalle de la materia
            } catch (e: Exception) {
                Toast.makeText(this@CreateEvaluationActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
