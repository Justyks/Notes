package com.example.notes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor
import android.content.ContentValues
import java.util.Collections.list

class NotesDBHelper (context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {



    public override fun onCreate(db: SQLiteDatabase){
        val query = ("CREATE TABLE " + "notes" + " ("
                + "_ID" + " INTEGER PRIMARY KEY, " +
                TEXT_COL + " TEXT)")

        db.execSQL(query)
    }

    public override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + "notes")
        onCreate(db)
    }

    fun addNote(text: String): Long {
        val values = ContentValues()
        values.put(TEXT_COL,text)

        val db= this.writableDatabase
        val id =db.insert("notes",null,values)
        //db.close()
        return id
    }

    fun getNotes(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM notes",null)
    }

    fun editNote(text: String, newText: String){
        val values = ContentValues()
        values.put(TEXT_COL,newText)
        val db = this.writableDatabase
        db.update("notes", values,TEXT_COL +"=?", arrayOf(text))
    }

    fun deleteNote(text: String){
        val db = this.writableDatabase
        db.delete("notes", "$TEXT_COL = \"$text\"",null)
        //db.close()

    }

    companion object{
        private val DATABASE_NAME = "notes.db"
        private val DATABASE_VERSION = 1
        const val TEXT_COL = "text"
        const val ID_COL = "_ID"
    }
}