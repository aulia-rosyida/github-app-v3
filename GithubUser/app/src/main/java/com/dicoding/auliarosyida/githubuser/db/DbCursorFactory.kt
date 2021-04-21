package com.dicoding.auliarosyida.githubuser.db

import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteCursorDriver
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQuery

class DbCursorFactory: SQLiteDatabase.CursorFactory {

    override fun newCursor(db: SQLiteDatabase?, driver: SQLiteCursorDriver?, editTable: String?, query: SQLiteQuery?): Cursor {
        return SQLiteCursor(driver, editTable, query)
    }
}