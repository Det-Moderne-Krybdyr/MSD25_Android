package com.example.msd25_android.logic.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.msd25_android.logic.data.user.User
import com.example.msd25_android.logic.data.user.FriendCrossRef
import com.example.msd25_android.logic.data.user.UserWithFriends
import com.example.msd25_android.logic.data.user.UserWithGroups
import com.example.msd25_android.logic.data.user.UserWithSessions
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Update
    fun updateUser(user: User)
    @Insert
    fun insertUser(user: User)
    @Query("DELETE FROM users WHERE id = :userId")
    fun deleteUserById(userId: Long)
    @Insert
    fun insertFriendCrossRef(crossRef: FriendCrossRef)
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Long): User?

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE phone_number = :phoneNumber")
    fun getUserByPhone(phoneNumber: String): User?

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserWithFriends(userId: Long): UserWithFriends?

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserWithGroups(userId: Long): UserWithGroups?

    @Transaction
    @Query("SELECT * FROM users WHERE phone_number = :phone")
    fun getUserWithSessions(phone: String): UserWithSessions?
}