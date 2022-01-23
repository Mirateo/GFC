package gfc.frontend.controllers

import android.annotation.SuppressLint
import android.content.Context
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
//    var taskBox: Box<Task> = ObjectBox.store.boxFor(Task::class.java)
//    var reTaskBox: Box<RepeatableTask> = ObjectBox.store.boxFor(RepeatableTask::class.java)
//    lateinit var tasksContainer: ArrayList<Task>
//    lateinit var notifier: ToDosAdapter

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
//
//    fun addThisRef(toDosAdapter: ToDosAdapter) {
//        notifier = toDosAdapter
//    }

    fun refreshTasks(type : String?) {
        println("refresh task started")
        when (type) {
            null -> {
                reTasksContainer = ArrayList(ReTasksService.getData("$url/allre"))
                tasksContainer = ArrayList(TasksService.getData("$url/all"))
            }
            "repeatable" -> reTasksContainer = ArrayList(ReTasksService.getData("$url/allre"))
            "unrepeatable" -> tasksContainer = ArrayList(TasksService.getData("$url/all"))
            else -> println("Incorrect task type!")
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