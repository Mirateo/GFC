package gfc.frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import gfc.frontend.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener{
            if(binding.userName.text.trim().isNotEmpty() &&
                binding.userPassword.text.trim().isNotEmpty()) {
                Toast.makeText(this, "Login i hasło ok", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                }
            else {
                Toast.makeText(this, "Login i hasło wymagane", Toast.LENGTH_LONG).show()
            }
        }

        binding.registerButton.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}