package gfc.frontend.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import gfc.frontend.controllers.TasksController
import gfc.frontend.dataclasses.UserInfo
import gfc.frontend.requests.SignupChildRequest
import kotlinx.coroutines.runBlocking

object FamilyService :KtorService() {
    lateinit var result: String
    
    class SimpleAuthorizationServiceBinder(val servc: FamilyService) : Binder() {
        fun getService(): FamilyService {
            return servc
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return SimpleAuthorizationServiceBinder(this)
    }

    fun getAll(url: String) = runBlocking<List<UserInfo>> {
        ktorRequest<List<UserInfo>>("GET", url, null)
        super.response as List<UserInfo>
    }

    fun addChild(url: String, signupChildRequest: SignupChildRequest) = runBlocking<String?> {
        ktorRequest<String?>("POST", url, signupChildRequest)
        super.response as String?
    }


    fun delChild(url: String) = runBlocking<String?> {
        ktorRequest<String?>("POST", url, null)
        super.response as String?
    }

    fun editChild(url: String, user: UserInfo) = runBlocking<String?> {
        ktorRequest<String?>("POST", url, user)
        super.response as String?
    }

}
