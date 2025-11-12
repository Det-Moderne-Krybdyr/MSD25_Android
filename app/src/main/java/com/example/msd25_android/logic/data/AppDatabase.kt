package com.example.msd25_android.logic.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.msd25_android.logic.data.dao.GroupDao
import com.example.msd25_android.logic.data.dao.SessionDao
import com.example.msd25_android.logic.data.dao.UserDao
import com.example.msd25_android.logic.data.expense.Expense
import com.example.msd25_android.logic.data.expense.ExpenseShare
import com.example.msd25_android.logic.data.group.Group
import com.example.msd25_android.logic.data.group.GroupUserRef
import com.example.msd25_android.logic.data.notification.Notification
import com.example.msd25_android.logic.data.seed.seed
import com.example.msd25_android.logic.data.session.Session
import com.example.msd25_android.logic.data.user.User
import com.example.msd25_android.logic.data.user.FriendCrossRef
import java.util.concurrent.Executors

@Database(
    entities =
        [
            User::class,
            FriendCrossRef::class,
            Group::class,
            GroupUserRef::class,
            Expense::class,
            ExpenseShare::class,
            Session::class,
            Notification::class
        ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao
    abstract fun sessionDao(): SessionDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDatabase::class.java,
                    name = "expenses_app_db"
                ).addCallback(object : Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        // Run in background thread
                        Executors.newSingleThreadExecutor().execute {
                            seed(INSTANCE!!)
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
