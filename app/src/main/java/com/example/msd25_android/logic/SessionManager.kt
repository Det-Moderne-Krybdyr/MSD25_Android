package com.example.msd25_android.logic

import android.app.Application
import android.health.connect.datatypes.AppInfo
import com.example.msd25_android.API_URL
import com.example.msd25_android.UserAuthState
import com.example.msd25_android.dataStore
import com.example.msd25_android.logic.data.models.Session
import com.example.msd25_android.logic.data.models.User
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SessionManager(private val application: Application,
                     private val setUserAuthState: (UserAuthState) -> Unit,
                     private val coroutine: CoroutineScope
) {

    private val userRepository = UserRepository(application.dataStore)
    private val requestHandler = com.example.msd25_android.requestHandler
    private val url = "$API_URL/auth"

    suspend fun login(phone: String, password: String, onFail: (String) -> Unit = {}, onSuccess: (String) -> Unit = {}) {
        val user = User(phone_number = phone, password = password)

        val onResponse: (BackendResponse<String>) -> Unit = { res ->
            coroutine.launch {
                if (res.success) {
                    userRepository.saveUserToken(res.data!!)
                    userRepository.saveUserId(res.data.split(':')[0].toInt())
                    setUserAuthState(UserAuthState.AUTHENTICATED)
                }
                else {
                    setUserAuthState(UserAuthState.UNAUTHENTICATED)
                }
            }

        }

        requestHandler.post(
            context = application,
            url = "$url/signin",
            onResponse = onResponse,
            postObject = user,
            onFail = onFail,
            onSuccess = onSuccess
        )
    }

    suspend fun signup(user: User, onFail: (String) -> Unit = {}, onSuccess: (String) -> Unit = {}) {

        val onResponse: (BackendResponse<String>) -> Unit = { res ->
            coroutine.launch {
                if (res.success) {
                    userRepository.saveUserToken(res.data!!)
                    userRepository.saveUserId(res.data.split(':')[0].toInt())
                    setUserAuthState(UserAuthState.AUTHENTICATED)
                }
                else {
                    setUserAuthState(UserAuthState.UNAUTHENTICATED)
                    onFail(res.message)
                }
            }

        }

        requestHandler.post(
            context = application,
            url = "$url/signup",
            onResponse = onResponse,
            postObject = user,
            onFail = onFail,
            onSuccess = onSuccess
        )
    }

    suspend fun logout() {

        val token = userRepository.currentToken.first()

        val onResponse: (BackendResponse<String>) -> Unit = { res ->
            coroutine.launch {
                userRepository.saveUserId(-1)
                userRepository.saveUserToken("")
                setUserAuthState(UserAuthState.UNAUTHENTICATED)
            }
        }

        requestHandler.post(
            context = application,
            url = "$url/signout",
            onResponse = onResponse,
            postObject = TokenPostObject(token!!)
        )
    }

    suspend fun restoreToken() {
        val token = userRepository.currentToken.first()
        if (token == null) {
            setUserAuthState(UserAuthState.UNAUTHENTICATED)
            return
        }

        val onResponse: (BackendResponse<Boolean>) -> Unit = { res ->
            if (res.success) {
                setUserAuthState(
                    if (res.data!!)
                        UserAuthState.AUTHENTICATED
                    else
                        UserAuthState.UNAUTHENTICATED
                )
            }
        }

        requestHandler.post(
            context = application,
            url = "$url/validate",
            onResponse = onResponse,
            postObject = TokenPostObject(token)
        )
    }
    @Serializable
    private data class TokenPostObject(
        val token: String
    )
}