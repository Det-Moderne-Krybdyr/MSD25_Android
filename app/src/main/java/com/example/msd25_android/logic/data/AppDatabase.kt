package com.example.msd25_android.logic.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.msd25_android.logic.data.user.User
import com.example.msd25_android.logic.data.user.FriendCrossRef

@Database(
    entities =
        [
            User::class,
            FriendCrossRef::class
        ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun friendDao(): FriendDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDatabase::class.java,
                    name = "expenses_app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
