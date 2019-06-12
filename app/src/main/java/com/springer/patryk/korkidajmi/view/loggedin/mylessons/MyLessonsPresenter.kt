package com.springer.patryk.korkidajmi.view.loggedin.mylessons

import com.alamkanak.weekview.WeekViewEvent
import com.springer.patryk.korkidajmi.model.Offer
import com.springer.patryk.korkidajmi.model.helpers.PreferencesHelper
import com.springer.patryk.korkidajmi.model.network.NetResult
import com.springer.patryk.korkidajmi.model.network.endpoints.OffersApi
import com.springer.patryk.korkidajmi.view.base.BasePresenterImpl
import com.springer.patryk.korkidajmi.view.loggedin.mylessons.adapters.MyLessonsListAdapter
import com.springer.patryk.korkidajmi.view.loggedin.mylessons.model.ViewTypeEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyLessonsPresenter(private val mView: MyLessonsContract.View,
						 private val mOffersApi: OffersApi, private val mLightColor: Int,
						 private val mDarkColor: Int) : MyLessonsContract.Presenter,
		BasePresenterImpl(mView) {

	private val mSdfServerDate = SimpleDateFormat("yyyy-MM-dd HH:mm")
	private var mCurrentViewType: ViewTypeEnum = ViewTypeEnum.LIST_VIEW
	private val mOffers: ArrayList<Offer> = ArrayList()
	private val mEvent: ArrayList<WeekViewEvent> = ArrayList()
	private val mLessonsAdapter: MyLessonsListAdapter by lazy { MyLessonsListAdapter(this) }
	private var mIsUserTutor: Boolean = false
	private var mCurrentDate: Date = Date()

	init {
		mView.getContext()?.let {
			val user = PreferencesHelper.getUserInfo(it)
			mIsUserTutor = user?.mIsCoach ?: false
		}
	}

	override fun setCurrentDate(date: Date) {
		mCurrentDate = date
	}

	override fun getCurrentDate(): Date = mCurrentDate

	override fun refreshOffers() {
		GlobalScope.launch(Dispatchers.Main) {
			mView.getContext()?.let {
				val userId: Int = PreferencesHelper.getUserInfo(it)?.mId ?: return@launch
				val result = safeApiCall { mOffersApi.getUserOffers(userId).await() }
				if (result is NetResult.Success) {
					if (result.data.isSuccessful) {
						mOffers.clear()
						mOffers.addAll(result.data.body() ?: emptyList())
						mLessonsAdapter.setDataset(
								ArrayList(mOffers.filter { it.dateIsNotPast() }.sortedBy { offer ->
									mSdfServerDate.parse(offer.mDateFrom).time
								}))
						mView.refreshOffers()
					}
				}
			}

		}
	}

	override fun getIsUserTutor(): Boolean = mIsUserTutor

	override fun getOffersList(month: Int, year: Int): List<WeekViewEvent> {
		return mOffers.map { offer ->
			if (offer.mStudent == null) {
				offer.convertToWeekEvent(mLightColor, offer.getAddress(mIsUserTutor))
			} else {
				offer.convertToWeekEvent(mDarkColor, offer.getAddress(mIsUserTutor))
			}
		}.filter { event ->
			(event.startTime.get(Calendar.YEAR) == year && event.startTime.get(
					Calendar.MONTH) == month - 1) || (event.endTime.get(
					Calendar.YEAR) == year && event.endTime.get(Calendar.MONTH) == month - 1)
		}
	}

	override fun getAdapter(): MyLessonsListAdapter = mLessonsAdapter

	override fun onListViewSelected() {
		if (mCurrentViewType != ViewTypeEnum.LIST_VIEW) {
			mCurrentViewType = ViewTypeEnum.LIST_VIEW
			mView.showListView()
		}
	}

	override fun onCalendarViewSelected() {
		if (mCurrentViewType != ViewTypeEnum.CALENDAR_VIEW) {
			mCurrentViewType = ViewTypeEnum.CALENDAR_VIEW
			mView.showCalendarView()
		}
	}
}