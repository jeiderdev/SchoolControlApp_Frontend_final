package com.example.schoolcontrol.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.*
import com.example.schoolcontrol.ui.adapters.EnrollmentAdapter
import kotlinx.coroutines.launch

class EditSubjectActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etTeacher: EditText
    private lateinit var btnSave: Button
    private lateinit var rvEnrollments: RecyclerView
    private lateinit var btnAddEnrollment: Button

    private var subjectId: Int = 0
    private var enrollments: MutableList<EnrollmentDto> = mutableListOf()
    private var enrollmentAdapter: EnrollmentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_subject)

        etName = findViewById(R.id.etSubjectName)
        etDescription = findViewById(R.id.etSubjectDescription)
        etTeacher = findViewById(R.id.etTeacher)
        btnSave = findViewById(R.id.btnSaveSubject)
        rvEnrollments = findViewById(R.id.rvEnrollments)
        btnAddEnrollment = findViewById(R.id.btnAddEnrollment)

        rvEnrollments.layoutManager = LinearLayoutManager(this)
        enrollmentAdapter = EnrollmentAdapter(enrollments)
        rvEnrollments.adapter = enrollmentAdapter

        subjectId = intent.getIntExtra("SUBJECT_ID", 0)
        if (subjectId == 0) {
            Toast.makeText(this, "Error: no se recibió la materia", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Cargar materia y estudiantes matriculados
        loadSubject()

        // Guardar cambios en nombre y descripción
        btnSave.setOnClickListener { updateSubject() }

        // Agregar estudiante
        btnAddEnrollment.setOnClickListener {
            lifecycleScope.launch {
                showAddStudentDialog()
            }
        }
    }

    private fun loadSubject() {
        lifecycleScope.launch {
            try {
                val subject = ApiClient.apiService.getSubjectById(subjectId)
                etName.setText(subject.name)
                etDescription.setText(subject.description ?: "")
                etTeacher.setText(subject.teacher?.name ?: "Sin profesor")
                val enr = ApiClient.apiService.getEnrollmentsBySubject(subjectId)
                enrollments.clear()
                enrollments.addAll(enr)
                enrollmentAdapter?.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@EditSubjectActivity, "Error al cargar materia: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSubject() {
        val updateDto = UpdateSubjectDto(
            name = etName.text.toString(),
            description = etDescription.text.toString()
        )
        lifecycleScope.launch {
            try {
                val updated = ApiClient.apiService.updateSubject(subjectId, updateDto)
                Toast.makeText(this@EditSubjectActivity, "Materia actualizada", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@EditSubjectActivity, "Error al actualizar: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun showAddStudentDialog() {
        var unenrolledStudents = ApiClient.apiService.getUnenrolledStudents(subjectId)
        val studentNames = unenrolledStudents.map { it.name }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona un estudiante")
        val listView = ListView(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentNames)
        listView.adapter = adapter
        builder.setView(listView)
        val dialog = builder.create()
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = unenrolledStudents[position]
            enrollStudent(selectedUser.id)
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun enrollStudent(userId: Int) {
        val createEnrollment = CreateEnrollmentDto(studentId = userId, subjectId = subjectId)
        lifecycleScope.launch {
            try {
                val enrollment = ApiClient.apiService.enrollStudent(createEnrollment)
                enrollments.add(enrollment)
                enrollmentAdapter?.notifyItemInserted(enrollments.size - 1)
                Toast.makeText(this@EditSubjectActivity, "Estudiante matriculado", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@EditSubjectActivity, "Error al matricular: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
