package gfc.frontend.service

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import gfc.frontend.dataclasses.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray


class TasksService(context: Context?) : VolleyService(context) {

    class SimpleTasksServiceBinder(val servc: TasksService): Binder() {
        fun getService(): TasksService {
            return servc
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return SimpleTasksServiceBinder(this)
    }

    fun toList(jsonArray: JSONArray?): List<Task>{
        val list: ArrayList<Task> = arrayListOf()

        if (jsonArray != null) {
            for (j in 0 until jsonArray.length()) {
                val user = Task(
                    jsonArray.getJSONObject(j).getLong("id"),
                    jsonArray.getJSONObject(j).getLong("ownerId"),
                    jsonArray.getJSONObject(j).getString("name"),
                    jsonArray.getJSONObject(j).getString("description"),
                    jsonArray.getJSONObject(j).getLong("points")
                )
                list.add(user)
            }
        }
        return list
    }

    suspend fun getData(url: String) =  runBlocking<List<Task>>  {
        withContext(Dispatchers.Default) {
            toList(volleyRequest("GET", url, null))
        }
    }

}