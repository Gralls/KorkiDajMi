package com.springer.patryk.korkidajmi.view.loggedin.mylessons.createlesson

import com.springer.patryk.korkidajmi.model.Offer
import com.springer.patryk.korkidajmi.model.PlaceEnum
import com.springer.patryk.korkidajmi.model.User
import com.springer.patryk.korkidajmi.model.helpers.PreferencesHelper
import com.springer.patryk.korkidajmi.model.network.NetResult
import com.springer.patryk.korkidajmi.model.network.endpoints.OffersApi
import com.springer.patryk.korkidajmi.view.base.BasePresenterImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateLessonPresenter(private val mView: CreateLessonContract.View,
							private val mOffersApi: OffersApi) : CreateLessonContract.Presenter,
		BasePresenterImpl(mView) {

	val visibleDateFormatter = SimpleDateFormat("dd-MM-yyyy")
	val serverDateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
	val visibleHourFormatter = SimpleDateFormat("HH:mm")
	val newOffer: Offer = Offer()
	var newTimeTo: Calendar? = null
	lateinit var mDate: Date
	lateinit var mCalendar: Calendar
	override fun refreshDate(calendar: Calendar) {
		mDate = calendar.time
		convertCalendarToDates(newTimeTo)
	}

	private fun convertCalendarToDates(newTimeTo: Calendar? = null) {
		mView.showDate(visibleDateFormatter.format(mDate))
		mView.showTimeFrom(visibleHourFormatter.format(mDate))
		newOffer.mDateFrom = serverDateFormatter.format(mDate)
		newTimeTo?.let {
			val calendar = Calendar.getInstance()
			calendar.time = mDate
			calendar.set(Calendar.HOUR_OF_DAY, it[Calendar.HOUR_OF_DAY])
			calendar.set(Calendar.MINUTE, it[Calendar.MINUTE])
			mDate = calendar.time
		}
		mView.showTimeTo(visibleHourFormatter.format(mDate))
		newOffer.mDateTo = serverDateFormatter.format(mDate)
	}

	override fun onDateClicked() {
		mView.showDatePicker(getDateAsCalendar())
	}

	override fun onTimeFromClicked() {
		mView.showTimeFromPicker(getDateAsCalendar())
	}

	override fun onTimeToClicked() {
		val calendar = getDateAsCalendar()
		newTimeTo?.let {
			calendar.set(Calendar.HOUR_OF_DAY, it[Calendar.HOUR_OF_DAY])
			calendar.set(Calendar.MINUTE, it[Calendar.MINUTE])
		}
		mView.showTimeToPicker(calendar)
	}

	override fun onNewDateSelected(calendar: Calendar) {
		mDate = calendar.time
		convertCalendarToDates()
	}

	override fun onNewTimeToSelected(calendar: Calendar) {
		newTimeTo = calendar.clone() as Calendar
		convertCalendarToDates(newTimeTo)
	}

	override fun onNewTimeFromSelected(calendar: Calendar) {
		mDate = calendar.time
		convertCalendarToDates()
	}

	override fun onSubmitClicked(price: Float, placeEnum: PlaceEnum, isCyclic: Boolean) {
		val dateFrom: Date = serverDateFormatter.parse(newOffer.mDateFrom)
		val dateTo: Date = serverDateFormatter.parse(newOffer.mDateTo)
		when {
			dateTo <= dateFrom -> mView.showSnackbar("Godzina końcowa powinna być większa od początkowej")
			price == 0f -> mView.showSnackbar("Stawka godzinowa jest pusta")
			else -> GlobalScope.launch(Dispatchers.Main) {
				mView.getContext()?.let {
					val userId: Int = PreferencesHelper.getUserInfo(it)?.mId ?: return@launch
					newOffer.mPrice = price
					newOffer.mPlace = placeEnum
					newOffer.mIsCyclical = isCyclic
					newOffer.mTutor = User(mId = userId)
					val result = safeApiCall { mOffersApi.addNewOffer(newOffer).await() }
					if (result is NetResult.Success) {
						if (result.data.isSuccessful) {
							mView.onSuccessAdd(mDate)
						}
					}
				}

			}
		}
	}

	private fun getDateAsCalendar(): Calendar {
		val calendar: Calendar = Calendar.getInstance()
		calendar.time = mDate
		return calendar
	}
}