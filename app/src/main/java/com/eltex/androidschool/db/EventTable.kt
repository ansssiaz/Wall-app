package com.eltex.androidschool.db

object EventTable {
    const val TABLE_NAME = "Events"
    const val ID = "id"
    const val AUTHOR_ID = "authorId"
    const val AUTHOR = "author"
    const val AUTHOR_AVATAR = "authorAvatar"
    const val CONTENT = "content"
    const val PUBLISHED = "published"
    const val DATETIME = "datetime"
    const val TYPE = "type"
    const val LIKE_OWNER_IDS = "likeOwnerIds"
    const val LIKED_BY_ME = "likedByMe"
    const val PARTICIPANT_IDS = "participantIds"
    const val PARTICIPATED_BY_ME = "participatedByMe"
    const val LINK = "link"
    const val LIKES = "likes"
    const val PARTICIPANTS = "participants"

    val allColumns = arrayOf(
        ID,
        AUTHOR_ID,
        AUTHOR,
        AUTHOR_AVATAR,
        CONTENT,
        PUBLISHED,
        DATETIME,
        TYPE,
        LIKE_OWNER_IDS,
        LIKED_BY_ME,
        PARTICIPANT_IDS,
        PARTICIPATED_BY_ME,
        LINK,
        LIKES,
        PARTICIPANTS
    )
}