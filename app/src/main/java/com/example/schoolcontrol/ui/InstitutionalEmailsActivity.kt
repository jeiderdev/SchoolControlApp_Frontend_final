package com.example.schoolcontrol.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.TeacherEmail
import com.example.schoolcontrol.ui.adapters.EmailAdapter
import kotlinx.coroutines.launch

class InstitutionalEmailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_institutional_emails)
        val rv = findViewById<RecyclerView>(R.id.rvEmails)
        rv.layoutManager = LinearLayoutManager(this)
        lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.getInstitutionalEmails()
                if (resp.isSuccessful) {
                    val emails = resp.body() ?: emptyList()
                    val teacherEmails = emails.map {
                        TeacherEmail(
                            teacher_name = "Desconocido", // o lo que corresponda
                            email = it.email
                        )
                    }.toMutableList()

                    rv.adapter = EmailAdapter(teacherEmails) { teacherEmail ->
                        Toast.makeText(this@InstitutionalEmailsActivity, "Editar: ${teacherEmail.email}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@InstitutionalEmailsActivity, "Error: ${resp.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@InstitutionalEmailsActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
