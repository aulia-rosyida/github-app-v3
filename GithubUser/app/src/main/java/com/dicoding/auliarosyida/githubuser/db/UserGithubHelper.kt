package com.dicoding.auliarosyida.githubuser.db


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion.TABLE_NAME
import java.sql.SQLException
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion._ID

class UserGithubHelper (context: Context) {

    companion object {
        private const val DB_TABLE = TABLE_NAME
        private lateinit var userDbHelper: UserDbHelper
        private lateinit var db: SQLiteDatabase

        //untuk inisiasi db
        private var INSTANCE: UserGithubHelper? = null
        fun getInstance(context: Context): UserGithubHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserGithubHelper(context)
            }
    }

    init {
        userDbHelper = UserDbHelper(context)
    }

    //buka dan tutup koneksi ke database
    @Throws(SQLException::class)
    fun open() {
        db = userDbHelper.writableDatabase
    }
    fun close() {
        userDbHelper.close()
        if (db.isOpen)
            db.close()
    }

    /**
     * metode untuk melakukan proses CRUD
     * */
    fun queryAll(): Cursor {
        return db.query(
            DB_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC")
    }
    /**
     * ambil data dengan id tertentu
     * */
    fun queryById(id: String): Cursor {
        return db.query(
            DB_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    // Untuk simpan data
    fun insert(contValues: ContentValues?): Long {
        return db.insert(DB_TABLE, null, contValues)
    }

    // Untuk perbaharui data
    fun update(id: String, contValues: ContentValues?): Int {
        return db.update(DB_TABLE, contValues, "$_ID = ?", arrayOf(id))
    }

    // Untuk hapus data
    fun deleteById(id: String): Int {
        return db.delete(DB_TABLE, "$_ID = '$id'", null)
    }
}