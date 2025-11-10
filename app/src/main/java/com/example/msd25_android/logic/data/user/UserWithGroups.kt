package com.example.msd25_android.logic.data.user

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.msd25_android.logic.data.group.Group
import com.example.msd25_android.logic.data.group.GroupUserRef

data class UserWithGroups(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = GroupUserRef::class,
            parentColumn = "userId",
            entityColumn = "groupId"
        )
    )
    val groups: List<Group>
)