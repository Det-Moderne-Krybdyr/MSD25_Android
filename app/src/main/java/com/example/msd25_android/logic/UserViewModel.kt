package com.example.msd25_android.logic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.msd25_android.logic.data.AppDatabase
import com.example.msd25_android.logic.data.user.FriendCrossRef
import com.example.msd25_android.logic.data.user.User
import com.example.msd25_android.logic.data.user.UserWithFriends
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

    fun getUserById(id: Long): User? {
        return dao.getUserById(id)
    }

    fun getUserByEmail(email: String): User? {
        return dao.getUserByEmail(email)
    }

    fun getUserByPhone(phoneNumber: String): User? {
        return dao.getUserByPhone(phoneNumber)
    }

    fun getFriends(id: Long): UserWithFriends? {
        return dao.getUserWithFriends(id)
    }

}