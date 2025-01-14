package com.eltex.androidschool.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.eltex.androidschool.dao.EventDaoImpl

class AppDb private constructor(db: SQLiteDatabase) {
    val eventDao = EventDaoImpl(db)

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null
        fun getInstance(context: Context): AppDb {
            INSTANCE?.let { return it }
            val application = context.applicationContext
            synchronized(this) {
                INSTANCE?.let { return it }
                val appDb = AppDb(DbHelper(application).writableDatabase)
                INSTANCE = appDb
                return appDb
            }
        }
    }
}
