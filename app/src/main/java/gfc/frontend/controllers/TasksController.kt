package gfc.frontend.controllers

import android.content.Context
import gfc.frontend.dataclasses.ObjectBox
import gfc.frontend.dataclasses.RepeatableTask
import gfc.frontend.dataclasses.Task
import gfc.frontend.service.ReTasksService
import gfc.frontend.service.TasksService
import io.objectbox.Box
import kotlinx.coroutines.*
import kotlin.system.*

class TasksController(val context: Context?) {
    val url = "https://gamefication-for-children.herokuapp.com/tasks"
    val userId : Long = 0
    var taskBox: Box<Task> = ObjectBox.store.boxFor(Task::class.java)
    var reTaskBox: Box<RepeatableTask> = ObjectBox.store.boxFor(RepeatableTask::class.java)

    val reTaskService = ReTasksService(this.context)
    val taskService = TasksService(this.context)


    fun refreshTasks(type : String) {
        println("refresh task started")
        when (type) {
            "repeatable" -> {
                val result = reTaskService.getData("$url/allre/$userId")

                reTaskBox.removeAll()
                println("refresh task before put")
                reTaskBox.put(result)
            }
            "unrepeatable" -> {
                taskService.getData("$url/all/$userId")
                taskBox.removeAll()
                println("refresh task before await")
                taskBox.put(taskService.result)
            }
            else -> {
                println("Incorrect task type!")
            }
        }
        println("refresh task finished")
    }
}