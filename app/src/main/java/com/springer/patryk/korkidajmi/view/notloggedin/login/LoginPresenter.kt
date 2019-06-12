package com.springer.patryk.korkidajmi.view.notloggedin.login

import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.User
import com.springer.patryk.korkidajmi.model.extensions.isValidEmail
import com.springer.patryk.korkidajmi.model.helpers.PreferencesHelper
import com.springer.patryk.korkidajmi.model.network.NetResult
import com.springer.patryk.korkidajmi.model.network.endpoints.UserApi
import com.springer.patryk.korkidajmi.view.base.BasePresenterImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginPresenter(private val mView: LoginContract.View, private val apiService: UserApi) :
		LoginContract.Presenter, BasePresenterImpl(mView) {

	override fun onLoginButtonClicked(userName: String, password: String) {
		if (isFormValid(userName, password)) {
			GlobalScope.launch(Dispatchers.Main) {
				val result =
					safeApiCall { apiService.loginUser(User(null, userName, password)).await() }
				if (result is NetResult.Success) {
					if (result.data.isSuccessful) {
						mView.getContext()?.let {
							PreferencesHelper.setUserInfo(it, result.data.body())
							mView.openLoggedInView()
						}
					} else {
						mView.showSnackbar(R.string.net_error_invalid_login_data)
					}
				}
			}
		}
	}

	private fun isFormValid(userName: String, password: String): Boolean {
		var isValid = true
		if (!userName.isValidEmail()) {
			isValid = false
			mView.showLoginError(R.string.error_invalid_email)
		}
		if (password.isBlank()) {
			isValid = false
			mView.showPasswordError(R.string.error_required_field)
		}
		return isValid
	}

	override fun onNoAccountClicked() {
		mView.openRegisterView()
	}
}