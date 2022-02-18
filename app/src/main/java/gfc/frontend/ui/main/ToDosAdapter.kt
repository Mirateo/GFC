package gfc.frontend.ui.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import gfc.frontend.NewTaskActivity
import gfc.frontend.R
import gfc.frontend.controllers.TasksController
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat.startActivity

fun today(date: Date?): Date? {
    if(date == null) return null
    val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    return formatter.parse(formatter.format(date))
}

fun yesterday(date: Date?): Date? {
    if(date == null) return null

    val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    val today: Date = formatter.parse(formatter.format(date))

    return Date(today.time - 1000 * 60 * 60 * 24)
}

class ToDosAdapter(private val section: Int?) :RecyclerView.Adapter<MyViewHolder>(){
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val listRow = layoutInflater.inflate(R.layout.recycler_view_item, parent, false)

        return MyViewHolder(listRow)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = holder.elementTitle
        val description = holder.elementDescription
        val elementPoints = holder.elementPoints
        val done = holder.elementCheck


        when (section) {
            1 -> {
                val currentTask = TasksController.reTasksContainer[position]
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
                        TasksController.taskDone(currentTask)
                    }
                    else {
                        Snackbar.make(
                            view,
                            "Task " + currentTask.name + " is not done today anymore :(",
                            Snackbar.LENGTH_LONG
                        ).setAction("Action", null).show()
                        TasksController.taskUndone(currentTask)
                        notifyDataSetChanged()
                    }
                }
            }
            2 -> {
                val currentTask = TasksController.tasksContainer[position]
                name.text = currentTask.name
                description.text = currentTask.description
                elementPoints.text = "+${currentTask.points}"
                done.isChecked = false

                done.setOnClickListener { view ->
                    Snackbar.make(view, "Task " + currentTask.name +  " Done!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()

                    TasksController.taskDone(currentTask)
                    notifyDataSetChanged()

                }
            }
        }
        holder.view.setOnClickListener{
            val repeatable = (section == 1)

            val intent = Intent(context, NewTaskActivity::class.java)
            intent.putExtra("edit", true)
            intent.putExtra("name", name.text)
            intent.putExtra("description", description.text)
            intent.putExtra("points", elementPoints.text.subSequence(1, elementPoints.text.length))
            intent.putExtra("repeatable", repeatable)
            startActivity(context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return when (section) {
            1 -> TasksController.reTasksContainer.size
            2 -> TasksController.tasksContainer.size
            else -> 0
        }
    }

}

class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){
    var elementTitle: TextView = itemView.findViewById(R.id.elementTitle)
    var elementDescription: TextView = itemView.findViewById(R.id.elementDescription)
    var elementCheck: CheckBox = itemView.findViewById(R.id.elementCheck)
    var elementPoints: TextView = itemView.findViewById(R.id.userRole)
}
