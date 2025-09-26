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
import com.example.schoolcontrol.models.UpdateUserDto
import com.example.schoolcontrol.models.UserDto
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
        etPass = findViewById(R.id.etPassword)
        btnSave = findViewById(R.id.btnSave)

        loadUserData()

        btnSave.setOnClickListener {
            updateUserData()
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            try {
                val user: UserDto = ApiClient.apiService.me()
                etName.setText(user.name)
                etIdentification.setText(user.idnumber)
                etAge.setText(user.age.toString())
                etRole.setText(user.role.name)
                etEmail.setText(user.email)
                etPhotoUrl.setText(user.photo ?: "")
                etPass.setText("")

                if (!user.photo.isNullOrBlank()) {
                    Glide.with(this@ProfileActivity)
                        .load(user.photo)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(ivProfilePhoto)
                } else {
                    ivProfilePhoto.setImageResource(R.drawable.ic_person)
                }

            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error al cargar: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserData() {
        val request = UpdateUserDto(
            name = etName.text.toString().ifBlank { null },
            age = etAge.text.toString().toIntOrNull(),
            photo = etPhotoUrl.text.toString().ifBlank { null },
            password = etPass.text.toString().ifBlank { null }
        )

        lifecycleScope.launch {
            try {
                val resp = ApiClient.apiService.updateMe(request)
                Toast.makeText(this@ProfileActivity, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                loadUserData()
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
