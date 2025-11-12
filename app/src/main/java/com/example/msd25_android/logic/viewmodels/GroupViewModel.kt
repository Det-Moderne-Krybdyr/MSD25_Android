package com.example.msd25_android.logic.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.msd25_android.logic.data.AppDatabase
import com.example.msd25_android.logic.data.group.Group
import com.example.msd25_android.logic.data.group.GroupUserRef
import com.example.msd25_android.logic.data.user.User

class
GroupViewModel(application: Application): AndroidViewModel(application) {

    private val dao = AppDatabase.Companion.getDatabase(application).groupDao()

    fun updateGroup(group: Group) {
        dao.updateGroup(group)
    }

    fun createGroupWithMembers(group: Group, members: List<User>) {
        val groupId = dao.insertGroup(group)

        members.forEach { member ->
            dao.insertGroupUserRef(GroupUserRef(groupId, member.id))
        }
    }
}