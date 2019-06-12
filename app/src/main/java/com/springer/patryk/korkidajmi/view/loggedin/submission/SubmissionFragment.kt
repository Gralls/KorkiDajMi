package com.springer.patryk.korkidajmi.view.loggedin.submission

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.springer.patryk.korkidajmi.Application
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.network.endpoints.SubmissionApi
import kotlinx.android.synthetic.main.fragment_submissions.*
import pl.mauto24.app.view.base.BaseFragment

class SubmissionFragment : SubmissionContract.View, BaseFragment() {
	override val layoutResId: Int
		get() = R.layout.fragment_submissions
	override val mTitleStringId: Int
		get() = R.string.menu_title_submission_list

	private val mPresenter: SubmissionContract.Presenter by lazy {
		SubmissionPresenter(this,
				(mBaseActivity.application as Application).getRetrofitInstance().create(
						SubmissionApi::class.java))
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		rv_submission_list.layoutManager = LinearLayoutManager(context)
		rv_submission_list.adapter = mPresenter.getAdapter()
	}

	override fun onResume() {
		super.onResume()
		mBaseActivity.invalidateOptionsMenu()
		mPresenter.refreshSubmissionList()
	}
}