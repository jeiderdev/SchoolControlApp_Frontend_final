package com.example.schoolcontrol.ui
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.UserCreateRequest
import kotlinx.coroutines.launch

class RegisterActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val etEmail = findViewById<EditText>(R.id.etRegEmail)
        val etPass = findViewById<EditText>(R.id.etRegPass)
        val etName = findViewById<EditText>(R.id.etRegName)
        val etId = findViewById<EditText>(R.id.etRegId)
        val etAge = findViewById<EditText>(R.id.etRegAge)
        val btnReg = findViewById<Button>(R.id.btnRegister)
        btnReg.setOnClickListener {
            val body = UserCreateRequest(
                email = etEmail.text.toString().trim(),
                password = etPass.text.toString(),
                full_name = etName.text.toString(),
                identification_number = etId.text.toString(),
                age = etAge.text.toString().toIntOrNull() ?: 18,
                role = "student")
            lifecycleScope.launch {
                try {
                    val resp = ApiClient.apiService.register(body)
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registrado: ${resp.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registro fallido: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }
}
