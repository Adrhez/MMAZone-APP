package com.example.mmazone.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.format(dateFormatter)
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, dateFormatter) }
    }

    @TypeConverter
    fun fromZonedDateTime(value: ZonedDateTime?): String? {
        return value?.format(dateTimeFormatter)
    }

    @TypeConverter
    fun toZonedDateTime(value: String?): ZonedDateTime? {
        return value?.let { ZonedDateTime.parse(it, dateTimeFormatter) }
    }
}
