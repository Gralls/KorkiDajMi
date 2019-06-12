package com.springer.patryk.korkidajmi.model.network.endpoints

import com.springer.patryk.korkidajmi.model.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
	@POST(EndpointsName.User.LOGIN)
	fun loginUser(@Body
				  user: User): Deferred<Response<User>>

	@POST(EndpointsName.User.CREATE_USER)
	fun createUser(@Body
				   user: User): Deferred<Response<User>>

	@GET(EndpointsName.User.USER_DETAILS)
	fun getUserDetails(@Path("user_id")
					   userId: Int): Deferred<Response<User>>

	@PUT(EndpointsName.User.USER_DETAILS)
	fun updateUser(@Path("user_id")
				   userId: Int,
				   @Body
				   user: User?): Deferred<Response<Unit>>
}