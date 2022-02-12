package gfc.frontend.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.internal.ContextUtils.getActivity
import gfc.frontend.LoginActivity
import gfc.frontend.MainActivity
import gfc.frontend.dataclasses.ObjectBox
import gfc.frontend.dataclasses.RepeatableTask
import gfc.frontend.dataclasses.Task
import gfc.frontend.requests.TaskDTO
import gfc.frontend.service.AuthorizationService
import gfc.frontend.service.ReTasksService
import gfc.frontend.service.TasksService
import gfc.frontend.ui.main.ToDosAdapter
import io.objectbox.Box
import kotlin.properties.Delegates

object TasksController {
    lateinit var context: Context
    var userId = -1L
    val url = "https://gamefication-for-children.herokuapp.com/tasks"
    var tasksContainer by Delegates.observable(ArrayList<Task>()) { _, _, _ ->
//        notifier.notifyDataSetChanged()
    }
    var reTasksContainer by Delegates.observable(ArrayList<RepeatableTask>()) { _, _, _ ->
//        notifier.notifyDataSetChanged()
    }

    fun init(context: Context) {
        this.context = context
        userId = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getLong("id", 0)
        ReTasksService.init(context)
        TasksService.init(context)
    }

    fun refreshTasks(type : String?) {
        when (type) {
            null -> {
                reTasksContainer = ArrayList(ReTasksService.getData("$url/allre"))
                tasksContainer = ArrayList(TasksService.getData("$url/all"))
            }
            "repeatable" -> reTasksContainer = ArrayList(ReTasksService.getData("$url/allre"))
            "unrepeatable" -> tasksContainer = ArrayList(TasksService.getData("$url/all"))
        }
    }

    fun taskDone(task: Any) {
        when (task) {
            is Task -> {
                TasksService.taskDone("$url/done/${task.id}")
                refreshTasks("unrepeatable")
            }
            is RepeatableTask -> {
                ReTasksService.taskDone("$url/done/${task.id}")
                refreshTasks("repeatable")
            }
            else -> {
                println("Incorrect Task type")
            }
        }
    }

    fun taskUndone(task: Any) {
        when (task) {
            is Task -> {
                TasksService.taskUndone("$url/undone/${task.id}")
                refreshTasks("unrepeatable")
            }
            is RepeatableTask -> {
                ReTasksService.taskUndone("$url/undone/${task.id}")
                refreshTasks("repeatable")
            }
            else -> {
                println("Incorrect Task type")
            }
        }
    }

    fun addTask(name: String, description: String, points: Long, repeatable: Boolean) {
        val ret = TasksService.addTask("$url/add", TaskDTO(ownerId = this.userId, name = name, description = description, points = points, repeatable = repeatable))
        if(ret >= 0) {
            if( repeatable == false) {
                tasksContainer.add(Task(id = ret, ownerId = userId, name = name, description = description, points = points))
            }
            else {
                reTasksContainer.add(RepeatableTask(id = ret, ownerId = userId, name = name, description = description, points = points, lastDone = null))
            }
        }
    }
}