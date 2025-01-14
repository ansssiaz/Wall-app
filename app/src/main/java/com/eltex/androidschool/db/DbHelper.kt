package com.eltex.androidschool.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, "eltex_android_db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE ${EventTable.TABLE_NAME} (
                ${EventTable.ID}	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                ${EventTable.AUTHOR_ID}	INTEGER NOT NULL DEFAULT 0,
                ${EventTable.AUTHOR}	TEXT NOT NULL,
                ${EventTable.AUTHOR_AVATAR}	TEXT,
                ${EventTable.CONTENT}	TEXT NOT NULL,
                ${EventTable.PUBLISHED}	TEXT NOT NULL,
                ${EventTable.DATETIME}	TEXT NOT NULL,
                ${EventTable.TYPE}	TEXT NOT NULL DEFAULT 'OFFLINE' CHECK(type IN ('OFFLINE', 'ONLINE')),
                ${EventTable.LIKE_OWNER_IDS}	TEXT,
                ${EventTable.LIKED_BY_ME}	INTEGER NOT NULL DEFAULT 0,
                ${EventTable.PARTICIPANT_IDS}	TEXT,
                ${EventTable.PARTICIPATED_BY_ME}	INTEGER NOT NULL DEFAULT 0,
                ${EventTable.LINK}	TEXT,
                ${EventTable.LIKES}	INTEGER NOT NULL DEFAULT 0,
                ${EventTable.PARTICIPANTS}	INTEGER NOT NULL DEFAULT 0
            );
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}