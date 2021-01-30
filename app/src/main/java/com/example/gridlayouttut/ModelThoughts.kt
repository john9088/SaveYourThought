package com.example.gridlayouttut

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ModelThoughts(context:Context):SQLiteOpenHelper(context,"THOUGHTS_DB",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE THOUGHTS(THOUGHT_ID INTEGER PRIMARY KEY AUTOINCREMENT,THOUGHTS_NAME TEXT)")
        db?.execSQL("CREATE TABLE THOUGHTS_LIST(THOUGHTS_LIST_ID INTEGER PRIMARY KEY AUTOINCREMENT,THOUGHTS_DATA TEXT,THOUGHT_ID INTEGER)")

        //db?.execSQL("INSERT INTO THOUGHTS(THOUGHTS_NAME) VALUES('HAPPY ME')")
        db?.execSQL("INSERT INTO THOUGHTS_LIST(THOUGHTS_DATA,THOUGHT_ID) VALUES('Happiness is the best makeup',1)")
        db?.execSQL("INSERT INTO THOUGHTS_LIST(THOUGHTS_DATA,THOUGHT_ID) VALUES('Roll with the punches and enjoy every minute of it',1)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { TODO("Not yet implemented")
    }

    fun insertData(db: SQLiteDatabase?,tableName:String,columns: String ,values:String){
        if(values.contains(',')){
            var tempValues:MutableList<String> = values.split(',') as MutableList<String>
            var tempColumn:MutableList<String> = columns.split(',') as MutableList<String>

            db?.execSQL("INSERT INTO $tableName('${tempColumn[0]}','${tempColumn[1]}') VALUES('${tempValues[0]}',${tempValues[1]})")
        }
        else
            db?.execSQL("INSERT INTO $tableName('$columns') VALUES('$values')")
    }

    fun deleteData(db: SQLiteDatabase?,tableName:String,columns: String ,values:Int): Boolean {
        Log.d("MAIN","Delete Query: DELETE FROM $tableName WHERE $columns = $values")
        //db?.execSQL("DELETE FROM $tableName WHERE $columns = $values")
        Log.d("From Db",values.toString())
        return db?.delete(tableName, columns + "=?", arrayOf(values.toString()))!! > 0;
    }

    fun updateData(db: SQLiteDatabase?,tableName:String,columns: String ,values:Int,queryColumn:String,queryData:String){
        Log.d("MAIN","Update Query: UPDATE TABLE $tableName SET $columns = $values WHERE $queryColumn = '$queryData'")
        db?.execSQL("UPDATE $tableName SET $columns = $values WHERE $queryColumn = '$queryData'")
    }
}