package com.springer.patryk.korkidajmi.view.loggedin.submission

import com.springer.patryk.korkidajmi.view.base.BasePresenter
import com.springer.patryk.korkidajmi.view.loggedin.submission.adapters.SubmissionListAdapter
import pl.mauto24.app.view.base.BaseFragmentView

interface SubmissionContract {
	interface View : BaseFragmentView {

	}

	interface Presenter : BasePresenter {
		fun refreshSubmissionList()
		fun getAdapter(): SubmissionListAdapter
		fun onSubmissionRejected(submissionId: Int)
		fun onSubmissionAccepted(submissionId: Int)
	}
}