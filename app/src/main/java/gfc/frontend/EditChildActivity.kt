package gfc.frontend

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import gfc.frontend.controllers.AuthorizationController
import gfc.frontend.controllers.FamilyController
import gfc.frontend.controllers.TasksController
import gfc.frontend.databinding.ActivityEditChildBinding
import gfc.frontend.databinding.ActivityNewTaskBinding
import gfc.frontend.dataclasses.UserInfo
import java.lang.IllegalArgumentException

class EditChildActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditChildBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_child)
        FamilyController.init(applicationContext)

        binding = ActivityEditChildBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getBooleanExtra("edit", false)) {
            val id = intent.getLongExtra("id", 0L)
            val email = intent.getStringExtra("email")
            val role = intent.getStringExtra("role")

            binding.userName.setText(intent.getStringExtra("username"))
            binding.userFN.setText(intent.getStringExtra("friendlyName"))
            binding.delButton.isVisible = true
            binding.delButton.isClickable = true

            binding.delButton.setOnClickListener{
                val resp = FamilyController.delChild(id)
                if( resp == null ) {
                    binding.monit.setBackgroundColor(Color.RED)
                    binding.monit.text = "Dodawanie użytkownika nie powiodło się."
                    binding.monit.visibility = View.VISIBLE

                    return@setOnClickListener
                }
                Toast.makeText(applicationContext, resp, Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, Intent())
                finish()
            }

//            binding.acceptButton.setOnClickListener{
//                val username = binding.userName.text.trim().toString()
//                val friendlyName = binding.userFN.text.trim().toString()
//                val pass1 = binding.userPassword.text.trim().toString()
//                val pass2 = binding.userPassword2.text.trim().toString()
//
//                if(pass1 != pass2) {
//                    binding.monit.setBackgroundColor(Color.RED)
//                    binding.monit.text = "Hasła nie są identyczne."
//                    binding.monit.visibility = View.VISIBLE
//
//                    return@setOnClickListener
//                }
//
//                try {
//                    val user = UserInfo(id, username, email.toString(), pass1, role.toString(), friendlyName)
//                    val resp = FamilyController.editChild(user)
//                    if( resp == null ) {
//                        binding.monit.setBackgroundColor(Color.RED)
//                        binding.monit.text = "Edycja użytkownika nie powiodła się."
//                        binding.monit.visibility = View.VISIBLE
//
//                        return@setOnClickListener
//                    }
//
//                    Toast.makeText(applicationContext, resp, Toast.LENGTH_SHORT).show()
//                    setResult(RESULT_OK, Intent())
//                    finish()
//                } catch (e: IllegalArgumentException) {
//                    binding.monit.setBackgroundColor(Color.RED)
//                    binding.monit.text = e.message
//                    binding.monit.visibility = View.VISIBLE
//                    return@setOnClickListener
//                }
//            }
        }
        binding.acceptButton.setOnClickListener{
            val username = binding.userName.text.trim().toString()
            val friendlyName = binding.userFN.text.trim().toString()
            val pass1 = binding.userPassword.text.trim().toString()
            val pass2 = binding.userPassword2.text.trim().toString()

            if(pass1 != pass2) {
                binding.monit.setBackgroundColor(Color.RED)
                binding.monit.text = "Hasła nie są identyczne."
                binding.monit.visibility = View.VISIBLE

                return@setOnClickListener
            }

            try {
                val resp = FamilyController.addChild(username, friendlyName, pass1)
                if( resp == null ) {
                    binding.monit.setBackgroundColor(Color.RED)
                    binding.monit.text = "Dodawanie użytkownika nie powiodło się."
                    binding.monit.visibility = View.VISIBLE

                    return@setOnClickListener
                }
                Toast.makeText(applicationContext, resp, Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, Intent())
                finish()
            } catch (e: IllegalArgumentException) {
                binding.monit.setBackgroundColor(Color.RED)
                binding.monit.text = e.message
                binding.monit.visibility = View.VISIBLE
                return@setOnClickListener
            }
        }

        binding.cancelButton.setOnClickListener { view ->
            finish()
        }
    }

}