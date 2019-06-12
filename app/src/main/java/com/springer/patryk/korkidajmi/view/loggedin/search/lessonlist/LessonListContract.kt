package com.springer.patryk.korkidajmi.view.loggedin.search.lessonlist

import com.springer.patryk.korkidajmi.view.base.BasePresenter
import com.springer.patryk.korkidajmi.view.loggedin.search.lessonlist.adapters.LessonListAdapter
import pl.mauto24.app.view.base.BaseFragmentView
import java.util.*

interface LessonListContract {
	interface View : BaseFragmentView {
		fun showFilterPanel(isVisible: Boolean)
		fun showNoLessons(isVisible: Boolean)
		fun showDatePicker(calendar: Calendar)
		fun updateFilterView(date: String, maxPrice: Int?, minPrice: Int?)
		fun showOfferDetails(offerId: Int)
	}

	interface Presenter : BasePresenter {
		fun onFilterExpandClicked()
		fun onFilterCloseClicked()
		fun getAdapter(): LessonListAdapter
		fun getLessonList()
		fun setSubjectAndLevel(subjectId: Int, levelId: Int)
		fun onFilterDateClicked()
		fun onDateSelected(calendar: Calendar)
		fun onFilterConfirmed(calendar: Calendar, minPrice: Int?, maxPrice: Int?)
		fun onOfferClicked(offerId: Int)
	}
}