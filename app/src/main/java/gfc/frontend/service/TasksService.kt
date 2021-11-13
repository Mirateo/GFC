package gfc.frontend.service

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import gfc.frontend.dataclasses.Task
import kotlinx.coroutines.*


class TasksService(context: Context?) : KtorService(context) {

    lateinit var result: ArrayList<Task>

    class SimpleTasksServiceBinder(val servc: TasksService): Binder() {
        fun getService(): TasksService {
            return servc
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return SimpleTasksServiceBinder(this)
    }

    fun getData(url: String) =  runBlocking<List<Task>>  {
        volleyRequest <List<Task>>("GET", url, null)
        super.response as List<Task>
    }
}