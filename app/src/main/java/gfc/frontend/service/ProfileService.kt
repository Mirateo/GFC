package gfc.frontend.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import gfc.frontend.dataclasses.Task
import gfc.frontend.requests.AccountRequest
import kotlinx.coroutines.runBlocking

object ProfileService : KtorService() {

    lateinit var result: ArrayList<Task>

    class SimpleProfileServiceBinder(val servc: ProfileService): Binder() {
        fun getService(): ProfileService {
            return servc
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return SimpleProfileServiceBinder(this)
    }
}
