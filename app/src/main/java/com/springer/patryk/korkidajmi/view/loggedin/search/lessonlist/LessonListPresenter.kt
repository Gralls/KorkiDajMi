package com.springer.patryk.korkidajmi.view.loggedin.search.lessonlist

import com.springer.patryk.korkidajmi.model.network.NetResult
import com.springer.patryk.korkidajmi.model.network.endpoints.OffersApi
import com.springer.patryk.korkidajmi.view.base.BasePresenterImpl
import com.springer.patryk.korkidajmi.view.loggedin.search.lessonlist.adapters.LessonListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LessonListPresenter(private val mView: LessonListContract.View,
						  private val mOffersApi: OffersApi) : BasePresenterImpl(mView),
		LessonListContract.Presenter {

	private val mAdapter: LessonListAdapter by lazy { LessonListAdapter(this) }
	private var mSubjectId: Int = -1
	private var mLevelId: Int = -1
	private var mCalendar = Calendar.getInstance()
	private var mMaxPrice: Int? = null
	private var mMinPrice: Int? = null
	private val sdfDate = SimpleDateFormat("dd-MM-yyyy")
	private val sdfQueryDate = SimpleDateFormat("yyyy-MM-dd")
	override fun onFilterCloseClicked() {
		mView.showFilterPanel(false)
	}

	override fun onFilterExpandClicked() {
		mView.updateFilterView(sdfDate.format(mCalendar.time), mMaxPrice, mMinPrice)
		mView.showFilterPanel(true)
	}

	override fun getAdapter(): LessonListAdapter = mAdapter

	override fun getLessonList() {
		GlobalScope.launch(Dispatchers.Main) {
			mView.getContext()?.let {
				val result = safeApiCall {
					mOffersApi.getOffers(mSubjectId, mLevelId, mMinPrice, mMaxPrice,
							sdfQueryDate.format(mCalendar.time)).await()
				}
				if (result is NetResult.Success) {
					if (result.data.isSuccessful) {
						result.data.body()?.forEach { offer ->
							val additionalPrice = offer.mTutor?.mSubjectsList?.find { userSubject ->
								userSubject.mLevel.mId == mLevelId && userSubject.mSubject.mId == mSubjectId
							}?.additionalPrice
							offer.mPrice += additionalPrice ?: 0f
						}
						mAdapter.setDataset(ArrayList(result.data.body()))
						mView.showNoLessons(result.data.body()?.isEmpty() ?: true)
					}
				}
			}
		}
	}

	override fun setSubjectAndLevel(subjectId: Int, levelId: Int) {
		mSubjectId = subjectId
		mLevelId = levelId
	}

	override fun onFilterDateClicked() {
		mView.showDatePicker(mCalendar)
	}

	override fun onDateSelected(calendar: Calendar) {
		mView.updateFilterView(sdfDate.format(calendar.time), mMaxPrice, mMinPrice)
	}

	override fun onFilterConfirmed(calendar: Calendar, minPrice: Int?, maxPrice: Int?) {
		mCalendar = calendar
		mMinPrice = if (minPrice == 0) null else minPrice
		mMaxPrice = if (maxPrice == 0) null else maxPrice
		getLessonList()
		mView.showFilterPanel(false)
	}

	override fun onOfferClicked(offerId: Int) {
		mView.showOfferDetails(offerId)
	}
}