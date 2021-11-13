package gfc.frontend.service

import android.app.Service
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import gfc.frontend.dataclasses.RepeatableTask
import gfc.frontend.dataclasses.Task
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class KtorService(context: Context?)  : Service()  {
    val queue = Volley.newRequestQueue(context)
    var response: Any? = null

    fun callBack (resp: Any) {
        println("RESP: " + resp.toString())
        this.response =  resp
    }

    suspend inline fun <reified T: Any> volleyRequest(meth: String, url: String, jsonArgs: JSONArray?)  = coroutineScope<Unit> {
        val httpClient = HttpClient(Android){
            expectSuccess = false
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
//            ResponseObserver { response ->
//                println("RESPONSE: " + response.receive())
//            }
        }

        val response = async {
            httpClient.request<T>(url) {
                method = when (meth) {
                    "GET" -> HttpMethod.Get
                    "POST" -> HttpMethod.Post
                    else -> HttpMethod.Put
                }
            }
        }.await().apply {
            callBack(this)
        }
        httpClient.close()
    }

//    suspend fun volleyRequest(method: String, url: String, jsonArgs: JSONArray?) = suspendCoroutine<JSONArray> {
//        val jsonObjectRequest = JsonArrayRequest(
//            when (method){
//                "GET" -> Request.Method.GET
//                "POST" -> Request.Method.POST
//                else -> Request.Method.PUT
//            },
//            url,
//            jsonArgs,
//            { response -> this.response = response },
//            {
//                println("Problem z połączeniem.")
//                this.response = null
//            }
//        )
//        queue.add(jsonObjectRequest)
//        this.response
//    }
}