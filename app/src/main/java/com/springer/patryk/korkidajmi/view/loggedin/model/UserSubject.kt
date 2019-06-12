package com.springer.patryk.korkidajmi.view.loggedin.model

import com.google.gson.annotations.SerializedName
import com.springer.patryk.korkidajmi.model.NamedObject

data class UserSubject(
		@SerializedName("subject")
		var mSubject: NamedObject,
		@SerializedName("level")
		var mLevel: NamedObject,
		@SerializedName("additionalPrice")
		var additionalPrice: Float = 0f) {

	override fun toString(): String {
		return "${mSubject.mName} ${mLevel.mName}"
	}
}