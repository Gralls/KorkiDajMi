package com.springer.patryk.korkidajmi.model

import com.google.gson.annotations.SerializedName

data class NamedObject(
		@SerializedName("id")
		val mId: Int,
		@SerializedName("name")
		val mName: String) {

	override fun toString(): String {
		return mName
	}

	override fun equals(other: Any?): Boolean {
		return mId == (other as NamedObject).mId
	}
}