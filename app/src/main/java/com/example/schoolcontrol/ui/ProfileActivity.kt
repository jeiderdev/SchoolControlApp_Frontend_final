package com.example.schoolcontrol.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.UserResponse
import com.example.schoolcontrol.models.UserUpdateRequest
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var ivProfilePhoto: ImageView
    private lateinit var etName: EditText
    private lateinit var etIdentification: EditText
    private lateinit var etAge: EditText
    private lateinit var etRole: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhotoUrl: EditText
    private lateinit var etPass: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto)
        etName = findViewById(R.id.etName)
        etIdentification = findViewById(R.id.etIdentification)
        etAge = findViewById(R.id.etAge)
        etRole = findViewById(R.id.etRole)
        etEmail = findViewById(R.id.etEmail)
        etPhotoUrl = findViewById(R.id.etPhotoUrl)
        etPass =findViewById(R.id.etPassword)
        btnSave = findViewById(R.id.btnSave)

        // 1. Cargar datos del usuario al abrir el perfil
        loadUserData()

        // 2. Guardar cambios
        btnSave.setOnClickListener {
            updateUserData()
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            try {
                val user: UserResponse = ApiClient.apiService.me()
                etName.setText(user.full_name)
                etIdentification.setText(user.identification_number)
                etAge.setText(user.age.toString())
                etRole.setText(user.role)
                etEmail.setText(user.email)
                etPhotoUrl.setText(user.photo ?: "")
                etPass.setText("");

                if (!user.photo.isNullOrBlank()) {
                    // Toast.makeText(this@ProfileActivity, "Hay imagen", Toast.LENGTH_SHORT).show()
                    Glide.with(this@ProfileActivity)
                        .load(user.photo)
                        .placeholder(R.drawable.ic_person) // mientras carga
                        .error(R.drawable.ic_person)       // si falla
                        .into(ivProfilePhoto)
                } else {
                    // Toast.makeText(this@ProfileActivity, "No hay imagen", Toast.LENGTH_SHORT).show()
                    ivProfilePhoto.setImageResource(R.drawable.ic_person)
                }

            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error al cargar: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserData() {
        val request = UserUpdateRequest(
            full_name = etName.text.toString(),
            identification_number = etIdentification.text.toString(),
            age = etAge.text.toString().toIntOrNull(),
            photo = etPhotoUrl.text.toString().ifBlank { null },
            password = etPass.text.toString().ifBlank { null }
        )

        lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.updateMe(request)
                if (resp.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                    // Recargar datos para reflejar cambios
                    loadUserData()
                } else {
                    Toast.makeText(this@ProfileActivity, "Error: ${resp.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
