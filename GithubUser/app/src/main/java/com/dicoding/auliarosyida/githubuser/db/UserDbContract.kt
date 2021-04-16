package com.dicoding.auliarosyida.githubuser.db

import android.provider.BaseColumns

internal class UserDbContract {
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
        }
    }
}