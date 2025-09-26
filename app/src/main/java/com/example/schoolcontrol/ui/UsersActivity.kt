package com.example.schoolcontrol.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.ui.adapters.UserAdapter
import kotlinx.coroutines.launch

class UsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        val rv = findViewById<RecyclerView>(R.id.rvUsers)
        rv.layoutManager = LinearLayoutManager(this)
        lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.getUsers()
                if (resp.isSuccessful) {
                    val users = resp.body() ?: emptyList()
                    rv.adapter = UserAdapter(users.toMutableList(),
                        onChangeRole = { user ->
                            // TODO: show dialog to pick role and call ApiClient.apiService.updateUserRole
                        },
                        onDelete = { user ->
                            // TODO: call ApiClient.apiService.deleteUser(user.id)
                        })
                } else {
                    Toast.makeText(this@UsersActivity, "Error: ${resp.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@UsersActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
