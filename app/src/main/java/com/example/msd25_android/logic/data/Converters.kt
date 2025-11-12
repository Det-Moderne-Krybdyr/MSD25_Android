package com.example.msd25_android.logic.data
import android.icu.math.BigDecimal
import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class Converters {

    @TypeConverter
    fun fromKotlinInstant(value: Instant?): Long? = value?.toEpochMilliseconds()

    @TypeConverter
    fun toKotlinInstant(value: Long?): kotlinx.datetime.Instant? = value?.let { kotlinx.datetime.Instant.fromEpochMilliseconds(it) }

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    enum class NotificationType(val value: String) {
        EXPENSE("expense"),
        GROUP("group"),
        FRIEND("friend"),
    }

    @TypeConverter
    fun toNotificationType(value: String?): NotificationType? {
        when(value) {
            "expense" -> NotificationType.EXPENSE
            "group" -> NotificationType.GROUP
            "friend" -> NotificationType.FRIEND
        }
        return null
    }

    @TypeConverter
    fun fromNotificationType(value: NotificationType?): String? {
        return value?.value
    }
}