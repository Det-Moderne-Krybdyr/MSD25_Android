package com.example.msd25_android.logic.data.user

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.msd25_android.logic.data.group.Group
import com.example.msd25_android.logic.data.group.GroupUserRef
import com.example.msd25_android.logic.data.session.Session

data class UserWithSessions(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val sessions: List<Session>
)
