package com.payroll.twogrowhr


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.json.JSONObject


@Composable
fun getLoginDetails(): JSONObject? {
    val context = androidx.compose.ui.platform.LocalContext.current

    // Set up a MutableState to hold the JSONObject
    val jsonObjectState = remember { mutableStateOf<JSONObject?>(null) }

    // Use LaunchedEffect to call GetUserDetails asynchronously and update the jsonObjectState
    LaunchedEffect(Unit)
    {
        val jsonObject = Constant.getUserLoginDetails(context)
        Log.d("GetData", "JsonObject from SQLite Before assigning to jsonObjectState : $jsonObject")
        jsonObjectState.value = jsonObject
    }
    Log.d("GetData", "JsonObject from SQLite : $jsonObjectState")

    // Access the jsonObject value from jsonObjectState
    return jsonObjectState.value
}

@Composable
fun getEmployeeDetails(): JSONObject? {
    val context = LocalContext.current

    val jsonObjectState = remember { mutableStateOf<JSONObject?>(null) }

    LaunchedEffect(Unit)
    {
        val jsonObject = Constant.getEmployeeMasterDetails(context)
        Log.d("GetData", "JsonObject from SQLite Before assigning to jsonObjectState : $jsonObject")
        jsonObjectState.value = jsonObject
    }
    Log.d("GetData", "JsonObject from SQLite : $jsonObjectState")

    return jsonObjectState.value
}