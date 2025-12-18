package com.example.msd25_android.logic.services

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.msd25_android.API_URL
import com.example.msd25_android.dataStore
import com.example.msd25_android.logic.BackendResponse
import com.example.msd25_android.logic.data.models.Group
import com.example.msd25_android.logic.data.models.User
import com.example.msd25_android.ui.user_repository.UserRepository
import kotlinx.coroutines.flow.first

class UserService(private val application: Application): AndroidViewModel(application) {
    private val requestHandler = com.example.msd25_android.requestHandler

    private val url = "$API_URL/user"

    suspend fun getUserInfo(onResponse: (BackendResponse<User>) -> Unit) {
        // token is added to headers automatically, and user is found from token

        requestHandler.get(
            context = application,
            url = "$url/getinfo",
            onResponse = onResponse
        )
    }

    suspend fun updateUser(user: User, onResponse: (BackendResponse<String>) -> Unit) {
        requestHandler.post(
            context = application,
            url = "$url/update",
            postObject = user,
            onResponse = onResponse
        )
    }

    suspend fun getFriends(onResponse: (BackendResponse<List<User>>) -> Unit) {
        requestHandler.get(
            context = application,
            url = "$url/friends",
            onResponse = onResponse,
        )
    }

    suspend fun addFriend(friendPhone: String, onResponse: (BackendResponse<String>) -> Unit) {
        val friend = User(phone_number = friendPhone)

        requestHandler.post(
            context = application,
            url = "$url/friends/add",
            postObject = friend,
            onResponse = onResponse,
        )
    }

    suspend fun getGroups(onResponse: (BackendResponse<List<Group>>) -> Unit) {
        requestHandler.get(
            context = application,
            url = "$url/groups",
            onResponse = onResponse
        )
    }
}