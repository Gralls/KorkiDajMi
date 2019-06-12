package com.springer.patryk.korkidajmi.model.network.endpoints

import com.springer.patryk.korkidajmi.view.loggedin.model.Submission
import com.springer.patryk.korkidajmi.view.loggedin.submission.NewStatus
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SubmissionApi {
	@GET(EndpointsName.Submission.SUBMISSION_LIST)
	fun getSubmissionsList(@Path("userId")
						   userId: Int): Deferred<Response<List<Submission>>>

	@POST(EndpointsName.Submission.CHANGE_SUBMISSION_STATUS)
	fun changeSubmissionStatus(@Path("submissionId")
							   submissionId: Int,
							   @Body
							   newStatus: NewStatus): Deferred<Response<Unit>>
}