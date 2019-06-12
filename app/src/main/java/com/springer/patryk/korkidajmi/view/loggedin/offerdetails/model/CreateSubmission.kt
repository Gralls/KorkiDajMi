package com.springer.patryk.korkidajmi.view.loggedin.offerdetails.model

import com.google.gson.annotations.SerializedName

data class CreateSubmission(
		@SerializedName("subject")
		val mSubjectId: Int = 0,
		@SerializedName("level")
		val mLevelId: Int = 0,
		@SerializedName("student")
		val mStudentId: Int = 0)