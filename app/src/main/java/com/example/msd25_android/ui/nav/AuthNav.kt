package com.example.msd25_android.ui.nav

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.msd25_android.AppDestinations
import com.example.msd25_android.AuthDestinations
import com.example.msd25_android.UserAuthState
import com.example.msd25_android.logic.SessionManager
import com.example.msd25_android.ui.screens.LoginScreen
import com.example.msd25_android.ui.screens.SignUpScreen
import kotlinx.coroutines.*

@Composable
fun AuthNav(current: AuthDestinations, setCurrent: (AuthDestinations) -> Unit, sessionManager: SessionManager) {

    val coroutineScope = rememberCoroutineScope()

    return when (current) {
        AuthDestinations.LOGIN -> LoginScreen(
            onLogin = {phone, pwd ->
                sessionManager.login(phone, pwd) },
            onGoToSignUp = { setCurrent(AuthDestinations.SIGNUP) }
        )
        AuthDestinations.SIGNUP -> SignUpScreen(
            onCreate = { user ->
                val res = sessionManager.signup(user)
                if (res.success) setCurrent(AuthDestinations.LOGIN)
                res
                       },
            onGoToLogin = { setCurrent(AuthDestinations.LOGIN) }
        )
    }
}