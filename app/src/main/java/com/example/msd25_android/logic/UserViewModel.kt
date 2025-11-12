package com.example.msd25_android.logic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.msd25_android.logic.data.AppDatabase
import com.example.msd25_android.logic.data.user.FriendCrossRef
import com.example.msd25_android.logic.data.user.User
import com.example.msd25_android.logic.data.user.UserWithFriends
import com.example.msd25_android.logic.data.user.UserWithGroups
import com.example.msd25_android.logic.data.user.UserWithNotifications
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.math.max
import kotlin.math.min

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).userDao()

    fun updateUser(user: User) {
        dao.updateUser(user)
    }

    fun deleteUser(id: Long) {
        dao.deleteUserById(id)
    }

    fun createFriendship(userId1: Long, userId2: Long) {
        if (userId1 == userId2) {
            return
        }

        dao.insertFriendCrossRef(FriendCrossRef(userId1, userId2))
        dao.insertFriendCrossRef(FriendCrossRef(userId2, userId1))
    }

    fun getUserById(id: Long): BackendResponse<User> {
        return BackendResponse.create(dao.getUserById(id), errorMsg = "User not found")
    }

    fun getUserByEmail(email: String): BackendResponse<User> {
        return BackendResponse.create(dao.getUserByEmail(email), errorMsg = "User not found")
    }

    fun getUserByPhone(phone: String): BackendResponse<User> {
        return BackendResponse.create(dao.getUserByPhone(phone), errorMsg = "User not found")
    }

    fun getUserWithFriends(phone: String): BackendResponse<UserWithFriends> {
        return BackendResponse.create(dao.getUserWithFriends(phone), errorMsg = "User not found")
    }

    fun getUserWithGroups(phone: String): BackendResponse<UserWithGroups> {
        return BackendResponse.create(dao.getUserWithGroups(phone), errorMsg = "User not found")
    }

    fun getUserWithNotifications(phone: String): BackendResponse<UserWithNotifications> {
        return BackendResponse.create(dao.getUserWithNotifications(phone), errorMsg = "User not found")
    }
}