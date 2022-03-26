package gfc.frontend.settings

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gfc.frontend.MainActivity
import gfc.frontend.R
import gfc.frontend.SettingsActivity
import gfc.frontend.controllers.AuthorizationController
import gfc.frontend.databinding.FragmentAccountSettingsBinding
import gfc.frontend.dataclasses.UserInfo
import gfc.frontend.requests.SigninRequest
import java.lang.IllegalArgumentException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AccountSettingsFragment : Fragment() {

    private var _binding: FragmentAccountSettingsBinding? = null

    private val binding get() = _binding!!

    private lateinit var userInfoPreferences: SharedPreferences
    private var userId: Long? = null
    private var realUsername: String? = null
    private var realEmail: String? = null
    private var role: String? = null
    private var friendlyName: String? = null
    private var realPasswd: String? = null

    private fun refreshRealData() {
        userInfoPreferences = requireContext().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
        userId = userInfoPreferences.getLong("id", 0)
        realUsername = userInfoPreferences.getString("username", "nope:(")
        realEmail = userInfoPreferences.getString("email", "nope:(")
        role = userInfoPreferences.getString("role", "nope:(")
        friendlyName = userInfoPreferences.getString("friendlyName", "nope:(")
        realPasswd = requireContext().getSharedPreferences("credentials", AppCompatActivity.MODE_PRIVATE).getString("password", "")

        loadInfos()
    }

    private fun loadInfos() {
        val textEmail : TextView = binding.email
        val textLogin : TextView = binding.login

        textLogin.text = "Login: $realUsername"
        textEmail.text = "E-mail: $realEmail"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(activity?.intent?.getBooleanExtra("family", false) == true) {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        super.onViewCreated(view, savedInstanceState)

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

            startActivity(Intent(context, MainActivity::class.java))
            activity?.finishAffinity()
        }

        binding.newEmailSave.setOnClickListener{
            val newEmail = binding.newEmail.text
            val password = binding.newEmailPass.text

            if(newEmail == null || password == null)  {
                binding.monit2.text = "Należy podać nowy email i hasło"
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val request: UserInfo
            try {
                request = UserInfo(userId!!, realUsername!!, newEmail.toString().trim(), realPasswd!!, role!!, friendlyName!!)
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
            binding.monit2.text = "E-mail zmieniony."
            binding.monit2.setBackgroundColor(Color.GREEN)
            binding.monit2.visibility = View.VISIBLE

            AuthorizationController.login(SigninRequest(realUsername!!, realPasswd!!))
            refreshRealData()

            startActivity(Intent(context, MainActivity::class.java))
            activity?.finishAffinity()
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

            startActivity(Intent(context, MainActivity::class.java))
            activity?.finishAffinity()
        }

        binding.accountHome.setOnClickListener {
            activity?.finish()
        }

        binding.familySettings.setOnClickListener {
            activity?.intent?.putExtra("family", true)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}