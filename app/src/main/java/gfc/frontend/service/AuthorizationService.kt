package gfc.frontend.service

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import gfc.frontend.dataclasses.Task
import gfc.frontend.requests.SignupRequest
import kotlinx.coroutines.runBlocking

class AuthorizationService(context: Context?): KtorService(context) {
    lateinit var result: String

    class SimpleAuthorizationServiceBinder(val servc: AuthorizationService) : Binder() {
        fun getService(): AuthorizationService {
            return servc
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return SimpleAuthorizationServiceBinder(this)
    }

    fun registerParent(url: String, request: SignupRequest) = runBlocking<String> {
        println("!!! $request")
        ktorRequest<String>("POST", url, request)
        super.response as String
    }
}
