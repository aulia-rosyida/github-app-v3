package com.dicoding.auliarosyida.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion.COL_COMPANY
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion.COL_FOLLOWER
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion.COL_FOLLOWING
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion.COL_LOCATION
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion.COL_NAME
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion.COL_PHOTO
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion.COL_REPOSITORY
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion.COL_USERNAME
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion.TABLE_NAME
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion._ID

class UserDbHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, DbCursorFactory(), DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "githubuser"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " ($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $COL_USERNAME TEXT NOT NULL," +
                " $COL_NAME TEXT," +
                " $COL_LOCATION TEXT," +
                " $COL_REPOSITORY TEXT," +
                " $COL_COMPANY TEXT," +
                " $COL_FOLLOWER TEXT," +
                " $COL_FOLLOWING TEXT," +
                " $COL_PHOTO TEXT NOT NULL)"
    }

    override fun onCreate(sqlDB: SQLiteDatabase?) {
        sqlDB?.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(sqlDB: SQLiteDatabase?, versionOld: Int, versionNew: Int) {
        sqlDB?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(sqlDB)
    }

}