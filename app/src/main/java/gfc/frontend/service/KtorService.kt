package gfc.frontend.service

import android.app.Service
import android.content.Context
import com.android.volley.toolbox.Volley
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
import org.json.JSONArray

abstract class KtorService(context: Context?)  : Service()  {
    val queue = Volley.newRequestQueue(context)
    var response: Any? = null

    fun callBack (resp: Any) {
        println("RESP: $resp")
        this.response =  resp
    }

    suspend inline fun <reified T: Any> ktorRequest(meth: String, url: String, jsonArgs: JSONArray?)  = coroutineScope<Unit> {
        val httpClient = HttpClient(Android){
            expectSuccess = false
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
        withContext(Dispatchers.Default) {
            httpClient.request<T>(url) {
                method = when (meth) {
                    "GET" -> HttpMethod.Get
                    "POST" -> HttpMethod.Post
                    else -> HttpMethod.Put
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
}