package com.springer.patryk.korkidajmi.view.loggedin.offerdetails

import com.springer.patryk.korkidajmi.model.User
import com.springer.patryk.korkidajmi.model.helpers.PreferencesHelper
import com.springer.patryk.korkidajmi.model.network.NetResult
import com.springer.patryk.korkidajmi.model.network.endpoints.OffersApi
import com.springer.patryk.korkidajmi.view.base.BasePresenterImpl
import com.springer.patryk.korkidajmi.view.loggedin.model.UserSubject
import com.springer.patryk.korkidajmi.view.loggedin.offerdetails.adapters.OtherOfferAdapter
import com.springer.patryk.korkidajmi.view.loggedin.offerdetails.adapters.TutorSubjectsAdapter
import com.springer.patryk.korkidajmi.view.loggedin.offerdetails.model.CreateSubmission
import com.springer.patryk.korkidajmi.view.loggedin.offerdetails.model.OfferDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.text.SimpleDateFormat

class OfferDetailsPresenter(private val mView: OfferDetailsContract.View,
							private val mOffersApi: OffersApi) : OfferDetailsContract.Presenter,
		BasePresenterImpl(mView) {

	private val mSdfServerDate = SimpleDateFormat("yyyy-MM-dd HH:mm")
	private val mSdfVisibleDate = SimpleDateFormat("dd-MM-yyyy HH:mm")
	private val mSdfVisibleHour = SimpleDateFormat("HH:mm")
	private val mSubjectsAdapter: TutorSubjectsAdapter by lazy {
		TutorSubjectsAdapter(0f)
	}
	private val mOtherOtherOfferAdapter: OtherOfferAdapter by lazy {
		OtherOfferAdapter(this)
	}
	private var mOfferPrice: Float = 0f

	private var mSubjectList: ArrayList<UserSubject> = ArrayList()

	override fun refreshOfferDetails(offerId: Int) {
		GlobalScope.launch(Dispatchers.Main) {
			val result = safeApiCall {
				mOffersApi.getOfferDetails(offerId).await()
			}
			if (result is NetResult.Success) {
				if (result.data.isSuccessful) {
					val offer: OfferDetails = result.data.body() ?: return@launch
					val tutor: User = offer.mTutor
					val tutorName: String = "${tutor.mFirstName} ${tutor.mLastName}"
					mView.showTutorInfo(tutorName, tutor.mCity ?: "", tutor.mDescription ?: "",
							tutor.mPhoto ?: "")
					val dateFrom = mSdfVisibleDate.format(mSdfServerDate.parse(offer.mDateFrom))
					val dateTo = mSdfVisibleHour.format(mSdfServerDate.parse(offer.mDateTo))
					mSubjectList = ArrayList(tutor.mSubjectsList)
					mOfferPrice = offer.mPrice
					mSubjectsAdapter.setOfferPrice(mOfferPrice)
					mSubjectsAdapter.setDataset(mSubjectList)
					mOtherOtherOfferAdapter.setDataset(ArrayList(offer.mOtherOffers.sortedBy {
						mSdfServerDate.parse(it.mDateFrom).time
					}))
					mView.showOfferDetails(dateFrom, dateTo)
				}
			}
		}
	}

	override fun getSubjectsList(): ArrayList<UserSubject> = mSubjectList

	override fun getOfferPrice(): Float = mOfferPrice
	override fun onOtherOfferClicked(offerId: Int) {
		mView.showOtherOffer(offerId)
	}

	override fun onSubmitClicked(offerId: Int, levelId: Int, subjectId: Int) {
		GlobalScope.launch(Dispatchers.Main) {
			var userId: Int = 0
			mView.getContext()?.let {
				userId = PreferencesHelper.getUserInfo(it)?.mId ?: 0
			}
			val result = safeApiCall {
				val submission = CreateSubmission(subjectId, levelId, userId)
				mOffersApi.submitToOffer(offerId, submission).await()
			}
			if (result is NetResult.Success) {
				val resultCode = result.data.code()
				if (resultCode == HttpURLConnection.HTTP_CREATED) {
					mView.showSnackbar("Zgłoszenie wysłane do korepytora")
				} else if (resultCode == HttpURLConnection.HTTP_CONFLICT) {
					mView.showSnackbar("Istnieje już zgłoszenie w tym terminie")
				}
			}
		}
	}

	override fun getSubjectsAdapter(): TutorSubjectsAdapter = mSubjectsAdapter
	override fun getOtherOffersAdapter(): OtherOfferAdapter = mOtherOtherOfferAdapter
}