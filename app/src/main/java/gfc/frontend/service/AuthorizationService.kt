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
import kotlinx.coroutines.*

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

    private fun saveToken(resp: Any) {
        val httpResponse = (resp as HttpResponse)
        if (httpResponse.status.isSuccess()) {
            super.response = httpResponse.headers["Authorization"]
        } else {
            super.response = null
        }
    }

    fun registerParent(url: String, request: SignupRequest) = runBlocking<String> {
        ktorAnonymousRequest<String>(url, request)
        super.response as String
    }

    fun login(loginUrl: String, credentials: SigninRequest) = runBlocking<String?> {
        println("Teraz3")

        try {
            loginRequest(loginUrl, credentials)
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            super.response = null
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            super.response = null
        } catch (ex: ServerResponseException) {
            // 5xx - response
            super.response = null
        } catch(ex: CancellationException) {
            println("!!!!!!!!!!!!!!!!!!!!!Error: ${ex}")
            super.response = null
        } catch(ex: Exception) {
            println("!!!!!!!!!!!!!!!!!!!!!Error: ${ex}")
            super.response = null
        }
        super.response as String?
    }


    private suspend fun loginRequest(url: String, credentials: SigninRequest) = coroutineScope<Unit> {
        println("Teraz4")
        val httpClient = HttpClient(Android){
        expectSuccess = true
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
        val httpResponse = httpClient.post (url) {
            body = credentials
            contentType(ContentType.Application.Json)
        } as HttpResponse

        if (httpResponse.status.isSuccess()){
            println("Teraz7")
             super.response = httpResponse.headers["Authorization"]
        }
        else {
            println("Teraz8")
             super.response = null
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
