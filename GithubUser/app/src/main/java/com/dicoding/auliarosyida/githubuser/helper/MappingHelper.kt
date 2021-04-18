package com.dicoding.auliarosyida.githubuser.helper

import android.database.Cursor
import android.provider.ContactsContract
import com.dicoding.auliarosyida.githubuser.db.UserDbContract
import com.dicoding.auliarosyida.githubuser.entity.User
import java.lang.Boolean.getBoolean

object MappingHelper {

    fun mapCursorToArrayList(usersCursor: Cursor?): ArrayList<User> {
        val usersList = ArrayList<User>()
        usersCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(UserDbContract.UserDbColumns._ID))
                val uname = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_USERNAME))
                val name = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_NAME))
                val location = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_LOCATION))
                val repo = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_REPOSITORY))
                val company = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_COMPANY))
                val follower = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_FOLLOWER))
                val following = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_FOLLOWING))
                val photo = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_PHOTO))
                val isFavorited = getBoolean(UserDbContract.UserDbColumns.COL_IS_FAVORITED)
                usersList.add(User(id, uname, name, location, repo, company, follower, following, photo, isFavorited))
            }
        }
        return usersList
    }

    fun mapCursorToObject(notesCursor: Cursor?): User {
        var anUser = User()
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(UserDbContract.UserDbColumns._ID))
            val uname = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_USERNAME))
            val name = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_NAME))
            val location = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_LOCATION))
            val repo = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_REPOSITORY))
            val company = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_COMPANY))
            val follower = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_FOLLOWER))
            val following = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_FOLLOWING))
            val photo = getString(getColumnIndexOrThrow(UserDbContract.UserDbColumns.COL_PHOTO))
            val isFavorited = getBoolean(UserDbContract.UserDbColumns.COL_IS_FAVORITED)
            anUser = User(id, uname, name, location, repo, company, follower, following, photo, isFavorited)
        }
        return anUser
    }
}