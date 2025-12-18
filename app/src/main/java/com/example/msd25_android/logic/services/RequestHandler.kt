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
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.json.JSONObject


class RequestHandler(val errorHandler: (VolleyError) -> Unit) {

    val json = Json {
        serializersModule = SerializersModule {
            contextual(BigDecimal::class, BigDecimalSerializer)
        }
    }

    suspend inline fun <reified R, reified P> post(
        context: Context,
        url: String,
        postObject: P,
        crossinline onResponse: (BackendResponse<R>) -> Unit,
        crossinline onFail: (String) -> Unit = {},
        crossinline onSuccess: (String) -> Unit = {},
    ) {

        val jsonObj = JSONObject(json.encodeToString(postObject))
        val headers = HashMap<String, String?>()
        val repository = UserRepository(context.dataStore)
        val token = repository.currentToken.first()
        headers.put("Content-Type", "application/json")
        headers.put("token", token)

        val jsonRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            jsonObj,
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