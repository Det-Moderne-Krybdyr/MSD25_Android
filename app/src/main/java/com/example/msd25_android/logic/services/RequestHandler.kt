package com.example.msd25_android.logic.services

import android.content.Context
import android.icu.math.BigDecimal
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.msd25_android.dataStore
import com.example.msd25_android.logic.BackendResponse
import com.example.msd25_android.logic.data.serialize.BigDecimalSerializer
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import org.json.JSONObject
import kotlin.math.max
import kotlin.math.min


class RequestHandler(val errorHandler: (VolleyError) -> Unit, val scope: CoroutineScope, val retryMessageHandler: (String) -> Unit = {}, val successMessageHandler: (String) -> Unit = {}) {

    val json = Json {
        serializersModule = SerializersModule {
            contextual(BigDecimal::class, BigDecimalSerializer)
            ignoreUnknownKeys = true
        }
    }

    // Inline wrapper only gets serializers (no recursion here)
    suspend inline fun <reified R, reified P> post(
        context: Context,
        url: String,
        postObject: P,
        noinline onResponse: (BackendResponse<R>) -> Unit,
        noinline onFail: (String) -> Unit = {},
        noinline onSuccess: (String) -> Unit = {},
        retry: Int = 0,
    ) {
        val pSer: KSerializer<P> = json.serializersModule.serializer()
        val rSer: KSerializer<R> = json.serializersModule.serializer()
        // Delegate to non-inline implementation
        postImpl(
            context = context,
            url = url,
            postObject = postObject,
            pSer = pSer,
            rSer = rSer,
            onResponse = onResponse,
            onFail = onFail,
            onSuccess = onSuccess,
            retry = retry,
        )
    }

    // Non-inline, may recurse safely
    suspend fun <R, P> postImpl(
        context: Context,
        url: String,
        postObject: P,
        pSer: KSerializer<P>,
        rSer: KSerializer<R>,
        onResponse: (BackendResponse<R>) -> Unit,
        onFail: (String) -> Unit = {},
        onSuccess: (String) -> Unit = {},
        retry: Int = 0,
    ) {
        val jsonObj = JSONObject(json.encodeToString(pSer, postObject))
        val headers = HashMap<String, String>()
        val repository = UserRepository(context.dataStore)
        val token = repository.currentToken.first()!!
        headers["Content-Type"] = "application/json"
        headers["token"] = token

        val resSer: KSerializer<BackendResponse<R>> =
            BackendResponse.serializer(rSer)

        val jsonRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            jsonObj,
            { response ->
                if (retry > 0) successMessageHandler("Reconnected")
                val resObj = json.decodeFromString(resSer, response.toString())
                if (resObj.success) onSuccess(resObj.message) else onFail(resObj.message)
                onResponse(resObj)
            },
            Response.ErrorListener { error ->
                errorHandler(error)
                scope.launch {
                    if (retry == 0 || retry % 10 == 0) {
                        withContext(Dispatchers.Main) {
                            retryMessageHandler("Network error, retrying...")
                        }
                    }
                    var delayMillis = 0L
                    if (retry > 0) delayMillis = 2000L / (retry)

                    delayMillis = max(delayMillis, 500)

                    delay(delayMillis)

                    postImpl(context, url, postObject, pSer, rSer, onResponse, onFail, onSuccess, retry + 1)
                }
            }
        ) {
            override fun getHeaders(): Map<String, String> = headers
        }

        VolleySingleton.addToRequestQueue(context, jsonRequest)
    }


    suspend inline fun <reified R> get(
        context: Context,
        url: String,
        crossinline onResponse: (BackendResponse<R>) -> Unit,
        crossinline onFail: (String) -> Unit = {},
        crossinline onSuccess: (String) -> Unit = {},
    ) {
        val headers = HashMap<String, String?>()
        val repository = UserRepository(context.dataStore)
        val token = repository.currentToken.first()
        headers.put("Content-Type", "application/json")
        headers.put("token", token)

        val jsonRequest = object : JsonObjectRequest(
            url,
            { response ->
                val resObj = json.decodeFromString<BackendResponse<R>>(response.toString())
                if (resObj.success) onSuccess(resObj.message)
                else onFail(resObj.message)
                onResponse(resObj)
            },
            Response.ErrorListener { error ->
                errorHandler(error)

            }) {

            override fun getHeaders(): Map<String, String?> {
                return headers
            }
        }
        VolleySingleton.addToRequestQueue(context, jsonRequest)
    }
}