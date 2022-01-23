package gfc.frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import gfc.frontend.controllers.AuthorizationController
import gfc.frontend.databinding.ActivityLoginBinding
import gfc.frontend.requests.SigninRequest
import gfc.frontend.service.AuthorizationService
import java.lang.IllegalArgumentException


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AuthorizationController.init(applicationContext)
        applicationContext.getSharedPreferences("credentials", MODE_PRIVATE)

        binding.loginButton.setOnClickListener{
            val username = binding.userName.text.trim().toString()
            val password = binding.userPassword.text.trim().toString()
            val request: SigninRequest

            try {
                request = SigninRequest(username, password)
            } catch (e: IllegalArgumentException) {
                binding.monit.text = e.message
                binding.monit.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if(AuthorizationController.login(request)){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else {
                Toast.makeText(this, "Logowanie nie powiodło się. Sprawdź login i hasło i spróbuj ponownie", Toast.LENGTH_LONG).show()
            }

        }

        binding.registerButton.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}