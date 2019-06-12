package com.springer.patryk.korkidajmi.view.loggedin.offerdetails.dialogs

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.extensions.formatHourPrice
import com.springer.patryk.korkidajmi.view.loggedin.model.UserSubject
import kotlinx.android.synthetic.main.row_submission_pricing.view.*
import pl.mauto24.app.view.base.BaseRecyclerViewAdapter

class TutorSubjectsAdapter(private var mOfferPrice: Float,
						   private val mListener: SubjectClickedListener) :
		BaseRecyclerViewAdapter<UserSubject, TutorSubjectsAdapter.ViewHolder>() {

	override fun setViewHolder(parent: ViewGroup): ViewHolder {
		val view: View = LayoutInflater.from(parent.context)
				.inflate(R.layout.row_submission_pricing, parent, false)
		return ViewHolder(view)
	}

	override fun bindView(item: UserSubject, viewHolder: ViewHolder) {
		viewHolder.bind(item)
	}

	inner class ViewHolder(private val mItemView: View) : RecyclerView.ViewHolder(mItemView) {
		fun bind(userSubject: UserSubject) = with(mItemView) {
			tv_row_submission_pricing_level.text = userSubject.mLevel.mName
			tv_row_submission_pricing_subject.text = userSubject.mSubject.mName
			val totalPrice = mOfferPrice + userSubject.additionalPrice
			tv_row_submission_pricing_price.text = formatHourPrice(totalPrice)
			this.setOnClickListener {
				mListener.onSubjectClicked(userSubject.mLevel.mId, userSubject.mSubject.mId)
			}
		}
	}

	interface SubjectClickedListener {
		fun onSubjectClicked(levelId: Int, subjectId: Int)
	}
}