package com.springer.patryk.korkidajmi.model

import android.support.annotation.ColorRes
import com.alamkanak.weekview.WeekViewEvent
import com.google.gson.annotations.SerializedName
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.extensions.formatHourPrice
import java.text.SimpleDateFormat
import java.util.*

data class Offer(
		@SerializedName("date_from")
		var mDateFrom: String = "",
		@SerializedName("date_to")
		var mDateTo: String = "",
		@SerializedName("is_cyclical")
		var mIsCyclical: Boolean = false,
		@SerializedName("place")
		var mPlace: PlaceEnum = PlaceEnum.STUDENT,
		@SerializedName("price")
		var mPrice: Float = 0f,
		@SerializedName("tutor")
		var mTutor: User? = null,
		@SerializedName("student")
		var mStudent: User? = null,
		@SerializedName("id")
		var mId: Int = 0) {

	val simpleDateFormater: SimpleDateFormat get() = SimpleDateFormat("yyyy-MM-dd HH:mm")
	val sdfHours get() = SimpleDateFormat("HH:mm")
	val sdfDate get() = SimpleDateFormat("dd-MM-yyyy")
	fun convertToWeekEvent(color: Int, address: String): WeekViewEvent {
		val startTime = Calendar.getInstance()
		startTime.time = simpleDateFormater.parse(mDateFrom)
		val endTime = Calendar.getInstance()
		endTime.time = simpleDateFormater.parse(mDateTo)
		val price: String = formatHourPrice(mPrice)
		WeekViewEvent()
		val weekViewEvent: WeekViewEvent = WeekViewEvent(mId.toString(), "$address\n",
				"${sdfHours.format(simpleDateFormater.parse(mDateFrom))}-${sdfHours.format(
						simpleDateFormater.parse(mDateTo))}\n$price", startTime, endTime)
		weekViewEvent.color = color
		return weekViewEvent
	}

	fun getAddress(isUserTutor: Boolean, isSubmission: Boolean = false): String = if (isUserTutor) {
		when {
			mStudent == null && !isSubmission -> "Wolny termin"
			mPlace == PlaceEnum.TUTOR -> "U mnie"
			else -> "${mStudent?.mAddress ?: ""} ${mStudent?.mCity ?: ""}"
		}
	} else {
		if (mPlace == PlaceEnum.STUDENT) {
			"U mnie"
		} else {
			"${mTutor?.mAddress ?: ""} ${mTutor?.mCity ?: ""}"
		}
	}

	fun getFromHour(): String = sdfHours.format(simpleDateFormater.parse(mDateFrom))
	fun getToHour(): String = sdfHours.format(simpleDateFormater.parse(mDateTo))
	fun getDate(): String = sdfDate.format(simpleDateFormater.parse(mDateFrom))
	@ColorRes
	fun getColor(): Int =
		if (mStudent == null) R.color.freeEventColor else R.color.occupiedEventColor

	fun dateIsNotPast(): Boolean {
		val date = sdfDate.parse(sdfDate.format(Date()))
		val offerDate = simpleDateFormater.parse(mDateFrom)
		return offerDate.after(date)
	}
}