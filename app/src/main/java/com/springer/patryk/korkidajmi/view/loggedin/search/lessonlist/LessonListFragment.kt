package com.springer.patryk.korkidajmi.view.loggedin.search.lessonlist

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.TranslateAnimation
import com.springer.patryk.korkidajmi.Application
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.extensions.getIntValue
import com.springer.patryk.korkidajmi.model.extensions.setVisibility
import com.springer.patryk.korkidajmi.model.network.endpoints.OffersApi
import com.springer.patryk.korkidajmi.view.loggedin.offerdetails.OfferDetailsFragment
import kotlinx.android.synthetic.main.fragment_lesson_list.*
import kotlinx.android.synthetic.main.layout_search_filters.*
import pl.mauto24.app.view.base.BaseFragment
import java.util.*

class LessonListFragment : BaseFragment(), LessonListContract.View {
	override val layoutResId: Int
		get() = R.layout.fragment_lesson_list
	override val mTitleStringId: Int
		get() = R.string.page_title_search_lessons
	private val mAnimDuration: Long = 250
	private val mZeroDelta: Float = 0f
	private val mPresenter: LessonListContract.Presenter by lazy {
		LessonListPresenter(this,
				(mBaseActivity.application as Application).getRetrofitInstance().create(
						OffersApi::class.java))
	}

	private val mCalendar: Calendar = Calendar.getInstance()

	companion object {
		const val SUBJECT_KEY = "subjectId"
		const val LEVEL_KEY = "levelId"
		fun newInstance(subjectId: Int, levelId: Int): LessonListFragment {
			val fragment = LessonListFragment()
			val bundle: Bundle = Bundle().apply {
				putInt(SUBJECT_KEY, subjectId)
				putInt(LEVEL_KEY, levelId)
			}
			fragment.arguments = bundle
			return fragment
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let { args ->
			mPresenter.setSubjectAndLevel(args[SUBJECT_KEY] as Int, args[LEVEL_KEY] as Int)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		btn_search_expand_filter.setOnClickListener { mPresenter.onFilterExpandClicked() }
		fl_search_filter_container.setOnClickListener { mPresenter.onFilterCloseClicked() }
		rv_search_lessons_list.adapter = mPresenter.getAdapter()
		rv_search_lessons_list.layoutManager = LinearLayoutManager(context)
		btn_search_date.setOnClickListener { mPresenter.onFilterDateClicked() }
		btn_search_filter.setOnClickListener {
			val minPrice: Int? = et_search_price_from.getIntValue()
			val maxPrice: Int? = et_search_price_to.getIntValue()
			mPresenter.onFilterConfirmed(mCalendar, minPrice, maxPrice)
		}
	}

	override fun onResume() {
		super.onResume()
		mPresenter.getLessonList()
	}

	override fun showFilterPanel(isVisible: Boolean) {
		if (isVisible) {
			showFilterPanel()
		} else {
			hideFilterPanel()
		}
	}

	private fun showFilterPanel() {
		fl_search_filter_container.visibility = View.VISIBLE
		btn_search_expand_filter.setVisibility(false)
		val animate =
			TranslateAnimation(mZeroDelta, mZeroDelta, -cl_search_filter_container.height.toFloat(),
					mZeroDelta)
		animate.duration = mAnimDuration
		animate.fillAfter = true
		fl_search_filter_container.startAnimation(animate)
	}

	private fun hideFilterPanel() {
		fl_search_filter_container.visibility = View.GONE
		btn_search_expand_filter.setVisibility(true)
		val animate = TranslateAnimation(mZeroDelta, mZeroDelta, mZeroDelta,
				-cl_search_filter_container.height.toFloat())
		animate.duration = mAnimDuration
		fl_search_filter_container.startAnimation(animate)
		mBaseActivity.hideKeyboard()
	}

	override fun showNoLessons(isVisible: Boolean) {
		iv_no_lessons.setVisibility(isVisible)
		tv_no_lessons.setVisibility(isVisible)
		rv_search_lessons_list.setVisibility(!isVisible)
	}

	override fun showDatePicker(calendar: Calendar) {
		val cal = calendar.clone() as Calendar
		DatePickerDialog(context, DatePickerDialog.OnDateSetListener { picker, year, month, day ->
			mCalendar.set(Calendar.YEAR, year)
			mCalendar.set(Calendar.MONTH, month)
			mCalendar.set(Calendar.DAY_OF_MONTH, day)
			mPresenter.onDateSelected(mCalendar)
		}, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]).show()
	}

	override fun updateFilterView(date: String, maxPrice: Int?, minPrice: Int?) {
		btn_search_date.text = date
		et_search_price_from.setText(minPrice?.toString())
		et_search_price_to.setText(maxPrice?.toString())
	}

	override fun showOfferDetails(offerId: Int) {
		mBaseActivity.setChildContent(OfferDetailsFragment.newInstance(offerId))
	}
}