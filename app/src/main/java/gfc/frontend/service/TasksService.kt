package gfc.frontend.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import gfc.frontend.dataclasses.Task
import org.json.JSONArray


class TasksService(val context: Context) : Service() {
    val url = "https://gamefication-for-children.herokuapp.com/tasks"
    val queue = Volley.newRequestQueue(context)
    val userId = 0

    class SimpleTasksServiceBinder(val servc: TasksService): Binder() {
        fun getService(): TasksService {
            return servc
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return SimpleTasksServiceBinder(this)
    }

    fun getAllUserTasks(type: String): List<Any>? {
        println("Ready")
        var response = JSONArray()
        queue.add(
            JsonArrayRequest(
                Request.Method.GET,
                "$url/all/$userId",
                null,
                { re -> response = JSONArray(re) }
            )
            { re -> println(re.toString())
                   println("Communication error:$re")
            }
        )

        return if (type == "unrepeteable") {
            Klaxon().parseArray<Task>(response.toString())
        } else null
    }
}