package gfc.frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import gfc.frontend.controllers.AuthorizationController
import gfc.frontend.databinding.ActivityRegisterBinding
import gfc.frontend.requests.SignupRequest
import gfc.frontend.service.AuthorizationService
import java.lang.IllegalArgumentException

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authController: AuthorizationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authController = AuthorizationController(this)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener{
            val username = binding.userName.text.trim().toString()
            val password = binding.userPassword.text.trim().toString()
            val password2 = binding.userPassword2.text.trim().toString()
            val email = binding.userMail.text.trim().toString()

            if(password != password2) {
                binding.monit.text = getString(R.string.passwordsIncorrect)
                binding.monit.visibility = View.VISIBLE
                return@setOnClickListener
            }

            try {
                SignupRequest(username, password, "PARENT", email)
            } catch (e: IllegalArgumentException) {
                binding.monit.text = e.message
                binding.monit.visibility = View.VISIBLE
                return@setOnClickListener
            }

            Toast.makeText(this, authController.registerParent(username, email, password), Toast.LENGTH_LONG).show()
            finish()
        }

        binding.loginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}