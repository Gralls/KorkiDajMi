package com.springer.patryk.korkidajmi.model.network.endpoints

import com.springer.patryk.korkidajmi.model.NamedObject
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface SubjectApi {
	@GET(EndpointsName.Subject.SUBJECTS_LIST)
	fun getSubjects(): Deferred<Response<List<NamedObject>>>

	@GET(EndpointsName.Subject.SUBJECTS_LEVEL_LIST)
	fun getSubjectsLevels(): Deferred<Response<List<NamedObject>>>
}