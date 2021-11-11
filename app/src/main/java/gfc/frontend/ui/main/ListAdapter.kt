package gfc.frontend.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import gfc.frontend.R
import gfc.frontend.controllers.TasksController
import gfc.frontend.dataclasses.Task
import gfc.frontend.service.TasksService
import io.objectbox.Box

//import gfc.frontend.dataclasses.temporaryDatabase

class ListAdapter(section: Int?, tasksController: TasksController) :RecyclerView.Adapter<MyViewHolder>(){
    private val tasksController = tasksController
    private val section = section

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listRow = layoutInflater.inflate(R.layout.recycler_view_item, parent, false)

        return MyViewHolder(listRow)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = holder.elementTitle
        val description = holder.elementDescription
        val done = holder.elementCheck
    }

    override fun getItemCount(): Int {
        return when (section) {
            0 -> this.tasksController.taskBox.count().toInt()
            1 -> this.tasksController.reTaskBox.count().toInt()
            else -> 1
        }
    }

}

class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){
    var elementTitle: TextView = itemView.findViewById(R.id.elementTitle)
    var elementDescription: TextView = itemView.findViewById(R.id.elementDescription)
    var elementCheck: TextView = itemView.findViewById(R.id.elementCheck)
}