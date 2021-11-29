package gfc.frontend.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import gfc.frontend.R
import gfc.frontend.controllers.TasksController
import gfc.frontend.databinding.FragmentMainBinding

class ToDosFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var tasksController: TasksController

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
        this.tasksController = TasksController(this.context)
        binding.recyclerViewList.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerViewList.adapter = ToDosAdapter(arguments?.getInt(ARG_SECTION_NUMBER), tasksController)
        println("Ready-3")
        refreshList()
        return root
    }

    private fun refreshList() {
        println("Ready-1")
        if (arguments?.getInt(ARG_SECTION_NUMBER) == 1) {
            tasksController.refreshTasks("unrepeatable")
        }
        else if(arguments?.getInt(ARG_SECTION_NUMBER) == 2) {
            tasksController.refreshTasks("repeatable")
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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