package com.eltex.androidschool.utils

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class DateFormatter @Inject constructor (private val zoneId: ZoneId) {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")

    fun formatDate(date: Instant): String = formatter.format(date.atZone(zoneId))

    fun getFormatter(): DateTimeFormatter = formatter
}