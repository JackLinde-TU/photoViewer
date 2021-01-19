package com.example.photoviewer

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.BitmapFactory
import java.io.File

const val DATABASE_NAME = "photoDB"
const val TABLE_NAME = "photos"
const val COL_ID = "id"
const val COL_PHOTO = "img"
const val COL_DESC = "description"

class DatabaseHandler(private var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_PHOTO + " VARCHAR(256) NOT NULL,"+
                COL_DESC + " TEXT NULL)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(img: File, desc: String) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_PHOTO, img.toString())
        cv.put(COL_DESC, desc)
        db.insert(TABLE_NAME, null, cv)

        db.close()
    }

    fun readData() : MutableList<Photo> {
        val list : MutableList<Photo> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val photo = Photo()
                photo.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                photo.image = BitmapFactory.decodeFile(result.getString(result.getColumnIndex(COL_PHOTO)))
                photo.desc = result.getString(result.getColumnIndex(COL_DESC)).toString()
                list.add(photo)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }


    fun deleteData() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
}