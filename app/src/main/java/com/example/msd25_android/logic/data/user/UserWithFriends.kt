package com.example.msd25_android.logic.data.user

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithFriends(
    @Embedded
    var user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "firstId",
        associateBy = Junction(
            value = FriendCrossRef::class,
            parentColumn = "userId",
            entityColumn = "secondId"
        )
    )
    var friends: List<User>
)
