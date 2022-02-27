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
        var temp: List<Any>?

        if(AuthorizationController.userIsParent) {
            when (type) {
                null -> {
                    temp = ReTasksService.getData("$url/fullRe")
                    if( temp != null){
                        reTasksContainer = ArrayList(temp)
                    }
                    temp = TasksService.getData("$url/full")
                    if( temp != null){
                        tasksContainer = ArrayList(temp)
                    }

                }
                "repeatable" -> {
                    temp = ReTasksService.getData("$url/fullRe")
                    if( temp != null){
                        reTasksContainer = ArrayList(temp)
                    }
                }
                "unrepeatable" -> {
                    temp = TasksService.getData("$url/full")
                    if( temp != null){
                        tasksContainer = ArrayList(temp)
                    }
                }
            }
        }
        else {
            when (type) {
                null -> {
                    temp = ReTasksService.getData("$url/allre")
                    if( temp != null){
                        reTasksContainer = ArrayList(temp)
                    }
                    temp = TasksService.getData("$url/all")
                    if( temp != null){
                        tasksContainer = ArrayList(temp)
                    }

                }
                "repeatable" -> {
                    temp = ReTasksService.getData("$url/allre")
                    if( temp != null){
                        reTasksContainer = ArrayList(temp)
                    }
                }
                "unrepeatable" -> {
                    temp = TasksService.getData("$url/all")
                    if( temp != null){
                        tasksContainer = ArrayList(temp)
                    }
                }
            }
        }

    }

    fun taskDone(task: Any): Long? {
        var ret: Long? = null
        when (task) {
            is Task -> {
                ret = TasksService.taskDone("$url/done/${task.id}")
                refreshTasks("unrepeatable")
            }
            is RepeatableTask -> {
                ret = ReTasksService.taskDone("$url/done/${task.id}")
                refreshTasks("repeatable")
            }
            else -> {
                println("Incorrect Task type")
            }
        }
        return ret
    }

    fun taskUndone(task: Any): Long? {
        var ret: Long? = null
        when (task) {
            is Task -> {
                ret = TasksService.taskUndone("$url/undone/${task.id}")
                refreshTasks("unrepeatable")
            }
            is RepeatableTask -> {
                ret = ReTasksService.taskUndone("$url/undone/${task.id}")
                refreshTasks("repeatable")
            }
            else -> {
                println("Incorrect Task type")
            }
        }
        return ret
    }

    fun deleteTask(id: Long): Long? {
        return TasksService.deleteTask("$url/remove/${id}")
    }
}