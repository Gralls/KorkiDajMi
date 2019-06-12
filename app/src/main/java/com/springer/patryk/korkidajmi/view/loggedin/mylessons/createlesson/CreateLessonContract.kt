package com.springer.patryk.korkidajmi.view.loggedin.mylessons.createlesson

import com.springer.patryk.korkidajmi.model.PlaceEnum
import com.springer.patryk.korkidajmi.view.base.BasePresenter
import pl.mauto24.app.view.base.BaseFragmentView
import java.util.*

interface CreateLessonContract {
	interface View : BaseFragmentView {
		fun showDate(date: String)
		fun showTimeFrom(time: String)
		fun showTimeTo(time: String)
		fun showDatePicker(calendar: Calendar)
		fun showTimeFromPicker(calendar: Calendar)
		fun showTimeToPicker(calendar: Calendar)
		fun onSuccessAdd(date: Date)
	}

	interface Presenter : BasePresenter {
		fun refreshDate(calendar: Calendar)
		fun onDateClicked()
		fun onTimeFromClicked()
		fun onTimeToClicked()
		fun onNewDateSelected(calendar: Calendar)
		fun onNewTimeToSelected(calendar: Calendar)
		fun onNewTimeFromSelected(calendar: Calendar)
		fun onSubmitClicked(price: Float, placeEnum: PlaceEnum, isCyclic: Boolean)
	}
}