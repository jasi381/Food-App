package com.jasmeet.food_app.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypesConverter {

    @TypeConverter
    fun fromAnyToString(attribute:Any?) :String{
        if (attribute == null)
            return ""
        return attribute.toString()
    }

    @TypeConverter
    fun fromStringToAny(attribute: String?):Any{
        if (attribute == null)
            return ""
        return attribute

    }
}