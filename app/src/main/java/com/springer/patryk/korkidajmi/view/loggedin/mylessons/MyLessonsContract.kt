package com.springer.patryk.korkidajmi.view.loggedin.mylessons

import com.alamkanak.weekview.WeekViewEvent
import com.springer.patryk.korkidajmi.view.base.BasePresenter
import com.springer.patryk.korkidajmi.view.loggedin.mylessons.adapters.MyLessonsListAdapter
import pl.mauto24.app.view.base.BaseFragmentView
import java.util.*

interface MyLessonsContract {
	interface View : BaseFragmentView {
		fun refreshOffers()
		fun showCalendarView()
		fun showListView()

	}

	interface Presenter : BasePresenter {
		fun refreshOffers()
		fun getOffersList(month: Int, year: Int): List<WeekViewEvent>
		fun getAdapter(): MyLessonsListAdapter
		fun onListViewSelected()
		fun onCalendarViewSelected()
		fun getIsUserTutor(): Boolean
		fun setCurrentDate(date: Date)
		fun getCurrentDate(): Date
	}
}