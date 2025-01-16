package com.eltex.androidschool.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Type
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromLongList(likeOwnerIds: List<Long?>): String {
        return Json.encodeToString(likeOwnerIds)
    }

    @TypeConverter
    fun toLongList(longListString: String?): List<Long?> {
        return longListString?.let {
            Json.decodeFromString<List<Long?>>(it)
        } ?: emptyList()
    }

    @TypeConverter
    fun fromType(type: Type): String {
        return type.name
    }

    @TypeConverter
    fun toType(typeString: String): Type {
        return try {
            Type.valueOf(typeString)
        } catch (e: IllegalArgumentException) {
            Type.OFFLINE
        }
    }
}

@Entity(tableName = "Events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "authorId")
    val authorId: Long = 0L,
    @ColumnInfo(name = "author")
    val author: String = "",
    @ColumnInfo(name = "authorAvatar")
    val authorAvatar: String? = "",
    @ColumnInfo(name = "content")
    val content: String = "",
    @ColumnInfo(name = "published")
    val published: String = "",
    @ColumnInfo(name = "datetime")
    val datetime: String = "",
    @ColumnInfo(name = "type")
    val type: String = Type.OFFLINE.name,
    @ColumnInfo(name = "likeOwnerIds")
    val likeOwnerIds: String = "",
    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean = false,
    @ColumnInfo(name = "participantIds")
    val participantIds: String = "",
    @ColumnInfo(name = "participatedByMe")
    val participatedByMe: Boolean = false,
    @ColumnInfo(name = "link")
    val link: String? = "",
    @ColumnInfo(name = "likes")
    val likes: Int = 0,
    @ColumnInfo(name = "participants")
    val participants: Int = 0,
) {
    companion object {
        private val converters = Converters()
        fun fromEvent(event: Event): EventEntity = with(event) {
            EventEntity(
                id = id,
                authorId = authorId,
                author = author,
                authorAvatar = authorAvatar,
                content = content,
                published = published,
                datetime = datetime,
                type = converters.fromType(type),
                likeOwnerIds = converters.fromLongList(likeOwnerIds),
                likedByMe = likedByMe,
                participantIds = converters.fromLongList(participantIds),
                participatedByMe = participatedByMe,
                link = link,
                likes = likes,
                participants = participants
            )
        }
    }

    fun toEvent(): Event = Event(
        id = id,
        authorId = authorId,
        author = author,
        authorAvatar = authorAvatar,
        content = content,
        published = published,
        datetime = datetime,
        type = converters.toType(type),
        likeOwnerIds = converters.toLongList(likeOwnerIds),
        likedByMe = likedByMe,
        participantIds = converters.toLongList(participantIds),
        participatedByMe = participatedByMe,
        link = link,
        likes = likes,
        participants = participants
    )
}