package com.springer.patryk.korkidajmi.view.loggedin.model

import com.google.gson.annotations.SerializedName
import com.springer.patryk.korkidajmi.model.NamedObject
import com.springer.patryk.korkidajmi.model.Offer
import com.springer.patryk.korkidajmi.model.User

data class Submission(
		@SerializedName("id")
		val mId: Int = 0,
		@SerializedName("subject")
		val mSubject: NamedObject,
		@SerializedName("level")
		val mLevel: NamedObject,
		@SerializedName("offer")
		val mOffer: Offer = Offer(),
		@SerializedName("student")
		val mStudent: User = User(),
		@SerializedName("status")
		val mStatus: SubmissionStatus = SubmissionStatus.NEW)