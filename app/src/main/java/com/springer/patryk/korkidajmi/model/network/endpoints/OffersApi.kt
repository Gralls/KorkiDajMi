package com.springer.patryk.korkidajmi.model.network.endpoints

import com.springer.patryk.korkidajmi.model.Offer
import com.springer.patryk.korkidajmi.view.loggedin.offerdetails.model.CreateSubmission
import com.springer.patryk.korkidajmi.view.loggedin.offerdetails.model.OfferDetails
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OffersApi {
	@GET(EndpointsName.Offer.OFFERS)
	fun getUserOffers(@Query("mUserId")
					  userId: Int,
					  @Query("mDate")
					  date: String? = null): Deferred<Response<List<Offer>>>

	@POST(EndpointsName.Offer.OFFERS)
	fun addNewOffer(@Body
					offer: Offer): Deferred<Response<Unit>>

	@GET(EndpointsName.Offer.OFFERS)
	fun getOffers(@Query("subjectId")
				  subjectId: Int,
				  @Query("levelId")
				  levelId: Int,
				  @Query("priceMinimal")
				  minPrice: Int?,
				  @Query("priceMaximal")
				  maxPrice: Int?,
				  @Query("date")
				  date: String?): Deferred<Response<List<Offer>>>

	@GET(EndpointsName.Offer.OFFER_DETAILS)
	fun getOfferDetails(@Path("offerId")
						offerId: Int): Deferred<Response<OfferDetails>>

	@POST(EndpointsName.Offer.SUBMIT_OFFER)
	fun submitToOffer(@Path("offerId")
					  offerId: Int,
					  @Body
					  submission: CreateSubmission): Deferred<Response<Unit>>
}