package gfc.frontend.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.internal.ContextUtils.getActivity
import gfc.frontend.LoginActivity
import gfc.frontend.MainActivity
import gfc.frontend.dataclasses.DoneTask
import gfc.frontend.dataclasses.ObjectBox
import gfc.frontend.dataclasses.RepeatableTask
import gfc.frontend.dataclasses.Task
import gfc.frontend.requests.TaskDTO
import gfc.frontend.service.AuthorizationService
import gfc.frontend.service.ReTasksService
import gfc.frontend.service.TasksService
import gfc.frontend.ui.main.ToDosAdapter
import io.objectbox.Box
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


fun minuteAgo(): Long {
    return Date().time - 60000
}

object TasksController {
    lateinit var context: Context
    var userId = -1L
    val url = "https://gamefication-for-children.herokuapp.com/tasks"
    lateinit var tasksContainer: ArrayList<Task>
    lateinit var reTasksContainer: ArrayList<RepeatableTask>
    lateinit var doneTasksContainer:  ArrayList<DoneTask>
    lateinit var timestamp: Array<Date?>

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

    fun init(context: Context) {
        this.context = context
        userId = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getLong("id", 0)
        ReTasksService.init(context)
        TasksService.init(context)
        timestamp = Array(3, {_ -> null} )
    }

    fun tabChanged(type: String?) {
        if (timestamp[0] == null || timestamp[1] == null || timestamp[2] == null ) {
            refreshTasks(type)
            return
        }
        when (type) {
            null -> {
                if(timestamp[0]?.time!! < minuteAgo() || timestamp[1]?.time!! < minuteAgo() || timestamp[2]?.time!! < minuteAgo()) {
                    refreshTasks(type)
                }
            }
            "repeatable" -> {
                if(timestamp[0]?.time!! < minuteAgo()) {
                    refreshTasks(type)
                }
            }
            "unrepeatable" -> {
                if(timestamp[1]?.time!! < minuteAgo() ) {
                    refreshTasks(type)
                }
            }
            "done" -> {
                if(timestamp[2]?.time!! < minuteAgo()) {
                    refreshTasks(type)
                }
            }
        }
    }

    fun updateTimestamps(type: String?) {
        when (type) {
            null -> {
                timestamp[0] = Date()
                timestamp[1] = Date()
                timestamp[2] = Date()
            }
            "repeatable" -> {
                timestamp[0] = Date()
            }
            "unrepeatable" -> {
                timestamp[1] = Date()
            }
            "done" -> {
                timestamp[2] = Date()
            }
        }
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
                    temp = TasksService.getDoneTasks("$url/fullDone")
                    if( temp != null){
                        doneTasksContainer = ArrayList(temp)
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
                "done" -> {
                    temp = TasksService.getDoneTasks("$url/fullDone")
                    if( temp != null){
                        doneTasksContainer = ArrayList(temp)
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
                    temp = TasksService.getDoneTasks("$url/allDone")
                    if( temp != null){
                        doneTasksContainer = ArrayList(temp)
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
                "done" -> {
                    temp = TasksService.getDoneTasks("$url/allDone")
                    if( temp != null){
                        doneTasksContainer = ArrayList(temp)
                    }
                }
            }
        }
        updateTimestamps(type)
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


    fun deleteTask(id: Long): Long? {
        return TasksService.deleteTask("$url/remove/${id}")
    }

    fun editTask(task: Task, repeatable: Boolean): Long? {
        if (repeatable) {
            return ReTasksService.editTask("$url/editRe", RepeatableTask(task))
        }
        return TasksService.editTask("$url/edit", task)
    }

}