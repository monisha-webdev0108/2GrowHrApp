@file:Suppress("DEPRECATION")

package com.payroll.twogrowhr

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit

object SharedPreferenceManager
{
    private const val IS_LOGGED_IN_KEY = "is_logged_in"

    private const val PRIVACY_POLICY_KEY = "privacy_policy_in"

    private const val CHECK_IN_OUT_KEY = "checkInOutFlag"



    fun getPrivacyPolicy(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean(PRIVACY_POLICY_KEY, false)
    }

    fun setPrivacyPolicy(context: Context, privacyPolicy: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit {
            putBoolean(PRIVACY_POLICY_KEY, privacyPolicy)
        }
    }

    fun isLoggedIn(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean(IS_LOGGED_IN_KEY, false)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit {
            putBoolean(IS_LOGGED_IN_KEY, isLoggedIn)
        }
    }

    fun getCheckInOut(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(CHECK_IN_OUT_KEY, "")
    }

    fun setCheckInOut(context: Context, checkInOutFlag: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit {
            putString(CHECK_IN_OUT_KEY, checkInOutFlag)
        }
    }
}