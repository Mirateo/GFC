package gfc.frontend.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import gfc.frontend.controllers.FamilyController
import gfc.frontend.databinding.FragmentFamilySettingsBinding
import android.content.Intent
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import gfc.frontend.*
import gfc.frontend.dataclasses.UserInfo


class FamilySettingsFragment : Fragment() {

    private var _binding: FragmentFamilySettingsBinding? = null
    private lateinit var familyList: ListView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamilySettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun updateFamilyList() {
        val family = ArrayList(FamilyController.getAll())

        familyList.adapter = ListViewAdapter(requireContext(), family)
        familyList.setOnItemClickListener { _: AdapterView<*>, _: View, i: Int, _: Long ->
            val intent = Intent(requireContext(), EditFamilySettings::class.java)
            intent.putExtra("id", family[i].id)
            intent.putExtra("username", family[i].username)
            intent.putExtra("email", family[i].email)
            intent.putExtra("password", family[i].password)
            intent.putExtra("role", family[i].role)
            intent.putExtra("friendlyName", family[i].friendlyName)
            startActivityForResult(intent, 1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(activity?.intent?.getBooleanExtra("family", false) == false) {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        super.onViewCreated(view, savedInstanceState)

        familyList = binding.familyList
        FamilyController.init(requireContext())

        updateFamilyList()

        binding.familyHome.setOnClickListener {
            activity?.finish()
        }
        binding.familyAdd.setOnClickListener {
            startActivityForResult(Intent(requireContext(), EditChildActivity::class.java), 1)
        }

        binding.profileSettings.setOnClickListener {
            activity?.intent?.putExtra("family", false)
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                updateFamilyList()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}