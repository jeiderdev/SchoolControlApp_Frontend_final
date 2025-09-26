package com.example.schoolcontrol.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.CreateSubjectDto
import com.example.schoolcontrol.models.SubjectDto
import com.example.schoolcontrol.models.UserDto
import kotlinx.coroutines.launch

class CreateSubjectActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var spinnerTeacher: Spinner
    private lateinit var btnSave: Button

    private var teachersList: List<UserDto> = emptyList()
    private var selectedTeacherId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_subject)

        etName = findViewById(R.id.etName)
        etDescription = findViewById(R.id.etDescription)
        spinnerTeacher = findViewById(R.id.spinnerTeacher)
        btnSave = findViewById(R.id.btnSave)

        loadTeachers() // cargar profesores en el spinner

        spinnerTeacher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long
            ) {
                selectedTeacherId = if (position == 0) null else teachersList[position - 1].id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedTeacherId = null
            }
        }

        btnSave.setOnClickListener {
            createSubject()
        }
    }

    private fun loadTeachers() {
        lifecycleScope.launch {
            try {
                teachersList = ApiClient.apiService.getTeachers()
                val names = mutableListOf("Sin asignar") // opción inicial vacía
                names.addAll(teachersList.map { it.name })
                val adapter = ArrayAdapter(this@CreateSubjectActivity, android.R.layout.simple_spinner_item, names)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTeacher.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(this@CreateSubjectActivity, "Error cargando profesores: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                teachersList = emptyList()
                spinnerTeacher.adapter = ArrayAdapter(this@CreateSubjectActivity, android.R.layout.simple_spinner_item, listOf("Sin profesores"))
            }
        }
    }

    private fun createSubject() {
        val name = etName.text.toString().trim()
        val description = etDescription.text.toString().trim().ifBlank { null }

        if (name.isEmpty()) {
            Toast.makeText(this, "Ingresa el nombre de la materia", Toast.LENGTH_SHORT).show()
            return
        }

        val request = CreateSubjectDto(
            name = name,
            description = description,
            teacherId = selectedTeacherId
        )

        lifecycleScope.launch {
            try {
                val subject: SubjectDto = ApiClient.apiService.createSubject(request)
                Toast.makeText(this@CreateSubjectActivity, "Materia creada: ${subject.name}", Toast.LENGTH_SHORT).show()
                finish() // cerrar actividad y volver al fragmento de materias
            } catch (e: Exception) {
                Toast.makeText(this@CreateSubjectActivity, "Error al crear materia: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
