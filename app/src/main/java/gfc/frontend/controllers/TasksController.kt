package gfc.frontend.controllers

import android.content.Context
import gfc.frontend.dataclasses.ObjectBox
import gfc.frontend.dataclasses.RepeatableTask
import gfc.frontend.dataclasses.Task
import gfc.frontend.dataclasses.TaskDTO
import gfc.frontend.service.ReTasksService
import gfc.frontend.service.TasksService
import gfc.frontend.ui.main.ToDosAdapter
import io.objectbox.Box

class TasksController(val context: Context?) {
    val url = "https://gamefication-for-children.herokuapp.com/tasks"
    val userId : Long = 0
    var taskBox: Box<Task> = ObjectBox.store.boxFor(Task::class.java)
    var reTaskBox: Box<RepeatableTask> = ObjectBox.store.boxFor(RepeatableTask::class.java)

    val reTaskService = ReTasksService(this.context)
    val taskService = TasksService(this.context)

    lateinit var notifier: ToDosAdapter

    fun refreshTasks(type : String?) {
        println("refresh task started")
        when (type) {
            null -> {
                val result = reTaskService.getData("$url/allre/$userId")
                reTaskBox.removeAll()
                println("refresh task before put")
                reTaskBox.put(result)

                val tmp = taskService.getData("$url/all/$userId")
                taskBox.removeAll()
                println("refresh task before await")
                println("!!!!! putted: $tmp")
                taskBox.put(tmp)

            }
            "repeatable" -> {
                val result = reTaskService.getData("$url/allre/$userId")

                reTaskBox.removeAll()
                reTaskBox.put(result)
            }
            "unrepeatable" -> {
                val tmp = taskService.getData("$url/all/$userId")
                taskBox.removeAll()
                taskBox.put(tmp)
            }
            else -> {
                println("Incorrect task type!")
            }
        }
        notifier.notifyDataSetChanged();
    }

    fun taskDone(task: Any) {
        when (task) {
            is Task -> {
                taskService.taskDone("$url/done/${task.id}")
                refreshTasks("unrepeatable")
            }
            is RepeatableTask -> {
                reTaskService.taskDone("$url/done/${task.id}")
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
                taskService.taskUndone("$url/undone/${task.id}")
                refreshTasks("unrepeatable")
            }
            is RepeatableTask -> {
                reTaskService.taskUndone("$url/undone/${task.id}")
                refreshTasks("repeatable")
            }
            else -> {
                println("Incorrect Task type")
            }
        }
    }

    fun addThisRef(toDosAdapter: ToDosAdapter): TasksController {
        notifier = toDosAdapter
        return this
    }

    fun addTask(name: String, description: String, points: Long, repeatable: Boolean) {
        taskService.addTask("$url/add", TaskDTO(this.userId, name, description, points, repeatable))
    }

}