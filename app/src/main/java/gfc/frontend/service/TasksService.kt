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
import android.widget.Toast
import gfc.frontend.dataclasses.RepeatableTask
import org.json.JSONException
import org.json.JSONObject

class TasksService(val context: Context?) : Service() {
    val url = "https://gamefication-for-children.herokuapp.com/tasks"
    val queue = Volley.newRequestQueue(context)
    val userId = 0
    lateinit var tasks: List<Task>
    lateinit var reTasks: List<RepeatableTask>

    class SimpleTasksServiceBinder(val servc: TasksService): Binder() {
        fun getService(): TasksService {
            return servc
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return SimpleTasksServiceBinder(this)
    }

    fun volleyRequest(callBack: VolleyCallBack?, method: String, url: String, jsonArgs: JSONArray?) {
//        val jsonObjectRequest = JsonArrayRequest(
//            when (method){
//                "GET" -> Request.Method.GET
//                "POST" -> Request.Method.POST
//                else -> Request.Method.PUT
//            },
//            url,
//            jsonArgs,
//            { response ->
//                try {
//                    val tmp = Klaxon().parseArray<Task>(result.toString())
//                    println("tmp: " + tmp)
//                    val tasks = Klaxon().parseArray<Task>(result.toString())
//                    this.response = response
//                    callBack?.onSuccess()
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            },
//            {
//                Toast.makeText( context, "Problem z połączeniem.", Toast.LENGTH_LONG ).show()
//            })
//        queue.add(jsonObjectRequest)
    }


    fun getAllUserTasks(type: String): List<Any>? {
        println("Ready json")
        val fUrl = url + when (type) {
            "repeatable" -> "/all/" + userId
            "unrepeatable" -> "/all/" + userId
            else -> {
                println("Incorrect task type")
                return null
            }
        }
        volleyRequest(
            object : VolleyCallBack {
                override fun onSuccess() {
//                    val tmp = Klaxon().parseArray<Task>(queue.toString())
//                    println("tmp: " + tmp)
//                    val tasks = Klaxon().parseArray<Task>(result.toString())
                }
            },
            "GET",
            fUrl,
            null
        )
        return null
    }
}