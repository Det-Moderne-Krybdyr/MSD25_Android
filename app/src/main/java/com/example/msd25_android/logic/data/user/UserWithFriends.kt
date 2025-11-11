package com.example.msd25_android.logic.data.user

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithFriends(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = FriendCrossRef::class,
            parentColumn = "id",
            entityColumn = "friendId"
        )
    )
    val friends: List<User>
)
