package gfc.frontend

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import gfc.frontend.controllers.AuthorizationController
import gfc.frontend.databinding.ActivityEditChildBinding
import gfc.frontend.databinding.ActivityEditFamilySettingsBinding
import gfc.frontend.databinding.FragmentAccountSettingsBinding
import gfc.frontend.dataclasses.UserInfo
import gfc.frontend.requests.SigninRequest
import java.lang.IllegalArgumentException

class EditFamilySettings : AppCompatActivity() {
    private lateinit var binding: ActivityEditFamilySettingsBinding

    private lateinit var userInfoPreferences: SharedPreferences
    private var userId: Long? = null
    private var realUsername: String? = null
    private var realEmail: String? = null
    private var role: String? = null
    private var friendlyName: String? = null
    private var realPasswd: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditFamilySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        refreshRealData()

        binding.newLoginSave.setOnClickListener{
            val newLogin = binding.newLogin.text
            val password = binding.newLoginPass.text

            if(newLogin == null || password == null)  {
                binding.monit2.setBackgroundColor(Color.RED)
                binding.monit2.text = "Należy podać nowy login i hasło"
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if(password.toString().trim() != realPasswd)  {
                binding.monit2.setBackgroundColor(Color.RED)
                binding.monit2.text = "Hasło niepoprawne"
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val request: UserInfo
            try {
                request = UserInfo(userId!!, newLogin.toString().trim(), realEmail!!, realPasswd!!, role!!, friendlyName!!)
            } catch (e: IllegalArgumentException) {
                binding.monit2.setBackgroundColor(Color.RED)
                binding.monit2.text = e.message
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if( AuthorizationController.editProfile(request) == null ) {
                binding.monit2.setBackgroundColor(Color.RED)
                binding.monit2.text = "Zmiana nazwy użytkownika nie powiodła się. Spróbuj ponownie później."
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }
            binding.monit2.text = "Nazwa użytkownika zmieniona."
            binding.monit2.setBackgroundColor(Color.GREEN)
            binding.monit2.visibility = View.VISIBLE

            AuthorizationController.login(SigninRequest(newLogin.toString().trim(), realPasswd!!))
            refreshRealData()

            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }

        binding.newFnSave.setOnClickListener{
            val newFN = binding.newEmail.text
            val password = binding.newFnPass.text

            if(newFN == null || password == null)  {
                binding.monit2.text = "Należy podać nowy email i hasło"
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val request: UserInfo
            try {
                request = UserInfo(userId!!, realUsername!!, realEmail!!, realPasswd!!, role!!, newFN.trim().toString())
            } catch (e: IllegalArgumentException) {
                binding.monit2.setBackgroundColor(Color.RED)
                binding.monit2.text = e.message
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }


            if( AuthorizationController.editProfile(request) == null ) {
                binding.monit2.setBackgroundColor(Color.RED)
                binding.monit2.text = "Zmiana nazwy użytkownika nie powiodła się. Spróbuj ponownie później."
                binding.monit2.visibility = View.VISIBLE

                return@setOnClickListener
            }
            binding.monit2.text = "Przyjazna nazwa zmieniona."
            binding.monit2.setBackgroundColor(Color.GREEN)
            binding.monit2.visibility = View.VISIBLE

            refreshRealData()

            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }


        binding.newPassSave.setOnClickListener {
            val password = binding.newPass.text
            val newPass1 = binding.newPassOldOne1.text
            val newPass2 = binding.newPassOldOne2.text

            if (password == null || newPass1 == null || newPass2 == null) {
                binding.monit2.text = "Należy podać stare i nowe hasło"
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if  (newPass1.toString() !=  newPass2.toString()) {
                binding.monit2.text = "Nowe hasła nie są takie same"
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (newPass1.length < 6) {
                binding.monit2.setBackgroundColor(Color.RED)
                binding.monit2.text = "Hasło musi składać się z co najmniej 6 znaków"
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (newPass1.length >= 40) {
                binding.monit2.setBackgroundColor(Color.RED)
                binding.monit2.text = "Hasło może się składać maksymalnie z 40 znaków"
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val request: UserInfo
            try {
                request = UserInfo(userId!!, realUsername!!, realEmail!!, newPass1.toString().trim(), role!!, friendlyName!!)
            } catch (e: IllegalArgumentException) {
                binding.monit2.setBackgroundColor(Color.RED)
                binding.monit2.text = e.message
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if( AuthorizationController.editProfile(request) == null ) {
                binding.monit2.setBackgroundColor(Color.RED)
                binding.monit2.text = "Zmiana hasła nie powiodła się. Spróbuj ponownie później."
                binding.monit2.visibility = View.VISIBLE

                return@setOnClickListener
            }
            binding.monit2.text = "Hasło zmienione."
            binding.monit2.setBackgroundColor(Color.GREEN)
            binding.monit2.visibility = View.VISIBLE

            AuthorizationController.login(SigninRequest(realUsername!!, newPass1.toString()))
            refreshRealData()

            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }

        binding.accountHome.setOnClickListener {
            finish()
        }
    }

    private fun refreshRealData() {
        userId = intent.getLongExtra("id", 0L)
        realUsername = intent.getStringExtra("username")
        realEmail = intent.getStringExtra("email")
        role = intent.getStringExtra("role")
        friendlyName = intent.getStringExtra("friendlyName")
        realPasswd = intent.getStringExtra("password")

        loadInfos()
    }

    private fun loadInfos() {
        val textLogin : TextView = binding.login
        val textFriendlyName : TextView = binding.friendlyName

        textLogin.text = "Login: $realUsername"
        textFriendlyName.text = "Przyjazna nazwa: $friendlyName"
    }
}



