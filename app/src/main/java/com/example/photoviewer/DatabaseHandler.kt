package com.example.photoviewer

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

const val DATABASE_NAME = "photoDB"
const val TABLE_NAME = "photos"
const val COL_ID = "id"
const val COL_PHOTO = "photo"

class DatabaseHandler(private var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_PHOTO + " BLOB)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(user: User) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_PHOTO, user.name)
        val result = db.insert(TABLE_NAME, null, cv)
        if (result == (-1).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }
    /** Ook nog fucked */
//    fun readData() : MutableList<User> {
//        val list : MutableList<User> = ArrayList()
//
//        val db = this.readableDatabase
//        val query = "SELECT * FROM $TABLE_NAME"
//        val result = db.rawQuery(query, null)
//        if (result.moveToFirst()) {
//            do {
//                val user = User()
//                user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
//                user.photo = result.getString(result.getColumnIndex(COL_NAME))
//                list.add(user)
//            } while (result.moveToNext())
//        }
//
//        result.close()
//        db.close()
//        return list
//    }


    fun deleteData() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
}