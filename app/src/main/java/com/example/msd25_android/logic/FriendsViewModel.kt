package com.example.msd25_android.logic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.msd25_android.logic.data.AppDatabase
import com.example.msd25_android.logic.data.Friend
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FriendsViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).friendDao()

    val friends = dao.getAllFriends()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addFriend(name: String, phone: String) {
        viewModelScope.launch {
            dao.insert(Friend(name = name, phoneNumber = phone))
        }
    }
}
