package com.example.msd25_android.logic.data.group

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.msd25_android.logic.data.user.FriendCrossRef
import com.example.msd25_android.logic.data.user.User

data class GroupWithMembers(
    @Embedded
    val group: Group,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = GroupUserRef::class,
            parentColumn = "groupId",
            entityColumn = "userId"
        )
    )
    val members: List<User>
)
