package gfc.frontend.service

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import gfc.frontend.dataclasses.UserInfo
import gfc.frontend.requests.SigninRequest
import gfc.frontend.requests.SignupRequest
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    private fun saveToken (resp: Any) {
        val httpResponse = (resp as HttpResponse)
        if (httpResponse.status.isSuccess()){
            super.response = httpResponse.headers["Authorization"]
        }
        else {
            super.response = null
        }
    }

    fun registerParent(url: String, request: SignupRequest) = runBlocking<String> {
        ktorAnonymousRequest<String>(url, request)
        super.response as String
    }

    fun login(loginUrl: String, credentials: SigninRequest) = runBlocking<String?> {
        loginRequest<Any>(loginUrl, credentials)
        super.response as String?
    }

    private suspend inline fun <reified T: Any> loginRequest(url: String, credentials: SigninRequest)  = coroutineScope<Unit> {
        val httpClient = super.anonymousHttpClient

        withContext(Dispatchers.Default) {
            httpClient.post<T>(url) {
                body = credentials
                contentType(ContentType.Application.Json)
            }
        }.apply {
            saveToken(this)
        }

        httpClient.close()
    }

    fun getUserInfo(url: String) = runBlocking<UserInfo> {
        ktorRequest<UserInfo>("GET", url, null)
        super.response as UserInfo
    }
}
