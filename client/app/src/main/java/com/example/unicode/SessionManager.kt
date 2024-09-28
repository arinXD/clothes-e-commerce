package com.example.unicode

import android.content.Context
import android.content.SharedPreferences

class SessionManager {
    var pref: SharedPreferences
    var edior: SharedPreferences.Editor
    var context: Context
    var PRIVATE_MODE: Int = 0

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        edior = pref.edit()
    }
    companion object {
//        "status": 1,
//        "user_id": 2,
//        "user_type": "user",
//        "user_name": "oat",
//        "email": "oat@gmail.com",
//        "gender": "1234",
//        "birthday": "2022-08-31T17:00:00.000Z"
        val PREF_NAME: String = "SessionDemo"
        val IS_LOGIN: String = "isLogin"
        val KEY_ID: String = "id"
        val KEY_TYPE: String = "type"
        val KEY_NAME: String = "username"
        val KEY_EMAIL: String = "email"
        val KEY_GENDER: String = "gender"
        val KEY_BDAY: String = "bDay"
    }
    fun createLoginSession(
        id: String,
        type: String,
        username: String,
        email: String,
        gender: String,
        bDay: String
    ) {
        edior.putBoolean(IS_LOGIN, true)
        edior.putString(KEY_ID, id)
        edior.putString(KEY_TYPE, type)
        edior.putString(KEY_NAME, username)
        edior.putString(KEY_EMAIL, email)
        edior.putString(KEY_GENDER, gender)
        edior.putString(KEY_BDAY, bDay)
        edior.commit()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }
    fun getType(): String?{
        return pref.getString(KEY_TYPE, "user")
    }
}