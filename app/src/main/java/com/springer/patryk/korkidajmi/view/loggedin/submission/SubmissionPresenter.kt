package com.springer.patryk.korkidajmi.view.loggedin.submission

import com.springer.patryk.korkidajmi.model.helpers.PreferencesHelper
import com.springer.patryk.korkidajmi.model.network.NetResult
import com.springer.patryk.korkidajmi.model.network.endpoints.SubmissionApi
import com.springer.patryk.korkidajmi.view.base.BasePresenterImpl
import com.springer.patryk.korkidajmi.view.loggedin.LoggedInActivity
import com.springer.patryk.korkidajmi.view.loggedin.model.SubmissionStatus
import com.springer.patryk.korkidajmi.view.loggedin.submission.adapters.SubmissionListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SubmissionPresenter(private val mView: SubmissionContract.View,
						  private val mSubmissionApi: SubmissionApi) : SubmissionContract.Presenter,
		BasePresenterImpl(mView) {

	private val mAdapter: SubmissionListAdapter by lazy {
		var isTutor: Boolean = false
		mView.getContext()?.let {
			isTutor = PreferencesHelper.getUserInfo(it)?.mIsCoach ?: false
		}
		SubmissionListAdapter(this, isTutor)
	}

	private var mUserId: Int = 0

	init {
		mView.getContext()?.let {
			mUserId = PreferencesHelper.getUserInfo(it)?.mId ?: 0
		}
	}

	override fun refreshSubmissionList() {
		GlobalScope.launch(Dispatchers.Main) {
			val result = safeApiCall { mSubmissionApi.getSubmissionsList(mUserId).await() }
			if (result is NetResult.Success) {
				if (result.data.isSuccessful) {
					mAdapter.setDataset(ArrayList(
							result.data.body()?.filter { it.mStatus == SubmissionStatus.NEW }))
				}
			}
		}
	}

	override fun onSubmissionRejected(submissionId: Int) {
		changeStatus(submissionId, SubmissionStatus.REJECTED)
	}

	override fun onSubmissionAccepted(submissionId: Int) {
		changeStatus(submissionId, SubmissionStatus.ACCEPTED)
	}

	private fun changeStatus(submissionId: Int, newStatus: SubmissionStatus) {
		GlobalScope.launch(Dispatchers.Main) {
			val result = safeApiCall {
				mSubmissionApi.changeSubmissionStatus(submissionId, NewStatus(newStatus)).await()
			}
			if (result is NetResult.Success) {
				if (result.data.isSuccessful) {
					if (newStatus == SubmissionStatus.ACCEPTED) {
						mView.showSnackbar("Zgłoszenie zaakceptowane")
					}
					if (newStatus == SubmissionStatus.REJECTED) {
						mView.showSnackbar("Zgłoszenie odrzucone")
					}
					mAdapter.removeSubmission(submissionId)
					(mView.getActivity() as LoggedInActivity).mPresenter.refreshSubmissionCount()
				}
			}
		}
	}

	override fun getAdapter(): SubmissionListAdapter = mAdapter
}