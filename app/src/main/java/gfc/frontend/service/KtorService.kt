package gfc.frontend.service

import android.accounts.AccountManager
import android.app.Service
import android.content.Context
import android.os.Bundle
import android.os.Handler
import com.android.volley.toolbox.Volley
import gfc.frontend.requests.TaskDTO
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import io.ktor.client.features.logging.*
import android.content.SharedPreferences
import gfc.frontend.requests.SigninRequest
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import kotlin.system.exitProcess


abstract class KtorService(val context: Context?)  : Service()  {
    var response: Any? = null

    val anonymousHttpClient = HttpClient(Android){
        expectSuccess = false
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    fun callBack (resp: Any) {
        this.response =  resp
    }

    suspend inline fun <reified T: Any> ktorRequest(meth: String, url: String, json: Any?)  = coroutineScope<Unit> {
        println("!!!!!!!!!!!!!!!!!!!!!!!!" + json.toString())
        val prefs = context!!.getSharedPreferences("credentials", MODE_PRIVATE)

        val username = prefs.getString("username", "")
        val password = prefs.getString("password", "")
        val token = prefs.getString("token", "")

        if(username == null || password == null || token == null) {
            exitProcess(1)
        }

        val httpClient = HttpClient(Android){
            expectSuccess = false
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
//            install(Auth) {
//                var newToken: String
//
//                bearer {
//                    refreshTokens { unauthorizedResponse: HttpResponse ->
//                        withContext(Dispatchers.Default) {
//                            anonymousHttpClient.post<T>("https://gamefication-for-children.herokuapp.com/login") {
//                                body = SigninRequest(username, password)
//                                contentType(ContentType.Application.Json)
//                            }
//                        }.apply {
//                            newToken = (this as HttpResponse).headers["Authorization"]!!
//                            getSharedPreferences("credentials", MODE_PRIVATE).edit().putString("token", newToken).apply()
//                        }
//
//                        BearerTokens(
//                            accessToken = token,
//                            refreshToken = newToken
//                        )
//                    }
//                }
//            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(
                            accessToken = token,
                            refreshToken = token
                        )
                    }
                }
            }
        }

        withContext(Dispatchers.Default) {
            when (meth) {
                "GET" -> {
                    httpClient.get<T>(url) {
                        contentType(ContentType.Application.Json)
                    }
                }
                "POST" -> {
                    httpClient.post<T>(url) {
                        if (json != null) {
                            body = json
                        }
                        contentType(ContentType.Application.Json)
                    }
                }
                else -> {
                    httpClient.put<T>(url) {
                        if (json != null) {
                            body = json
                        }
                        contentType(ContentType.Application.Json)
                    }
                }
            }
        }.apply {
            callBack(this)
        }

        httpClient.close()
    }

    suspend inline fun <reified T: Any> ktorAnonymousRequest(url: String, json: Any)  = coroutineScope<Unit> {
        val httpClient = anonymousHttpClient

        withContext(Dispatchers.Default) {
            httpClient.post<T>(url) {
                body = json
                contentType(ContentType.Application.Json)
            }
        }.apply {
            callBack(this)
        }

        httpClient.close()
    }

    fun taskDone(url: String) = runBlocking<Long>  {
        ktorRequest <Long>("GET", url, null)
        response as Long
    }

    fun taskUndone(url: String) = runBlocking<Long>  {
        ktorRequest <Long>("GET", url, null)
        response as Long
    }

    fun addTask(url: String, newTask: TaskDTO) = runBlocking<Long> {
        ktorRequest<Long>("POST", url, newTask)
        response as Long
    }

}