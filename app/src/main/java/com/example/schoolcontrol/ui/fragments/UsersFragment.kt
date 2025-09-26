package com.example.schoolcontrol.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.UserDto
import com.example.schoolcontrol.ui.RegisterActivity
import com.example.schoolcontrol.ui.adapters.UserAdapter
import kotlinx.coroutines.launch

class UsersFragment : Fragment() {

    private lateinit var rvUsers: RecyclerView
    private lateinit var btnAddUser: Button
    private lateinit var rgRoles: RadioGroup

    // 🔹 Rol actual (por defecto "students")
    private var currentRole: String = "students"

    private val registerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            loadUsers(currentRole)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        rvUsers = view.findViewById(R.id.rvUsers)
        btnAddUser = view.findViewById(R.id.btnAddUser)
        rgRoles = view.findViewById(R.id.rgRoles)

        rvUsers.layoutManager = LinearLayoutManager(requireContext())

        // Cargar estudiantes al inicio
        loadUsers("students")

        // 🔹 Cambiar de rol según selección
        rgRoles.setOnCheckedChangeListener { _, checkedId ->
            currentRole = when (checkedId) {
                R.id.rbStudents -> "students"
                R.id.rbTeachers -> "teachers"
                R.id.rbAdmins -> "admins"
                else -> "students"
            }
            loadUsers(currentRole)
        }

        btnAddUser.setOnClickListener {
            registerLauncher.launch(Intent(requireContext(), RegisterActivity::class.java))
        }

        return view
    }

    private fun loadUsers(role: String) {
        lifecycleScope.launch {
            try {
                val users: List<UserDto> = when (role) {
                    "students" -> ApiClient.apiService.getStudents()
                    "teachers" -> ApiClient.apiService.getTeachers()
                    "admins" -> ApiClient.apiService.getAdmins()
                    else -> emptyList()
                }
                rvUsers.adapter = UserAdapter(users)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error cargando $role: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
