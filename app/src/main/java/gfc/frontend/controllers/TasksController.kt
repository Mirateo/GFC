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


    fun refreshTasks(type : String) = runBlocking<Unit> {
        when (type) {
            "repeatable" -> {
                val result =
                    withContext(Dispatchers.Default) {
                        reTaskService.getData("$url/allre/$userId")
                    }

                reTaskBox.removeAll()
                reTaskBox.put(result)
            }
            "unrepeatable" -> {
                val result =
                    withContext(Dispatchers.Default) {
                        taskService.getData("$url/all/$userId")
                    }

                taskBox.removeAll()
                taskBox.put(result)
            }
            else -> {
                println("Incorrect task type!")
            }
        }
    }
}