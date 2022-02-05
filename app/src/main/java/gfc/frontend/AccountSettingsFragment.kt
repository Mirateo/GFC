package gfc.frontend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import gfc.frontend.databinding.FragmentAccountSettingsBinding
import gfc.frontend.requests.SignupRequest
import java.lang.IllegalArgumentException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AccountSettingsFragment : Fragment() {

    private var _binding: FragmentAccountSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = requireContext().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("username", "nope:(").toString()
        val email = requireContext().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("email", "nope:(").toString()
        val role = requireContext().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("role", "nope:(").toString()
        val friendlyName = requireContext().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("friendlyName", "nope:(").toString()

        val textEmail : TextView = binding.email
        val textLogin : TextView = binding.login

        textLogin.text = "Login: $username"
        textEmail.text = "E-mail: $email"

        binding.newLoginSave.setOnClickListener{
            val newLogin = binding.newLogin.text
            val password = binding.newLoginPass.text

            if(newLogin == null || password == null)  {
                binding.monit2.text = "Należy podać nowy login i hasło"
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val request: SignupRequest
            try {
                request = SignupRequest(username.trim(), email, "PARENT", password.toString())
            } catch (e: IllegalArgumentException) {
                binding.monit2.text = e.message
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }
            binding.monit2.visibility = View.INVISIBLE

            println("Edit: " + request.toString())
//            Toast.makeText(this, AuthorizationController.registerParent(request), Toast.LENGTH_LONG).show()
        }

        binding.newEmailSave.setOnClickListener{
            val newEmail = binding.newEmail.text
            val password = binding.newEmailPass.text

            if(newEmail == null || password == null)  {
                binding.monit2.text = "Należy podać nowy email i hasło"
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val request: SignupRequest
            try {
                request = SignupRequest(username, newEmail.trim().toString(), "PARENT", password.toString())
            } catch (e: IllegalArgumentException) {
                binding.monit2.text = e.message
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }
            binding.monit2.visibility = View.INVISIBLE

            println("Edit: " + request.toString())
//            Toast.makeText(this, AuthorizationController.registerParent(request), Toast.LENGTH_LONG).show()
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
            if  (newPass1 !=  newPass2) {
                binding.monit2.text = "Nowe hasła nie są takie same"
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }
            val request: SignupRequest
            try {
                request = SignupRequest(
                    username, email, "PARENT", newPass1.toString()
                )
            } catch (e: IllegalArgumentException) {
                binding.monit2.text = e.message
                binding.monit2.visibility = View.VISIBLE
                return@setOnClickListener
            }
            binding.monit2.visibility = View.INVISIBLE

            println("Edit: " + request.toString())

//            Toast.makeText(this, AuthorizationController.registerParent(request), Toast.LENGTH_LONG).show()
        }

        binding.accountHome.setOnClickListener {
            activity?.finish()
        }

        binding.familySettings.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}