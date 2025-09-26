package com.example.schoolcontrol.ui
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.ForgotPasswordRequest
import com.example.schoolcontrol.models.ForgotResponse
import kotlinx.coroutines.launch

class ForgotPasswordActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val etEmail = findViewById<EditText>(R.id.etForgotEmail)
        val btnSend = findViewById<Button>(R.id.btnSendToken)
        btnSend.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isEmpty()) { Toast.makeText(this, "Ingresa un correo", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            lifecycleScope.launch {
                try {
                    val resp: ForgotResponse = ApiClient.apiService.forgotPassword(ForgotPasswordRequest(email))
                    Toast.makeText(this@ForgotPasswordActivity, "Token: ${'$'}{resp.token}", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                    intent.putExtra("token", resp.token)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@ForgotPasswordActivity, "Error: ${'$'}{e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
