package com.payroll.twogrowhr.retrofit

import com.google.gson.Gson
import org.json.JSONObject

data class ResponseError(val message:String, val errorCode:Int)

inline fun <reified T> JSONObject.toModel(): T? = this.run {
    try {
        Gson().fromJson<T>(this.toString(), T::class.java)
    }
    catch (e:java.lang.Exception){ e.printStackTrace(); null }
}


inline fun <reified T> String.toModel(): T? = this.run {
    try {
        JSONObject(this).toModel<T>()
    }
    catch (e:java.lang.Exception){  null }
}