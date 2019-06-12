package com.springer.patryk.korkidajmi.view.loggedin.submission

import com.google.gson.annotations.SerializedName
import com.springer.patryk.korkidajmi.view.loggedin.model.SubmissionStatus

data class NewStatus(
		@SerializedName("newStatus")
		val mStatus: SubmissionStatus)