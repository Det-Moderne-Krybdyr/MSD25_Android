package com.example.msd25_android.logic.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.msd25_android.logic.data.group.Group
import com.example.msd25_android.logic.data.group.GroupUserRef
import com.example.msd25_android.logic.data.group.GroupWithExpenses
import com.example.msd25_android.logic.data.group.GroupWithMembers

@Dao
interface GroupDao {
    @Insert
    fun insertGroup(group: Group)
    @Update
    fun updateGroup(group: Group)
    @Insert
    fun insertGroupUserRef(ref: GroupUserRef)
    @Transaction
    @Query("SELECT * FROM `groups` WHERE id = :groupId")
    fun getGroupWithMembers(groupId: Long): GroupWithMembers?

    @Transaction
    @Query("SELECT * FROM `groups` WHERE id = :groupId")
    fun getGroupWithExpenses(groupId: Long): GroupWithExpenses?
}