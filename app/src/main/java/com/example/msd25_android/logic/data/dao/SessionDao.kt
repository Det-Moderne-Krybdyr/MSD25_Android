package com.example.msd25_android.logic.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.example.msd25_android.logic.data.session.Session

@Dao
interface SessionDao {
    @Insert
    fun insertSession(session: Session)

    @Delete
    fun deleteSession(session: Session)
}