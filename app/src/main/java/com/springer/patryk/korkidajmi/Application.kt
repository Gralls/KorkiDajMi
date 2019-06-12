package com.springer.patryk.korkidajmi

import android.app.Application
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class Application : Application() {
	var retrofit: Retrofit? = null

	override fun onCreate() {
		super.onCreate()
		getRetrofitInstance()
		Timber.plant(Timber.DebugTree())
	}

	fun getRetrofitInstance(): Retrofit {
		if (retrofit == null) {

			val client = OkHttpClient.Builder().addInterceptor(
					HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
					.connectTimeout(10, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS)
					.writeTimeout(10, TimeUnit.SECONDS).build()

			retrofit = Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(client)
					.addConverterFactory(GsonConverterFactory.create())
					.addCallAdapterFactory(CoroutineCallAdapterFactory()).build()
		}
		return retrofit!!
	}
}