package com.springer.patryk.korkidajmi.view.loggedin.offerdetails

import com.springer.patryk.korkidajmi.view.base.BasePresenter
import com.springer.patryk.korkidajmi.view.loggedin.model.UserSubject
import com.springer.patryk.korkidajmi.view.loggedin.offerdetails.adapters.OtherOfferAdapter
import com.springer.patryk.korkidajmi.view.loggedin.offerdetails.adapters.TutorSubjectsAdapter
import pl.mauto24.app.view.base.BaseFragmentView

interface OfferDetailsContract {
	interface View : BaseFragmentView {
		fun showTutorInfo(name: String, address: String, description: String, photoBase64: String)
		fun showOfferDetails(dateFrom: String, dateTo: String)
		fun showOtherOffer(offerId: Int)
	}

	interface Presenter : BasePresenter {
		fun refreshOfferDetails(offerId: Int)
		fun getSubjectsAdapter(): TutorSubjectsAdapter
		fun getOtherOffersAdapter(): OtherOfferAdapter
		fun onOtherOfferClicked(offerId: Int)
		fun onSubmitClicked(offerId: Int, levelId: Int, subjectId: Int)
		fun getSubjectsList(): ArrayList<UserSubject>
		fun getOfferPrice(): Float
	}
}