package com.springer.patryk.korkidajmi.view.loggedin.offerdetails.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.extensions.formatHourPrice
import com.springer.patryk.korkidajmi.view.loggedin.model.UserSubject
import kotlinx.android.synthetic.main.row_pricing.view.*
import pl.mauto24.app.view.base.BaseRecyclerViewAdapter

class TutorSubjectsAdapter(private var mOfferPrice: Float) :
		BaseRecyclerViewAdapter<UserSubject, TutorSubjectsAdapter.ViewHolder>() {

	public fun setOfferPrice(price: Float) {
		mOfferPrice = price
		notifyDataSetChanged()
	}

	override fun setViewHolder(parent: ViewGroup): ViewHolder {
		val view: View =
			LayoutInflater.from(parent.context).inflate(R.layout.row_pricing, parent, false)
		return ViewHolder(view)
	}

	override fun bindView(item: UserSubject, viewHolder: ViewHolder) {
		viewHolder.bind(item)
	}

	inner class ViewHolder(private val mItemView: View) : RecyclerView.ViewHolder(mItemView) {
		fun bind(userSubject: UserSubject) = with(mItemView) {
			tv_row_pricing_level.text = userSubject.mLevel.mName
			tv_row_pricing_subject.text = userSubject.mSubject.mName
			val totalPrice = mOfferPrice + userSubject.additionalPrice
			tv_row_pricing_price.text = formatHourPrice(totalPrice)
		}
	}
}