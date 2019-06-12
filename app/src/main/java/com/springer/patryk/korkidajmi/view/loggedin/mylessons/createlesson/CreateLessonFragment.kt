package com.springer.patryk.korkidajmi.view.loggedin.mylessons.createlesson

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.springer.patryk.korkidajmi.Application
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.PlaceEnum
import com.springer.patryk.korkidajmi.model.extensions.getFloatValue
import com.springer.patryk.korkidajmi.model.extensions.setUnderLineText
import com.springer.patryk.korkidajmi.model.network.endpoints.OffersApi
import kotlinx.android.synthetic.main.fragment_create_leasson.*
import pl.mauto24.app.view.base.BaseFragment
import java.util.*

class CreateLessonFragment : BaseFragment(), CreateLessonContract.View {
	override val layoutResId: Int
		get() = R.layout.fragment_create_leasson
	override val mTitleStringId: Int
		get() = R.string.page_title_new_lesson
	private val mOfferApi: OffersApi by lazy {
		(mBaseActivity.application as Application).getRetrofitInstance()
				.create(OffersApi::class.java)
	}
	private val mPresenter: CreateLessonContract.Presenter  by lazy {
		CreateLessonPresenter(this, mOfferApi)
	}

	companion object {
		const val START_DATE_KEY: String = "startDate"
		const val REQUEST_CODE_KEY: String = "requestCode"
		const val LESSON_DATE_KEY: String = "lessonDate"
		const val REQUEST_CODE: Int = 81362
		fun newInstance(calendar: Calendar?, parent: BaseFragment): CreateLessonFragment {
			val fragment = CreateLessonFragment()
			val bundle = Bundle()
			bundle.putSerializable(START_DATE_KEY, calendar)
			fragment.setTargetFragment(parent, REQUEST_CODE)
			fragment.arguments = bundle
			return fragment
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		tv_create_lesson_date.setOnClickListener {
			mPresenter.onDateClicked()
		}
		tv_create_lesson_time_from.setOnClickListener { mPresenter.onTimeFromClicked() }
		tv_create_lesson_time_to.setOnClickListener { mPresenter.onTimeToClicked() }
		btn_create_lesson_submit.setOnClickListener {
			val price: Float = et_create_lesson_price.getFloatValue()
			val place: PlaceEnum =
				if (cb_create_lesson_place.isChecked) PlaceEnum.STUDENT else PlaceEnum.TUTOR
			mPresenter.onSubmitClicked(price, place, cb_create_lesson_cyclic.isChecked)
		}
	}

	override fun onResume() {
		super.onResume()
		val calendar: Calendar =
			arguments?.get(START_DATE_KEY) as? Calendar ?: Calendar.getInstance()
		mPresenter.refreshDate(calendar)
	}

	override fun showDate(date: String) {
		tv_create_lesson_date.setUnderLineText(date)
	}

	override fun showTimeFrom(time: String) {
		tv_create_lesson_time_from.setUnderLineText(time)
	}

	override fun showTimeTo(time: String) {
		tv_create_lesson_time_to.setUnderLineText(time)
	}

	override fun showDatePicker(calendar: Calendar) {
		DatePickerDialog(context,
				DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
					val newDate: Calendar = Calendar.getInstance()
					newDate.set(Calendar.YEAR, year)
					newDate.set(Calendar.MONTH, month)
					newDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
					mPresenter.onNewDateSelected(newDate)
				}, calendar[Calendar.YEAR], calendar[Calendar.MONTH],
				calendar[Calendar.DAY_OF_MONTH]).show()
	}

	override fun showTimeFromPicker(calendar: Calendar) {
		TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, hour, minutes ->
			calendar.set(Calendar.HOUR_OF_DAY, hour)
			calendar.set(Calendar.MINUTE, minutes)
			mPresenter.onNewTimeFromSelected(calendar)
		}, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], true).show()
	}

	override fun showTimeToPicker(calendar: Calendar) {
		TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, hour, minutes ->
			calendar.set(Calendar.HOUR_OF_DAY, hour)
			calendar.set(Calendar.MINUTE, minutes)
			mPresenter.onNewTimeToSelected(calendar)
		}, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], true).show()
	}

	override fun onSuccessAdd(date: Date) {
		val intent: Intent = Intent()
		intent.putExtra(LESSON_DATE_KEY, date)
		targetFragment?.onActivityResult(REQUEST_CODE, Activity.RESULT_OK, intent)
		mBaseActivity.onBackPressed()
	}
}