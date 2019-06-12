package com.springer.patryk.korkidajmi.view.loggedin.offerdetails.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.Offer
import com.springer.patryk.korkidajmi.view.loggedin.offerdetails.OfferDetailsContract
import kotlinx.android.synthetic.main.row_other_offer.view.*
import pl.mauto24.app.view.base.BaseRecyclerViewAdapter

class OtherOfferAdapter(private val mPresenter: OfferDetailsContract.Presenter) :
		BaseRecyclerViewAdapter<Offer, OtherOfferAdapter.ViewHolder>() {

	override fun setViewHolder(parent: ViewGroup): ViewHolder {
		val view: View =
			LayoutInflater.from(parent.context).inflate(R.layout.row_other_offer, parent, false)
		return ViewHolder(view)
	}

	override fun bindView(item: Offer, viewHolder: ViewHolder) {
		viewHolder.bind(item)
	}

	inner class ViewHolder(private val mItemView: View) : RecyclerView.ViewHolder(mItemView) {
		fun bind(offer: Offer) = with(mItemView) {
			tv_row_other_offer_date.text = offer.getDate()
			tv_row_other_offer_time.text = "${offer.getFromHour()} - ${offer.getToHour()}"
			this.setOnClickListener { mPresenter.onOtherOfferClicked(offer.mId) }
		}
	}
}