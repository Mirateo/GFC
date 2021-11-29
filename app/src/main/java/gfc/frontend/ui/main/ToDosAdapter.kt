package gfc.frontend.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import gfc.frontend.R
import gfc.frontend.controllers.TasksController
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


public fun today(date: Date?): Date? {
    if(date == null) return null
    val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    return formatter.parse(formatter.format(date))
}

public fun yesterday(date: Date?): Date? {
    if(date == null) return null

    val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    val today: Date = formatter.parse(formatter.format(date))

    return Date(today.time - 1000 * 60 * 60 * 24)
}

class ToDosAdapter(private val section: Int?, tasksController: TasksController) :RecyclerView.Adapter<MyViewHolder>(){
    private val tasksController = tasksController.addThisRef(this)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listRow = layoutInflater.inflate(R.layout.recycler_view_item, parent, false)

        return MyViewHolder(listRow)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = holder.elementTitle
        val description = holder.elementDescription
        val elementPoints = holder.elementPoints
        val done = holder.elementCheck

        when (section) {
            1 -> {
                val currentTask = tasksController.reTaskBox.all[position]
                name.text = currentTask.name
                description.text = currentTask.description
                elementPoints.text = "+${currentTask.points}"
                val lastDone = currentTask.lastDone
                done.isChecked = lastDone != null && lastDone >= today(Date())
                println("Last done: " + lastDone + "\n today is: " + today(Date()))

                done.setOnClickListener { view ->
                if(done.isChecked) {
                    Snackbar.make(
                        view,
                        "Task " + currentTask.name + " Done!",
                        Snackbar.LENGTH_LONG
                    ).setAction("Action", null).show()
                    tasksController.taskDone(currentTask)
                }
                else {
                    Snackbar.make(
                        view,
                        "Task " + currentTask.name + " is not done today anymore :(",
                        Snackbar.LENGTH_LONG
                    ).setAction("Action", null).show()
                    tasksController.taskUndone(currentTask)
                }
                }

            }
            2 -> {
                val currentTask = tasksController.taskBox.all[position]
                name.text = currentTask.name
                description.text = currentTask.description
                elementPoints.text = "+${currentTask.points}"
                done.isChecked = false

                done.setOnClickListener { view ->
                    Snackbar.make(view, "Task " + currentTask.name +  " Done!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()

                tasksController.taskDone(currentTask)

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return when (section) {
            1 -> this.tasksController.reTaskBox.count().toInt()
            2 -> this.tasksController.taskBox.count().toInt()
            else -> 0
        }
    }

}

class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){
    var elementTitle: TextView = itemView.findViewById(R.id.elementTitle)
    var elementDescription: TextView = itemView.findViewById(R.id.elementDescription)
    var elementCheck: CheckBox = itemView.findViewById(R.id.elementCheck)
    var elementPoints: TextView = itemView.findViewById(R.id.pointsAmount)
}
