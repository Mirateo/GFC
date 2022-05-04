package gfc.frontend.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import gfc.frontend.NewTaskActivity
import gfc.frontend.R
import gfc.frontend.controllers.TasksController
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat.startActivity
import gfc.frontend.MainActivity
import gfc.frontend.controllers.AuthorizationController
import gfc.frontend.controllers.FamilyController
import gfc.frontend.controllers.RewardsController

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
        val owner = holder.taskOwner
        val background = holder.view
        var ret: Long?

        when (section) {
            1 -> {
                val currentTask = TasksController.reTasksContainer[position]
                name.text = currentTask.name
                description.text = currentTask.description
                elementPoints.text = "+${currentTask.points}"
                if(AuthorizationController.userIsParent){
                    if(currentTask.own) {
                        owner.text = "prywatne"
                    }
                    else {
                        owner.text = FamilyController.getChildrenName(currentTask.ownerId)
                    }
                }
                else {
                    if(currentTask.own) {
                        owner.text = "prywatne"
                    }
                    else {
                        owner.text = "od rodzica"
                    }
                }
                val lastDone = currentTask.lastDone
                done.isChecked = (lastDone != null && lastDone >= today(Date()))

                done.setOnClickListener { view ->
                    var mess = "Gratulacje!"
                    ret = if(done.isChecked) {
                        TasksController.taskDone(currentTask)
                    } else {
                        mess = "Cofnięto status zadania."
                        TasksController.taskUndone(currentTask)
                    }
                    if (ret == null) {
                        Snackbar.make(view, "Status zadania nie został zmieniony. Błąd serwera.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                    else {
                        Snackbar.make(view, mess, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                        notifyDataSetChanged()
                        if(currentTask.own || !AuthorizationController.userIsParent) {
                            (context as MainActivity).notifyPointsUpdated(ret!!)
                        }
                    }
                }
            }
            2 -> {
                val currentTask = TasksController.tasksContainer[position]
                name.text = currentTask.name
                description.text = currentTask.description
                elementPoints.text = "+${currentTask.points}"
                if(AuthorizationController.userIsParent){
                    if(currentTask.own) {
                        owner.text = "prywatne"
                    }
                    else {
                        owner.text = FamilyController.getChildrenName(currentTask.ownerId)
                    }
                }
                else {
                    if(currentTask.own) {
                        owner.text = "prywatne"
                    }
                    else {
                        owner.text = "od rodzica"
                    }
                }
                done.isChecked = false

                done.setOnClickListener { view ->
                    ret = TasksController.taskDone(currentTask)
                    if (ret == null) {
                        Snackbar.make(view, "Status zadania nie został zmieniony. Błąd serwera.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                    else {
                        Snackbar.make(view, "Gratulacje, zadanie wykonane!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                        notifyDataSetChanged()

                        if(currentTask.own || !AuthorizationController.userIsParent) {
                            (context as MainActivity).notifyPointsUpdated(ret!!)
                        }
                    }
                }
            }
            3 -> {
                val currentReward = RewardsController.rewardsContainer[position]
                name.text = currentReward.title
                description.text = currentReward.description
                elementPoints.text = "${currentReward.points}"
                if(currentReward.owner == currentReward.reporter){
                    owner.text = "prywatne"
                }
                else {
                    if(AuthorizationController.userIsParent){
                        owner.text = FamilyController.getChildrenName(currentReward.owner.id)
                    } else {
                        owner.text = "od rodzica"
                    }
                }
                done.isChecked = currentReward.chosen
                if (currentReward.chosen) {
                    if(AuthorizationController.userIsParent){
                        elementPoints.text = "Zadanie wykonane! Zaakceptuj."
                    } else {
                        elementPoints.text = "Gratulacje! Wysłano do rodzica."
                    }
                    background.setBackgroundColor(Color.GREEN)
                }

                done.setOnClickListener { view ->
                    ret = if(AuthorizationController.userIsParent && !done.isChecked) {
                        Snackbar.make(view, "Zadanie zaakceptowane. Pora na nagrodę!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                        background.setBackgroundColor(Color.alpha(0))
                        RewardsController.accept(currentReward.rewardId)
                    } else if (currentReward.owner == currentReward.reporter && AuthorizationController.userIsParent) {
                        val preRet = RewardsController.accept(currentReward.rewardId)
                        if(preRet == -1L) {
                            done.isChecked = false
                            Snackbar.make(view, "Wybacz, nie masz wystarczającej liczby punktów.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                            return@setOnClickListener
                        } else {
                            Snackbar.make(view, "Gratulacje! Pora na nagrodę.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                            preRet
                        }
                    } else if (currentReward.owner == currentReward.reporter) {
                        Snackbar.make(view, "Gratulacje! Pora na nagrodę.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                        background.setBackgroundColor(Color.alpha(0))
                        RewardsController.accept(currentReward.rewardId)
                    } else if(done.isChecked) {
                        val preRet = RewardsController.select(currentReward.rewardId)
                        if(preRet == -1L) {
                            done.isChecked = false
                            Snackbar.make(view, "Wybacz, nie masz wystarczającej liczby punktów.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                            return@setOnClickListener
                        } else {
                            Snackbar.make(view, "Gratulacje! Wysłano prośbę do rodzica.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                            preRet
                        }
                    }
                    else {
                        Snackbar.make(view, "Rodzic powiadomiony.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                        background.setBackgroundColor(Color.alpha(0))
                        RewardsController.unselect(currentReward.rewardId)
                    }
                    if (ret == null) {
                        Snackbar.make(view, "Status zadania nie został zmieniony. Błąd serwera.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                    else {
                        notifyDataSetChanged()

                        if(currentReward.owner == currentReward.reporter || !AuthorizationController.userIsParent) {
                            (context as MainActivity).notifyPointsUpdated(ret!!)
                        }
                    }
                }
            }
            4 -> {
                val currentTask = TasksController.doneTasksContainer[position]
                name.text = currentTask.name
                description.text = currentTask.description
                elementPoints.text = "${currentTask.points}"
                done.isChecked = true
                done.isEnabled = false
                if(AuthorizationController.userIsParent){
                    if(currentTask.own) {
                        owner.text = "prywatne"
                    }
                    else {
                        owner.text = FamilyController.getChildrenName(currentTask.ownerId)
                    }
                }
                else {
                    if(currentTask.own) {
                        owner.text = "prywatne"
                    }
                    else {
                        owner.text = "od rodzica"
                    }
                }
            }
        }

        holder.view.setOnClickListener{ view ->
            if (section == 4) {
                return@setOnClickListener
            }

            if(!AuthorizationController.userIsParent && owner.text != "prywatne") {
                Snackbar.make(view, "Poproś rodzica o zmianę ustawień zadania/nagrody.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

                return@setOnClickListener
            }

            if (section == 3 ) {
                if (RewardsController.rewardsContainer[position].chosen && AuthorizationController.userIsParent) {
                    Snackbar.make(view, "Edycja wykonanego zadania nie jest możliwa.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    return@setOnClickListener
                }

                val intent = Intent(context, NewTaskActivity::class.java)
                intent.putExtra("edit", true)
                intent.putExtra("name", name.text)
                intent.putExtra("description", description.text)
                intent.putExtra("points", elementPoints.text.subSequence(0, elementPoints.text.length))
                intent.putExtra("selectedChild", owner.text)
                intent.putExtra("rewards", true)
                intent.putExtra("rewardId", RewardsController.rewardsContainer[position].rewardId)

                (context as Activity).startActivityForResult(intent, 0)
            }
            else {
                val repeatable = (section == 1)

                val intent = Intent(context, NewTaskActivity::class.java)
                intent.putExtra("edit", true)
                intent.putExtra("name", name.text)
                intent.putExtra("description", description.text)
                intent.putExtra("points", elementPoints.text.subSequence(1, elementPoints.text.length))
                intent.putExtra("repeatable", repeatable)
                intent.putExtra("selectedChild", owner.text)
                if(repeatable) {
                    intent.putExtra("taskId", TasksController.reTasksContainer[position].id)
                }
                else {
                    intent.putExtra("taskId", TasksController.tasksContainer[position].id)
                }
                (context as Activity).startActivityForResult(intent, 0)
            }
        }
    }


    override fun getItemCount(): Int {
        return when (section) {
            1 -> TasksController.reTasksContainer.size
            2 -> TasksController.tasksContainer.size
            3 -> RewardsController.rewardsContainer.size
            4 -> TasksController.doneTasksContainer.size
            else -> 0
        }
    }

}

class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){
    var elementTitle: TextView = itemView.findViewById(R.id.elementTitle)
    var elementDescription: TextView = itemView.findViewById(R.id.elementDescription)
    var elementCheck: CheckBox = itemView.findViewById(R.id.elementCheck)
    var elementPoints: TextView = itemView.findViewById(R.id.userRole)
    var taskOwner: TextView = itemView.findViewById(R.id.taskOwner)
}
