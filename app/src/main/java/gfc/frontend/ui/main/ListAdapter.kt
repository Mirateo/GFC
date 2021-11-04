package gfc.frontend.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import gfc.frontend.R
import gfc.frontend.dataclasses.Task
import gfc.frontend.service.TasksService

//import gfc.frontend.dataclasses.temporaryDatabase

class ListAdapter(section: Int?) :RecyclerView.Adapter<MyViewHolder>(){

    private val section = section
    private var elements: List<Any>? = null
    private lateinit var tasksService: TasksService

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listRow = layoutInflater.inflate(R.layout.recycler_view_item, parent, false)
        tasksService = TasksService(parent.context)

        refreshList()

        return MyViewHolder(listRow)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = holder.elementTitle
        val description = holder.elementDescription
        val done = holder.elementCheck

        refreshList()
    }

    override fun getItemCount(): Int {
        return if (this.elements == null) { 0 } else {
            this.elements!!.size
        }
    }

    private fun refreshList() {
        if (this.section == 1) {
            elements = tasksService.getAllUserTasks("unrepeatable")
        }
        else if(this.section == 2) {
            elements = tasksService.getAllUserTasks("repeatable")
        }
    }
}

class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){
    var elementTitle: TextView = itemView.findViewById(R.id.elementTitle)
    var elementDescription: TextView = itemView.findViewById(R.id.elementDescription)
    var elementCheck: TextView = itemView.findViewById(R.id.elementCheck)
}