package gfc.frontend.service

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import gfc.frontend.dataclasses.RepeatableTask
import kotlinx.coroutines.runBlocking


class ReTasksService(context: Context?) : KtorService(context) {

    class SimpleReTasksServiceBinder(val servc: ReTasksService): Binder() {
        fun getService(): ReTasksService {
            return servc
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return SimpleReTasksServiceBinder(this)
    }


    fun getData(url: String) =  runBlocking<List<RepeatableTask>>  {
        ktorRequest <List<RepeatableTask>> ("GET", url, null)
        super.response as List<RepeatableTask>
    }

}
