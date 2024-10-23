package com.payroll.twogrowhr.Database


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

@Suppress("SameParameterValue")
class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{

    companion object
    {
        const val DATABASE_NAME = "my_database.db"
        const val DATABASE_VERSION = 1
//        var context: Context? = null

        //Login Details :-
        const val TABLE_LOGIN_DETAILS = "tbl_login"
        const val TLD_SLNO = "sln"
        const val TLD_LOGIN_VALUE = "login_values"

        //Employee Details
        const val TABLE_EMPLOYEE_DETAILS = "tbl_employee_master"
        const val TLD_SLNO_EMPLOYEE = "sln"
        const val TLD_EMPLOYEE_VALUE = "employee_master"


    }


//    private val myContext: Context = context

    override fun onCreate(db: SQLiteDatabase)
    {
        //Login Details
        val createTableLogin = "CREATE TABLE IF NOT EXISTS $TABLE_LOGIN_DETAILS($TLD_SLNO integer primary key,$TLD_LOGIN_VALUE text)"
        db.execSQL(createTableLogin)

        //Employee Details
        val createTableEmployee1 = "CREATE TABLE IF NOT EXISTS $TABLE_EMPLOYEE_DETAILS( $TLD_SLNO_EMPLOYEE integer primary key, $TLD_EMPLOYEE_VALUE text)"
        db.execSQL(createTableEmployee1)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LOGIN_DETAILS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EMPLOYEE_DETAILS")
        onCreate(db)
    }

    /*------------------------------------------------------------------------------------------------------------------------------------------------*/
    //Login Details :-
    fun createLogin(result: String?)
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
//        val toStore = Gson().toJson(loginResponse, LoginResponseModel::class.java)
        contentValues.put(TLD_LOGIN_VALUE, result)
        db.insert(TABLE_LOGIN_DETAILS, null, contentValues)
        db.close()
        Log.d("SQLite", "LOGIN TABLE CREATED")
    }

    fun getCountLoginSetUp(): Int
    {
        val db = this.writableDatabase
        val strSQL = "SELECT COUNT(*) FROM $TABLE_LOGIN_DETAILS"
        @SuppressLint("Recycle") val cursor = db.rawQuery(strSQL, null)
        cursor.moveToFirst()
        return if (cursor.getInt(0) > 0) 1 else 0
    }

    fun getUserLoginData(): Cursor?
    {
        val db = this.writableDatabase
        return db.rawQuery("SELECT $TLD_LOGIN_VALUE FROM $TABLE_LOGIN_DETAILS",null)
    }

    fun deleteUserLoginData()
    {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_LOGIN_DETAILS")
        db.close()
        Log.d("SQLite", "LOGIN TABLE DELETED")
    }

    /*------------------------------------------------------------------------------------------------------------------------------------------------*/
//    EMPLOYEE DETAILS
    fun createEmployeeMaster(Result: String)
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TLD_EMPLOYEE_VALUE, Result)
        db.insert(TABLE_EMPLOYEE_DETAILS, null, contentValues)
        db.close()
        Log.d("SQLite", "EMPLOYEE TABLE CREATED")
    }

    fun getEmployeeMasterSetup(): Int
    {
        val db = this.writableDatabase
        val strSQL = "SELECT COUNT(*) FROM $TABLE_EMPLOYEE_DETAILS"
        @SuppressLint("Recycle") val cursor = db.rawQuery(strSQL, null)
        cursor.moveToFirst()
        return if (cursor.getInt(0) > 0) 1 else 0
    }

    fun getEmployeeMaster(): Cursor?
    {
        val db = this.writableDatabase
        return db.rawQuery("SELECT $TLD_EMPLOYEE_VALUE FROM $TABLE_EMPLOYEE_DETAILS",null)
    }

    fun deleteEmployeeMaster()
    {
        val db = this.writableDatabase
        if (isTableExists(TABLE_EMPLOYEE_DETAILS, db)) {
            db.execSQL("DELETE FROM $TABLE_EMPLOYEE_DETAILS")
            db.close()
            Log.d("SQLite", "EMPLOYEE TABLE DELETED")
        } else {
            Log.d("SQLite", "EMPLOYEE TABLE DOES NOT EXIST")
        }
    }

    private fun isTableExists(tableName: String, db: SQLiteDatabase): Boolean
    {
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='$tableName'",null )
        val tableExists = cursor.moveToFirst()
        cursor.close()
        return tableExists
    }

    /*------------------------------------------------------------------------------------------------------------------------------------------------*/
}


