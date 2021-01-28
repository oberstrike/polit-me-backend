package de.maju.util

import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class LocalDateTimeMapper {

    fun asLong(localDate: LocalDateTime?): Long {
        return localDate?.toEpochMilli() ?: 0
    }

    fun asLocalDateTime(milli: Long?): LocalDateTime? {
        return if (milli == 0L) LocalDateTime.now() else milli?.toZonedDateTime()
    }

}
