package com.me.data.datasource

import android.content.Context
import com.me.domain.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import androidx.core.content.edit

class Storage @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences =
        context.getSharedPreferences(Constants.USER_MEMORY, Context.MODE_PRIVATE)

    fun setString(key: String, value: String) {
        sharedPreferences.edit {
            putString(key, value)
            commit()
        }
    }

    fun getString(key: String): String? = sharedPreferences.getString(key, null)
}