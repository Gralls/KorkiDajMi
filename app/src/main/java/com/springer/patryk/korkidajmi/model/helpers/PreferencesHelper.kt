package com.springer.patryk.korkidajmi.model.helpers

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.springer.patryk.korkidajmi.model.User

object PreferencesHelper {
	const val NOT_SET_LONG: Long = -1
	const val NOT_SET_STRING: String = ""
	private const val PREF_NAME: String = "korkidajmipref"
	private const val KEY_USER: String = "user"

	private fun getEditor(context: Context): SharedPreferences.Editor =
		getPreferences(context).edit()

	private fun getPreferences(context: Context): SharedPreferences =
		context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

	fun setUserInfo(context: Context, userInfo: User?) {
		getEditor(context).putString(KEY_USER, Gson().toJson(userInfo)).commit()
	}

	fun getUserInfo(context: Context): User? {
		val userString: String = getPreferences(context).getString(KEY_USER, NOT_SET_STRING)
		if (userString.isEmpty()) {
			return null
		}
		return Gson().fromJson(userString, User::class.java)
	}
}

