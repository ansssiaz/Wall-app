package com.eltex.androidschool.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import com.eltex.androidschool.db.EventTable
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Type
import com.eltex.androidschool.utils.getBooleanOrThrow
import com.eltex.androidschool.utils.getIntOrThrow
import com.eltex.androidschool.utils.getLongOrThrow
import com.eltex.androidschool.utils.getStringOrThrow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EventDaoImpl(private val db: SQLiteDatabase) : EventDao {
    override fun getAll(): List<Event> {
        val result = mutableListOf<Event>()
        db.query(
            EventTable.TABLE_NAME,
            EventTable.allColumns,
            null,
            null,
            null,
            null,
            "${EventTable.ID} DESC"
        ).use { cursor ->
            while (cursor.moveToNext()) {
                result += cursor.readEvent()
            }
        }
        return result
    }

    override fun save(event: Event): Event {
        val contentValues = contentValuesOf(
            EventTable.AUTHOR_ID to event.authorId,
            EventTable.AUTHOR to event.author,
            EventTable.AUTHOR_AVATAR to event.authorAvatar,
            EventTable.CONTENT to event.content,
            EventTable.PUBLISHED to event.published,
            EventTable.DATETIME to event.datetime,
            EventTable.TYPE to event.type.name,
            EventTable.LIKE_OWNER_IDS to Json.encodeToString(event.likeOwnerIds),
            EventTable.LIKED_BY_ME to event.likedByMe,
            EventTable.PARTICIPANT_IDS to Json.encodeToString(event.participantIds),
            EventTable.PARTICIPATED_BY_ME to event.participatedByMe,
            EventTable.LINK to event.link,
            EventTable.LIKES to event.likes,
            EventTable.PARTICIPANTS to event.participants
        )
        if (event.id != 0L) {
            contentValues.put(EventTable.ID, event.id)
        }
        val id = db.replace(EventTable.TABLE_NAME, null, contentValues)
        return getEventById(id)
    }

    override fun likeById(id: Long): Event {
        db.execSQL(
            """
            UPDATE ${EventTable.TABLE_NAME} SET
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END,
                likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END
            WHERE id = ?;
        """.trimIndent(),
            arrayOf(id.toString())
        )
        return getEventById(id)
    }

    override fun participateById(id: Long): Event {
        db.execSQL(
            """
            UPDATE ${EventTable.TABLE_NAME} SET
                participatedByMe = CASE WHEN participatedByMe THEN 0 ELSE 1 END,
                participants = participants + CASE WHEN participatedByMe THEN -1 ELSE 1 END
            WHERE id = ?;
        """.trimIndent(),
            arrayOf(id.toString())
        )
        return getEventById(id)
    }

    override fun getEventById(id: Long): Event =
        db.query(
            EventTable.TABLE_NAME,
            EventTable.allColumns,
            "${EventTable.ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToFirst()
            it.readEvent()
        }

    override fun deleteById(id: Long) {
        db.delete(EventTable.TABLE_NAME, "${EventTable.ID} = ?", arrayOf(id.toString()))
    }

    private fun Cursor.readEvent(): Event =
        Event(
            id = getLongOrThrow(EventTable.ID),
            authorId = getLongOrThrow(EventTable.AUTHOR_ID),
            author = getStringOrThrow(EventTable.AUTHOR),
            authorAvatar = getStringOrThrow(EventTable.AUTHOR_AVATAR),
            content = getStringOrThrow(EventTable.CONTENT),
            published = getStringOrThrow(EventTable.PUBLISHED),
            datetime = getStringOrThrow(EventTable.DATETIME),
            type = Type.valueOf(getStringOrThrow(EventTable.TYPE)),
            likeOwnerIds = parseLongList(getStringOrThrow(EventTable.LIKE_OWNER_IDS)),
            likedByMe = getBooleanOrThrow(EventTable.LIKED_BY_ME),
            participantIds = parseLongList(getStringOrThrow(EventTable.PARTICIPANT_IDS)),
            participatedByMe = getBooleanOrThrow(EventTable.PARTICIPATED_BY_ME),
            link = getStringOrThrow(EventTable.LINK),
            likes = getIntOrThrow(EventTable.LIKES),
            participants = getIntOrThrow(EventTable.PARTICIPANTS)
        )

    private fun parseLongList(longListString: String?): List<Long?> {
        return longListString?.let {
            Json.decodeFromString<List<Long?>>(it)
        } ?: emptyList()
    }
}