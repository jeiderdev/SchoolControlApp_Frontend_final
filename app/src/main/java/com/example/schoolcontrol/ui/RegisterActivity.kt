package com.example.schoolcontrol.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.CreateUserDto
import com.example.schoolcontrol.models.UserRole
import com.example.schoolcontrol.models.UserDto
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etIdNumber: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAge: EditText
    private lateinit var etPassword: EditText
    private lateinit var spinnerRole: Spinner
    private lateinit var etPhotoUrl: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName)
        etIdNumber = findViewById(R.id.etIdNumber)
        etEmail = findViewById(R.id.etEmail)
        etAge = findViewById(R.id.etAge)
        etPassword = findViewById(R.id.etPassword)
        spinnerRole = findViewById(R.id.spinnerRole)
        etPhotoUrl = findViewById(R.id.etPhotoUrl)
        btnRegister = findViewById(R.id.btnRegister)

        setupRoleSpinner()

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val idnumber = etIdNumber.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val age = etAge.text.toString().toIntOrNull()
            val password = etPassword.text.toString()
            val role = spinnerRole.selectedItem.toString()
            val photo = etPhotoUrl.text.toString().ifBlank { null }

            if (name.isEmpty() || idnumber.isEmpty() || email.isEmpty() || age == null || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dto = CreateUserDto(
                name = name,
                idnumber = idnumber,
                email = email,
                age = age,
                password = password,
                role = UserRole.valueOf(role),
                photo = photo,
                active = true
            )

            lifecycleScope.launch {
                try {
                    val newUser: UserDto = ApiClient.apiService.register(dto)
                    Toast.makeText(this@RegisterActivity, "Usuario ${newUser.name} registrado", Toast.LENGTH_SHORT).show()
                    setResult(AppCompatActivity.RESULT_OK)
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRoleSpinner() {
        val roles = UserRole.values().map { it.name } // ["ADMIN", "TEACHER", "STUDENT"]
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter
    }
}
