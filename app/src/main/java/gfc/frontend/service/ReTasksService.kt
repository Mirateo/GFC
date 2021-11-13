package gfc.frontend.service

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import gfc.frontend.dataclasses.RepeatableTask
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import java.text.DateFormat


class ReTasksService(context: Context?) : KtorService(context) {

    class SimpleReTasksServiceBinder(val servc: ReTasksService): Binder() {
        fun getService(): ReTasksService {
            return servc
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return SimpleReTasksServiceBinder(this)
    }

    fun toReList(jsonArray: JSONArray?): List<RepeatableTask>{
        val list: ArrayList<RepeatableTask> = arrayListOf()

        if (jsonArray != null) {
            for (j in 0 until jsonArray.length()) {
                val user = RepeatableTask(
                    jsonArray.getJSONObject(j).getLong("id"),
                    jsonArray.getJSONObject(j).getLong("ownerId"),
                    jsonArray.getJSONObject(j).getString("name"),
                    jsonArray.getJSONObject(j).getString("description"),
                    jsonArray.getJSONObject(j).getLong("points"),
                    jsonArray.getJSONObject(j).getBoolean("doneToday"),
                    DateFormat.getDateInstance().parse(jsonArray.getJSONObject(j).getString("lastDone"))
                )
                list.add(user)
            }
        }
        return list
    }

    fun getData(url: String) =  runBlocking<List<RepeatableTask>>  {
        volleyRequest <List<RepeatableTask>> ("GET", url, null)
//        toReList(super.response)

        super.response as List<RepeatableTask>
    }

}
