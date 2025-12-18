package com.example.msd25_android.logic.services

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.msd25_android.API_URL
import com.example.msd25_android.logic.BackendResponse
import com.example.msd25_android.logic.data.models.Group
import com.example.msd25_android.logic.data.models.User

class GroupService(private val application: Application): AndroidViewModel(application) {

    private val requestHandler = com.example.msd25_android.requestHandler

    private val url = "$API_URL/group"

    suspend fun getGroupInfo(groupId: Int, onResponse: (BackendResponse<User>) -> Unit) {

        requestHandler.post(
            context = application,
            url = "$url/getinfo",
            postObject = groupId,
            onResponse = onResponse
        )
    }

    suspend fun createGroup(group: Group, onResponse: (BackendResponse<Group>) -> Unit) {

        requestHandler.post(
            context = application,
            url = "$url/post",
            postObject = group,
            onResponse = onResponse,
        )
    }
}