package gfc.frontend.service

import android.app.Service
import android.content.Context
import gfc.frontend.requests.TaskDTO
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import io.ktor.client.features.logging.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.statement.*
import kotlin.system.exitProcess


abstract class KtorService : Service()  {
    var response: Any? = null
    lateinit var context: Context

    fun init(context: Context){
        this.context = context
    }

    val anonymousHttpClient = HttpClient(Android){
        expectSuccess = true
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    fun callBack (resp: Any?) {
        this.response =  resp
    }

    suspend inline fun <reified T: Any?> ktorRequest(meth: String, url: String, json: Any?)  = coroutineScope<Unit> {
        val prefs = context.getSharedPreferences("credentials", MODE_PRIVATE)

        val username = prefs.getString("username", "")
        val password = prefs.getString("password", "")
        val token = prefs.getString("token", "")

        if(username == null || password == null || token == null) {
            exitProcess(1)
        }

        val httpClient = HttpClient(Android){
            expectSuccess = true
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
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

        response = try {
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
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: Exception) {
            println("Error: $ex")
            null
        }

        httpClient.close()
    }

    suspend inline fun <reified T: Any> ktorAnonymousRequest(url: String, json: Any)  = coroutineScope<Unit> {
        val httpClient = anonymousHttpClient

        response = try {
            httpClient.post<T>(url) {
                body = json
                contentType(ContentType.Application.Json)
            }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ServerResponseException) {
            println("Error: ${ex.response.status.description}")
            null
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