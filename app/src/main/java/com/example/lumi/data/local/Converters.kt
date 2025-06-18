package com.example.lumi.data.local

import androidx.room.TypeConverter
import com.example.lumi.data.model.StatusType

class Converters {
    @TypeConverter
    fun fromStatusType(statusType: StatusType): String {
        return statusType.name
    }

    @TypeConverter
    fun toStatusType(statusType: String): StatusType {
        return StatusType.valueOf(statusType)
    }
}