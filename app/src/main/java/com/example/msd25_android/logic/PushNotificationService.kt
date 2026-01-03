package com.example.msd25_android.logic

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.rememberCoroutineScope
import com.example.msd25_android.API_URL
import com.example.msd25_android.logic.services.RequestHandler
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PushNotificationService: FirebaseMessagingService() {

    private val requestHandler = RequestHandler(
        errorHandler = { error -> Log.e("REQUESTHANDLER", error.toString()) },
        scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
        retryMessageHandler = { message -> Toast.makeText(application, message, Toast.LENGTH_SHORT) },
        successMessageHandler = { message -> Toast.makeText(application, message, Toast.LENGTH_SHORT) }
    )
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        data class ReqObj(
            val token: String
        )

        scope.launch {
            requestHandler.post<String, ReqObj>(
                context = application,
                url = "$API_URL/user/fcm",
                postObject = ReqObj(token),
                onResponse = {}
            )
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


    }
}