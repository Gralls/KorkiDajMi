package com.springer.patryk.korkidajmi.view.loggedin.search.lessonlist.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.Offer
import com.springer.patryk.korkidajmi.model.extensions.formatHourPrice
import com.springer.patryk.korkidajmi.view.loggedin.search.lessonlist.LessonListContract
import kotlinx.android.synthetic.main.row_offer.view.*
import pl.mauto24.app.view.base.BaseRecyclerViewAdapter

class LessonListAdapter(private val mPresenter: LessonListContract.Presenter) :
		BaseRecyclerViewAdapter<Offer, LessonListAdapter.ViewHolder>() {

	override fun setViewHolder(parent: ViewGroup): ViewHolder {
		val view: View =
			LayoutInflater.from(parent.context).inflate(R.layout.row_offer, parent, false)
		return ViewHolder(view)
	}

	override fun bindView(item: Offer, viewHolder: ViewHolder) {
		viewHolder.bind(item)
	}

	inner class ViewHolder(private val mItemView: View) : RecyclerView.ViewHolder(mItemView) {
		fun bind(offer: Offer) = with(mItemView) {
			(mItemView as CardView).setCardBackgroundColor(
					ContextCompat.getColor(context, R.color.freeEventColor))
			tv_row_lesson_date.text = offer.getDate()
			tv_row_lesson_time_from.text = offer.getFromHour()
			tv_row_lesson_time_to.text = offer.getToHour()
			tv_row_lesson_pricing.text = formatHourPrice(offer.mPrice)
			tv_row_lesson_address.text = "${offer.mTutor?.mFirstName} ${offer.mTutor?.mCity}"
			this.setOnClickListener {
				mPresenter.onOfferClicked(offer.mId)
			}
		}
	}
}