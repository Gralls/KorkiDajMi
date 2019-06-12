package com.springer.patryk.korkidajmi.view.notloggedin.register

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

class RegisterPresenter(private val mView: RegisterContract.View, private val apiService: UserApi) :
		RegisterContract.Presenter, BasePresenterImpl(mView) {

	override fun onRegisterClicked(email: String, firstName: String, lastName: String,
								   password: String, passwordRepeat: String, isCoach: Boolean) {
		if (isFormValid(email, firstName, lastName, password, passwordRepeat)) {
			val user = User(null, email, password, firstName, lastName, isCoach)
			GlobalScope.launch(Dispatchers.Main) {
				val result = safeApiCall { apiService.createUser(user).await() }
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

	private fun isFormValid(email: String, firstName: String, lastName: String, password: String,
							passwordRepeat: String): Boolean {
		var isValid: Boolean = true

		if (!email.isValidEmail()) {
			isValid = false
			mView.showEmailError(R.string.error_invalid_email)
		}
		if (firstName.isBlank()) {
			isValid = false
			mView.showFirstNameError(R.string.error_required_field)
		}
		if (lastName.isBlank()) {
			isValid = false
			mView.showLastNameError(R.string.error_required_field)
		}
		if (password.isBlank()) {
			isValid = false
			mView.showPasswordError(R.string.error_required_field)
		}else if(password.length < 8){
			isValid = false
			mView.showPasswordError(R.string.error_password_length)
		}
		if (passwordRepeat.isBlank()) {
			isValid = false
			mView.showPasswordRepeatError(R.string.error_required_field)
		}
		if (password != passwordRepeat) {
			isValid = false
			mView.showPasswordError(R.string.error_different_passwords)
			mView.showPasswordRepeatError(R.string.error_different_passwords)
		}
		return isValid
	}
}