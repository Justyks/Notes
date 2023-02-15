package com.example.notes.database

import android.provider.BaseColumns

class NotesContract {
    class AddingNote : BaseColumns{
        var _ID = BaseColumns._ID
        var COLUMN_TEXT = "text"
    }
}