package com.example.msd25_android.logic.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.msd25_android.logic.data.user.User
import com.example.msd25_android.logic.data.user.FriendCrossRef
import com.example.msd25_android.logic.data.user.UserWithFriends
import com.example.msd25_android.logic.data.user.UserWithGroups
import com.example.msd25_android.logic.data.user.UserWithNotifications
import com.example.msd25_android.logic.data.user.UserWithSessions
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Update
    fun updateUser(user: User)
    @Insert(onConflict = IGNORE)
    fun insertUser(user: User)
    @Query("DELETE FROM users WHERE id = :userId")
    fun deleteUserById(userId: Long)
    @Insert(onConflict = IGNORE)
    fun insertFriendCrossRef(crossRef: FriendCrossRef)
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Long): User?

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>
    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE phone_number = :phoneNumber")
    fun getUserByPhone(phoneNumber: String): User?

    @Transaction
    @Query("SELECT * FROM users WHERE phone_number = :phone")
    fun getUserWithFriends(phone: String): UserWithFriends?

    @Transaction
    @Query("SELECT * FROM users WHERE phone_number = :phone")
    fun getUserWithGroups(phone: String): UserWithGroups?

    @Transaction
    @Query("SELECT * FROM users WHERE phone_number = :phone")
    fun getUserWithNotifications(phone: String): UserWithNotifications?

    @Transaction
    @Query("SELECT * FROM users WHERE phone_number = :phone")
    fun getUserWithSessions(phone: String): UserWithSessions?
}