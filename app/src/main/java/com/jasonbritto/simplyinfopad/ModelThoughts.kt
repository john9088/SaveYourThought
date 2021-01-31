package com.jasonbritto.simplyinfopad

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ModelThoughts(context:Context):SQLiteOpenHelper(context,"THOUGHTS_DB",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE THOUGHTS(THOUGHT_ID INTEGER PRIMARY KEY AUTOINCREMENT,THOUGHTS_NAME TEXT)")
        db?.execSQL("CREATE TABLE THOUGHTS_LIST(THOUGHTS_LIST_ID INTEGER PRIMARY KEY AUTOINCREMENT,THOUGHTS_DATA TEXT,THOUGHT_ID INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { TODO("Not yet implemented")
    }

    fun insertData(db: SQLiteDatabase?, tableName:String, columns: String, values: Array<String>){
        if(values.size > 1){
            var tempColumn:MutableList<String> = columns.split(',') as MutableList<String>
            Log.d("DB","INSERT INTO $tableName('${tempColumn[0]}','${tempColumn[1]}') VALUES('${values[0]}',${values[0]})")
            db?.execSQL("INSERT INTO $tableName('${tempColumn[0]}','${tempColumn[1]}') VALUES('${values[0]}',${values[1]})")
        }
        else{
            Log.d("DB","INSERT INTO $tableName('$columns') VALUES('$values')")
            db?.execSQL("INSERT INTO $tableName('$columns') VALUES('${values[0]}')")
        }
    }

    fun deleteData(db: SQLiteDatabase?,tableName:String,columns: String ,values:Int): Boolean {
        Log.d("DB","Delete Query: DELETE FROM $tableName WHERE $columns = $values")
        //db?.execSQL("DELETE FROM $tableName WHERE $columns = $values")
        Log.d("DB",values.toString())
        return db?.delete(tableName, columns + "=?", arrayOf(values.toString()))!! > 0;
    }

    fun updateData(db: SQLiteDatabase?,tableName:String,columns: String ,values:String,queryColumn:String,queryData:String){
        Log.d("DB","Update Query: UPDATE TABLE $tableName SET $columns = '$values' WHERE $queryColumn = $queryData")
        db?.execSQL("UPDATE $tableName SET $columns = '$values' WHERE $queryColumn = $queryData")
    }
}