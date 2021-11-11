package gfc.frontend.service

import android.app.Service
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import gfc.frontend.dataclasses.RepeatableTask
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class VolleyService(context: Context?)  : Service()  {
    val queue = Volley.newRequestQueue(context)
    var response: JSONArray? = null

    fun popResponse(): JSONArray? {
        val tmp = response
        response = null
        return tmp
    }

    suspend fun volleyRequest(method: String, url: String, jsonArgs: JSONArray?): JSONArray? = suspendCoroutine<JSONArray> {
        val jsonObjectRequest = JsonArrayRequest(
            when (method){
                "GET" -> Request.Method.GET
                "POST" -> Request.Method.POST
                else -> Request.Method.PUT
            },
            url,
            jsonArgs,
            { response -> this.response = response },
            {
                println("Problem z połączeniem.")
                this.response = null
            }
        )
        queue.add(jsonObjectRequest)
        this.response
    }
}