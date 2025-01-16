package com.eltex.androidschool.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eltex.androidschool.dao.EventDao
import com.eltex.androidschool.entity.Converters
import com.eltex.androidschool.entity.EventEntity

@Database(
    entities = [EventEntity::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract val eventDao: EventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getInstance(context: Context): AppDb {
            INSTANCE?.let { return it }

            val application = context.applicationContext

            synchronized(this) {
                INSTANCE?.let { return it }

                val appDb =
                    Room.databaseBuilder(application, AppDb::class.java, "eltex_android_app_db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()

                INSTANCE = appDb

                return appDb
            }
        }
    }
}
