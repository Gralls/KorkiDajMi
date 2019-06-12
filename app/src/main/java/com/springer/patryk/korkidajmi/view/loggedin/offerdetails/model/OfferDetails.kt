package com.springer.patryk.korkidajmi.view.loggedin.offerdetails.model

import com.google.gson.annotations.SerializedName
import com.springer.patryk.korkidajmi.model.Offer
import com.springer.patryk.korkidajmi.model.PlaceEnum
import com.springer.patryk.korkidajmi.model.User

data class OfferDetails(
		@SerializedName("date_from")
		val mDateFrom: String = "",
		@SerializedName("date_to")
		val mDateTo: String = "",
		@SerializedName("place")
		val mPlace: PlaceEnum = PlaceEnum.STUDENT,
		@SerializedName("price")
		val mPrice: Float = 0f,
		@SerializedName("tutor")
		val mTutor: User = User(),
		@SerializedName("otherOffers")
		val mOtherOffers: List<Offer> = emptyList(),
		@SerializedName("id")
		var mId: Int = 0)
