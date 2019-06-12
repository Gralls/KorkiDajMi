package com.springer.patryk.korkidajmi.view.loggedin

import com.springer.patryk.korkidajmi.model.User
import com.springer.patryk.korkidajmi.model.helpers.PreferencesHelper
import com.springer.patryk.korkidajmi.model.network.endpoints.SubmissionApi
import com.springer.patryk.korkidajmi.view.loggedin.model.SubmissionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoggedInPresenter(private val mView: LoggedInContract.View,
						private val mSubmissionApi: SubmissionApi) : LoggedInContract.Presenter {

	private var mUserId = 0
	private var mCount = 0
	override fun refreshUser() {
		mView.getContext()?.let {
			val user: User? = PreferencesHelper.getUserInfo(it)
			if (user != null) {
				mUserId = user.mId ?: 0
				mView.updateUser(user.mFirstName, user.mIsCoach)
			}
		}
	}

	override fun refreshSubmissionCount() {
		GlobalScope.launch(Dispatchers.Main) {
			val result = mSubmissionApi.getSubmissionsList(mUserId).await()
			mCount = result.body()?.count { submission ->
				submission.mStatus == SubmissionStatus.NEW
			} ?: 0
			mView.updateUnreadCount(mCount)
		}
	}

	override fun refreshCount() {
		mView.updateUnreadCount(mCount)
	}
}