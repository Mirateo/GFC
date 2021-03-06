package gfc.frontend.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import gfc.frontend.R
import gfc.frontend.controllers.RewardsController
import gfc.frontend.controllers.TasksController
import gfc.frontend.databinding.FragmentMainBinding

class ToDosFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root
        binding.recyclerViewList.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerViewList.adapter = ToDosAdapter(arguments?.getInt(ARG_SECTION_NUMBER))
        refreshList()

        return root
    }

    private fun refreshList() {
        when (arguments?.getInt(ARG_SECTION_NUMBER)) {
            1 -> TasksController.tabChanged("unrepeatable")
            2 -> TasksController.tabChanged("repeatable")
            3 -> RewardsController.tabChanged()
            4 -> TasksController.tabChanged("done")
        }

        binding.recyclerViewList.adapter?.notifyDataSetChanged()
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): ToDosFragment {
            return ToDosFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}