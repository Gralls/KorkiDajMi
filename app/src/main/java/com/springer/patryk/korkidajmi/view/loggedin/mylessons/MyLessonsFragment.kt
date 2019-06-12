package com.springer.patryk.korkidajmi.view.loggedin.mylessons

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alamkanak.weekview.DateTimeInterpreter
import com.springer.patryk.korkidajmi.Application
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.extensions.setVisibility
import com.springer.patryk.korkidajmi.model.network.endpoints.OffersApi
import com.springer.patryk.korkidajmi.view.loggedin.LoggedInActivity
import com.springer.patryk.korkidajmi.view.loggedin.mylessons.createlesson.CreateLessonFragment
import kotlinx.android.synthetic.main.fragment_my_lessons.*
import pl.mauto24.app.view.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class MyLessonsFragment : BaseFragment(), MyLessonsContract.View {
	override val layoutResId: Int
		get() = R.layout.fragment_my_lessons
	override val mTitleStringId: Int
		get() = R.string.page_title_my_lessons
	private val mOfferApi: OffersApi by lazy {
		(mBaseActivity.application as Application).getRetrofitInstance()
				.create(OffersApi::class.java)
	}
	private var mNotificationView: View? = null

	public val mPresenter: MyLessonsContract.Presenter by lazy {
		MyLessonsPresenter(this, mOfferApi,
				ContextCompat.getColor(requireContext(), R.color.freeEventColor),
				ContextCompat.getColor(requireContext(), R.color.occupiedEventColor))
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(activity as LoggedInActivity).setToolbarTitle(R.string.page_title_my_lessons)

		wv_my_lessons_calendar.setMonthChangeListener { newYear, newMonth ->
			val events = mPresenter.getOffersList(newMonth, newYear)
			events
		}
		wv_my_lessons_calendar.setEmptyViewLongPressListener {
			if (mPresenter.getIsUserTutor()) {
				mBaseActivity.addChildContent(CreateLessonFragment.newInstance(it, this))
			}
		}

		wv_my_lessons_calendar.setOnEventClickListener { event, _ ->
			showSnackbar("Click event id: ${event.id}")
		}

		wv_my_lessons_calendar.setEventLongPressListener { event, _ ->
			showSnackbar("Hold event id: ${event.id}")
		}
		val dateTimeInterpreter = wv_my_lessons_calendar.dateTimeInterpreter
		wv_my_lessons_calendar.dateTimeInterpreter = object : DateTimeInterpreter {
			override fun interpretTime(hour: Int, minutes: Int): String {
				return dateTimeInterpreter.interpretTime(hour, minutes)
			}

			override fun interpretDate(date: Calendar?): String {
				val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
				var weekday = weekdayNameFormat.format(date?.time)
				val format = SimpleDateFormat(" d/M", Locale.getDefault())

				return weekday.toUpperCase() + format.format(date?.time)
			}
		}
		rv_my_lessons_list.adapter = mPresenter.getAdapter()
		rv_my_lessons_list.layoutManager = LinearLayoutManager(context)
		mPresenter.refreshOffers()
		fab_my_lessons_create.setVisibility(mPresenter.getIsUserTutor())
		fab_my_lessons_create.setOnClickListener {
			mBaseActivity.addChildContent(CreateLessonFragment.newInstance(null, this))
		}
	}

	override fun onResume() {
		super.onResume()
		mBaseActivity.invalidateOptionsMenu()
	}

	override fun refreshOffers() {
		wv_my_lessons_calendar.notifyDatasetChanged()
	}

	override fun showCalendarView() {
		rv_my_lessons_list.setVisibility(false)
		wv_my_lessons_calendar.setVisibility(true)
		fab_my_lessons_create.setVisibility(false)
	}

	override fun showListView() {
		rv_my_lessons_list.setVisibility(true)
		wv_my_lessons_calendar.setVisibility(false)
		fab_my_lessons_create.setVisibility(mPresenter.getIsUserTutor())
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		data?.let { intent ->
			when (requestCode) {
				CreateLessonFragment.REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
					val date = intent.extras[CreateLessonFragment.LESSON_DATE_KEY] as Date
					mPresenter.setCurrentDate(date)
					val calendar = Calendar.getInstance()
					calendar.time = mPresenter.getCurrentDate()
					wv_my_lessons_calendar.goToDate(calendar)
					wv_my_lessons_calendar.goToHour(calendar[Calendar.HOUR_OF_DAY].toDouble())
					mPresenter.refreshOffers()
				}
			}
		}
	}
}