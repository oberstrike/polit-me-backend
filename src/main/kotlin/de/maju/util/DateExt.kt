package de.maju.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId


fun LocalDate.toEpochMilli(zoneId: ZoneId = ZoneId.systemDefault()): Long {
    return this.atStartOfDay().atZone(zoneId).toInstant().toEpochMilli()
}

fun LocalDateTime.toEpochMilli(zoneId: ZoneId = ZoneId.systemDefault()): Long {
    return this.atZone(zoneId).toInstant().toEpochMilli()
}


fun Long?.toLocalDateTime(zoneId: ZoneId? = ZoneId.systemDefault()): LocalDateTime? {
    return if (this == null) {
        null
    } else {
        Instant.ofEpochMilli(this).atZone(zoneId).toLocalDateTime()
    }
}

fun Long.toZonedDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}

