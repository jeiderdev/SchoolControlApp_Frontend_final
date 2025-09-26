package com.example.schoolcontrol.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.TeacherEmail
import com.example.schoolcontrol.models.InstitutionalEmailRequest
import com.example.schoolcontrol.ui.adapters.EmailAdapter
import kotlinx.coroutines.launch

class CorreosFragment: Fragment() {
    private lateinit var rv: RecyclerView
    private lateinit var adapter: EmailAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_correos, container, false)
        rv = v.findViewById(R.id.rvEmails)
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = EmailAdapter(mutableListOf(), onEdit = { teacherEmail -> showEditDialog(teacherEmail) })
        rv.adapter = adapter
        loadEmails()
        return v
    }

    private fun loadEmails() {
        lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.getTeachersEmails()
                if (resp.isSuccessful) {
                    val list = resp.body() ?: emptyList()
                    adapter.updateData(list.toMutableList())
                } else {
                    Toast.makeText(requireContext(), "Error cargando correos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showEditDialog(item: TeacherEmail) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Editar correo de ${item.teacher_name ?: "Profesor"}")
        val view = layoutInflater.inflate(R.layout.dialog_edit_email, null)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        etEmail.setText(item.email ?: "")
        builder.setView(view)
        builder.setPositiveButton("Guardar") { _, _ ->
            val newEmail = etEmail.text.toString()
            // NOTE: your backend needs an endpoint to update user email (PUT /users/{id}/email).
            // If backend returns user_id in teachers list, use it here. If not, adapt to your backend.
            // For now we attempt to call /users/{id}/email with a payload {"email": newEmail}
            // If your /teachers/emails returns also user_id, replace 0 with that id.
            Toast.makeText(requireContext(), "Si eres admin podrás guardar este correo (verifica endpoint backend)", Toast.LENGTH_SHORT).show()
            // Implement call here when your backend returns user id with the teachers list.
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}

