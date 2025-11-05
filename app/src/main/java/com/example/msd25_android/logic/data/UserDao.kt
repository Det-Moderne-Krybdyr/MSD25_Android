package com.example.msd25_android.logic.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.msd25_android.logic.data.user.User
import com.example.msd25_android.logic.data.user.FriendCrossRef
import com.example.msd25_android.logic.data.user.UserWithFriends

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Insert
    suspend fun addFriendShip(crossRef: FriendCrossRef)

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserWithFriends(userId: Long): UserWithFriends
}