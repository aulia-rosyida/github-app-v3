package com.dicoding.auliarosyida.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

object UserDbContract {

    const val AUTHORITY = "com.dicoding.auliarosyida.githubuser"
    const val SCHEME = "content"

    internal class UserDbColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user_favorite"
            const val _ID = "_id"
            const val COL_USERNAME = "username"
            const val COL_NAME = "name"
            const val COL_LOCATION = "location"
            const val COL_REPOSITORY = "public_repos"
            const val COL_COMPANY = "company"
            const val COL_FOLLOWER = "followers"
            const val COL_FOLLOWING = "following"
            const val COL_PHOTO = "avatar_url"
            const val COL_IS_FAVORITED = "is_favorited"

            // untuk membuat URI content://com.dicoding.auliarosyida.githubuser/user
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}