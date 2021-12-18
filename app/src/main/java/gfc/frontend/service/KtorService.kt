package gfc.frontend.service

import android.app.Service
import android.content.Context
import com.android.volley.toolbox.Volley
import com.google.gson.*
import gfc.frontend.dataclasses.TaskDTO
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
import com.google.gson.Gson
import com.google.gson.GsonBuilder








abstract class KtorService(context: Context?)  : Service()  {
    val queue = Volley.newRequestQueue(context)
    var response: Any? = null

    fun callBack (resp: Any) {
        println("RESP: $resp")
        this.response =  resp
    }

    suspend inline fun <reified T: Any> ktorRequest(meth: String, url: String, json: Any?)  = coroutineScope<Unit> {
        val httpClient = HttpClient(Android){
            expectSuccess = false
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
        withContext(Dispatchers.Default) {
            println(json)
            when (meth) {
                "GET" -> {
                    httpClient.get<T>(url)
                }
                "POST" -> {
                    httpClient.post<T>(url) {
                        headers{
                            append(HttpHeaders.ContentType, ContentType.Application.Json)
                        }
                        if (json != null) {
                            body = json
                        }
                    }
                }
                else -> {
                    httpClient.put<T>(url) {
                        if (json != null) {
                            body = json
                        }
                    }
                }
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
        val gson = Gson()
        val jsonElement = gson.toJsonTree(newTask)
        val jsonObject = jsonElement as JsonObject

        ktorRequest<Long>("POST", url, gson.toJson(newTask))
        response as Long
    }

}