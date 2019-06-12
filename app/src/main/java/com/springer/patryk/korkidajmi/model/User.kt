package com.springer.patryk.korkidajmi.model

import com.google.gson.annotations.SerializedName
import com.springer.patryk.korkidajmi.view.loggedin.model.UserSubject

data class User(
		@SerializedName("id")
		val mId: Int? = null,
		@SerializedName("email")
		val mEmail: String = "",
		@SerializedName("password")
		val mPassword: String = "",
		@SerializedName("firstName")
		var mFirstName: String = "",
		@SerializedName("lastName")
		var mLastName: String = "",
		@SerializedName("isCoach")
		val mIsCoach: Boolean = false,
		@SerializedName("subjectList")
		var mSubjectsList: ArrayList<UserSubject>? = null,
		@SerializedName("description")
		var mDescription: String? = null,
		@SerializedName("city")
		var mCity: String? = null,
		@SerializedName("address")
		var mAddress: String? = null,
		@SerializedName("photo")
		var mPhoto: String? = null)