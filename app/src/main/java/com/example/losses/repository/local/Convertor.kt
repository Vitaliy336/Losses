package com.example.losses.repository.local

import android.text.TextUtils
import androidx.room.TypeConverter
import com.google.gson.Gson

class Convertor {

    private val gson: Gson by lazy {
        Gson()
    }

    @TypeConverter
    fun mapToJson(map: HashMap<String, Int>?): String {
        return if(map == null) {
            ""
        } else {
            return gson.toJson(map)
        }
    }

    @TypeConverter
    fun jsonToMap(json: String?): HashMap<String, Int> {
        return if(TextUtils.isEmpty(json)) {
            HashMap()
        } else {
            gson.fromJson(json, HashMap<String, Int>().javaClass)
        }
    }
}