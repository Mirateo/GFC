package gfc.frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import gfc.frontend.controllers.AuthorizationController
import gfc.frontend.databinding.ActivityRegisterBinding
import gfc.frontend.requests.SignupRequest
import java.lang.IllegalArgumentException

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AuthorizationController.init(applicationContext)

        binding.registerButton.setOnClickListener{
            val username = binding.userName.text.trim().toString()
            val password = binding.userPassword.text.trim().toString()
            val password2 = binding.userPassword2.text.trim().toString()
            val email = binding.userMail.text.toString()

            if(password != password2) {
                binding.monit.text = getString(R.string.passwordsIncorrect)
                binding.monit.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val request: SignupRequest
            try {
                request = SignupRequest(username, email, "PARENT", password)
            } catch (e: IllegalArgumentException) {
                binding.monit.text = e.message
                binding.monit.visibility = View.VISIBLE
                return@setOnClickListener
            }

            Toast.makeText(this, AuthorizationController.registerParent(request), Toast.LENGTH_LONG).show()
            finish()
        }

        binding.loginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}