package com.dicoding.auliarosyida.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.AUTHORITY
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion.CONTENT_URI
import com.dicoding.auliarosyida.githubuser.db.UserDbContract.UserDbColumns.Companion.TABLE_NAME
import com.dicoding.auliarosyida.githubuser.db.UserGithubHelper
import com.loopj.android.http.AsyncHttpClient

class FavContentProvider : ContentProvider() {

    companion object {
        private const val FAV = 1
        private const val FAV_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userGithubHelper: UserGithubHelper

        init {
            // content://com.dicoding.auliarosyida.githubuser/user
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV)

            // content://com.dicoding.auliarosyida.githubuser/user/uname
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAV_ID)
        }
    }

    override fun onCreate(): Boolean {
        userGithubHelper = UserGithubHelper.getInstance(context as Context)
        userGithubHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        AsyncHttpClient.log.d("CONTENT PROVIDER - QUERY", "masuk query ${sUriMatcher.match(uri)}")
        return when (sUriMatcher.match(uri)) {
            FAV -> userGithubHelper.queryAll()
            FAV_ID -> userGithubHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contValues: ContentValues?): Uri? {
        val added: Long = when (FAV) {
            sUriMatcher.match(uri) -> userGithubHelper.insert(contValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> userGithubHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun update(uri: Uri, contValues: ContentValues?, string: String?, strings: Array<out String>?): Int {
        val updated: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> userGithubHelper.update(uri.lastPathSegment.toString(),contValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

}