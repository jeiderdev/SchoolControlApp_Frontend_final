package com.example.schoolcontrol.ui
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.schoolcontrol.R
import com.example.schoolcontrol.api.ApiClient
import com.example.schoolcontrol.models.ResetPasswordRequest
import kotlinx.coroutines.launch

class ResetPasswordActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        val etToken = findViewById<EditText>(R.id.etToken)
        val etNewPass = findViewById<EditText>(R.id.etNewPassword)
        val btnReset = findViewById<Button>(R.id.btnReset)
        // Prefill token if passed
        val preToken = intent.getStringExtra("token")
        if (!preToken.isNullOrEmpty()) etToken.setText(preToken)
        btnReset.setOnClickListener {
            val token = etToken.text.toString().trim()
            val newPass = etNewPass.text.toString()
            if (token.isEmpty() || newPass.isEmpty()) { Toast.makeText(this, "Completa token y nueva contraseña", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            lifecycleScope.launch {
                try {
                    val resp = ApiClient.apiService.resetPassword(ResetPasswordRequest(token, newPass))
                    Toast.makeText(this@ResetPasswordActivity, resp.message, Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@ResetPasswordActivity, "Error: ${'$'}{e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
