package com.springer.patryk.korkidajmi.view.loggedin.offerdetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.springer.patryk.korkidajmi.Application
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.helpers.ImageUtil
import com.springer.patryk.korkidajmi.model.network.endpoints.OffersApi
import com.springer.patryk.korkidajmi.view.loggedin.offerdetails.dialogs.CreateSubmissionDialog
import kotlinx.android.synthetic.main.fragment_offer_details.*
import pl.mauto24.app.view.base.BaseFragment

class OfferDetailsFragment : BaseFragment(), OfferDetailsContract.View {
	override val layoutResId: Int
		get() = R.layout.fragment_offer_details
	override val mTitleStringId: Int
		get() = R.string.page_title_offer_details

	private val mPresenter: OfferDetailsContract.Presenter by lazy {
		OfferDetailsPresenter(this,
				(mBaseActivity.application as Application).getRetrofitInstance().create(
						OffersApi::class.java))
	}

	companion object {
		const val REQUEST_CODE = 1293
		const val OFFER_ID_KEY = "offerId"
		fun newInstance(offerId: Int): OfferDetailsFragment {
			val fragment: OfferDetailsFragment = OfferDetailsFragment()
			val bundle: Bundle = Bundle().apply {
				putInt(OFFER_ID_KEY, offerId)
			}
			fragment.arguments = bundle
			return fragment
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		rv_offer_details_pricing.layoutManager = LinearLayoutManager(context)
		rv_offer_details_pricing.adapter = mPresenter.getSubjectsAdapter()
		rv_offer_details_pricing.addItemDecoration(
				DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

		rv_offer_details_other.layoutManager =
				LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
		rv_offer_details_other.adapter = mPresenter.getOtherOffersAdapter()

		btn_offer_details_calendar.setOnClickListener { showSnackbar("Pokaż cały kalendarz") }
		btn_offer_details_sign.setOnClickListener {
			val fragment = CreateSubmissionDialog.newInstance(mPresenter.getSubjectsList(),
					mPresenter.getOfferPrice(), this, REQUEST_CODE)
			fragmentManager?.beginTransaction()?.apply {
				add(fragment, null)
				commit()
			}
		}
	}

	override fun onResume() {
		super.onResume()
		arguments?.let { bundle ->
			mPresenter.refreshOfferDetails(bundle.getInt(OFFER_ID_KEY))
		}
	}

	override fun showTutorInfo(name: String, address: String, description: String,
							   photoBase64: String) {
		if (photoBase64.isNotBlank()) {
			iv_offer_details_picture.setImageBitmap(ImageUtil.convert(photoBase64))
		}
		tv_offer_details_city.text = address
		tv_offer_tutor_name.text = name
		tv_offer_details_description.text = description
	}

	override fun showOfferDetails(dateFrom: String, dateTo: String) {
		tv_offer_details_date_from.text = dateFrom
		tv_offer_details_date_to.text = dateTo
	}

	override fun showOtherOffer(offerId: Int) {
		mBaseActivity.setChildContent(OfferDetailsFragment.newInstance(offerId))
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			data?.let { intent ->
				val levelId = intent.getIntExtra(CreateSubmissionDialog.LEVEL_KEY, 0)
				val subjectId = intent.getIntExtra(CreateSubmissionDialog.SUBJECT_KEY, 0)
				val offerId = arguments?.getInt(OFFER_ID_KEY) ?: 0
				mPresenter.onSubmitClicked(offerId, levelId, subjectId)
			}
		}
	}
}
