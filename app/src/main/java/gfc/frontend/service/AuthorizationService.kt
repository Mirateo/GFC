package gfc.frontend.service

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.ActivityCompat.finishAffinity
import com.google.android.material.internal.ContextUtils
import gfc.frontend.LoginActivity
import gfc.frontend.MainActivity
import gfc.frontend.controllers.TasksController
import gfc.frontend.dataclasses.UserInfo
import gfc.frontend.requests.SigninRequest
import gfc.frontend.requests.SignupRequest
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
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

object AuthorizationService: KtorService() {
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
        return@runBlocking loginRequest(loginUrl, credentials)
    }

    private suspend fun loginRequest(url: String, credentials: SigninRequest): String? {
        val httpClient = super.anonymousHttpClient

        return try {
            val httpResponse = httpClient.post (url) {
                body = credentials
                contentType(ContentType.Application.Json)
            } as HttpResponse

            return if (httpResponse.status.isSuccess()){
                httpResponse.headers["Authorization"]
            }
            else {
                null
            }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            httpClient.close()
            null
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            httpClient.close()

//            context.startActivity(Intent(context, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
//            (context as Application).finishAffinity()
            null
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            httpClient.close()
            null
        } catch(ex: Exception) {
            println("!!!!!!!!!!!!!!!!!!!!!Error: ${ex}")
            httpClient.close()
            null
        }
    }

    fun getUserInfo(url: String) = runBlocking<UserInfo> {
        ktorRequest<UserInfo>("GET", url, null)
        super.response as UserInfo
    }

    fun editProfile(url: String, newUser: UserInfo) = runBlocking<String?>  {
        ktorRequest<String?>("POST", url, newUser)
        super.response as String?
    }
}
