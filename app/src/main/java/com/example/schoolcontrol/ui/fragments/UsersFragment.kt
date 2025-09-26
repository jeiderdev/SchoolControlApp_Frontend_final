package com.example.schoolcontrol.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

    // 🔹 Lanzador para abrir RegisterActivity y recibir resultado
    private val registerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            // Usuario creado, recargar la lista
            loadUsers()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        rvUsers = view.findViewById(R.id.rvUsers)
        btnAddUser = view.findViewById(R.id.btnAddUser)

        rvUsers.layoutManager = LinearLayoutManager(requireContext())

        loadUsers()

        btnAddUser.setOnClickListener {
            // Abrir RegisterActivity usando el launcher
            registerLauncher.launch(Intent(requireContext(), RegisterActivity::class.java))
        }

        return view
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            try {
                val users: List<UserDto> = ApiClient.apiService.getAllUsers()
                rvUsers.adapter = UserAdapter(users)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error cargando usuarios: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

