package com.springer.patryk.korkidajmi.view.loggedin.submission.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.extensions.formatHourPrice
import com.springer.patryk.korkidajmi.model.extensions.setVisibility
import com.springer.patryk.korkidajmi.model.helpers.PreferencesHelper
import com.springer.patryk.korkidajmi.view.loggedin.model.Submission
import com.springer.patryk.korkidajmi.view.loggedin.submission.SubmissionContract
import kotlinx.android.synthetic.main.row_submission.view.*
import pl.mauto24.app.view.base.BaseRecyclerViewAdapter

class SubmissionListAdapter(private val mPresenter: SubmissionContract.Presenter,
							val isTutor: Boolean) :
		BaseRecyclerViewAdapter<Submission, SubmissionListAdapter.ViewHolder>() {

	var currentUserId = 0
	override fun setViewHolder(parent: ViewGroup): ViewHolder {
		currentUserId = PreferencesHelper.getUserInfo(parent.context)?.mId ?: 0
		val view: View =
			LayoutInflater.from(parent.context).inflate(R.layout.row_submission, parent, false)
		return ViewHolder(view)
	}

	public fun removeSubmission(submissionId: Int) {
		val index = mItems.indexOf(mItems.find { it.mId == submissionId })
		mItems.removeAt(index)
		notifyItemRemoved(index)
	}

	override fun bindView(item: Submission, viewHolder: ViewHolder) {
		viewHolder.bind(item)
	}

	inner class ViewHolder(private val mItemView: View) : RecyclerView.ViewHolder(mItemView) {
		fun bind(submission: Submission) = with(mItemView) {
			val offer = submission.mOffer
			tv_submission_date.text = offer.getDate()
			tv_submission_time.text = "${offer.getFromHour()} - ${offer.getToHour()}"
			tv_submission_level.text = submission.mLevel.mName
			tv_submission_subject.text = submission.mSubject.mName
			tv_submission_place.text = offer.getAddress(isTutor, true)
			tv_submission_price.text = formatHourPrice(offer.mPrice)
			if (currentUserId == offer.mTutor?.mId) {
				ibtn_submission_accept.setVisibility(true)
				ibtn_submission_reject.setVisibility(true)
			} else {
				ibtn_submission_accept.setVisibility(false)
				ibtn_submission_reject.setVisibility(false)
			}
			ibtn_submission_reject.setOnClickListener {
				mPresenter.onSubmissionRejected(submission.mId)
			}
			ibtn_submission_accept.setOnClickListener {
				mPresenter.onSubmissionAccepted(submission.mId)
			}
		}
	}
}