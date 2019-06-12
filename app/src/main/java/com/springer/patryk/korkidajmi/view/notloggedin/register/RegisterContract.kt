package com.springer.patryk.korkidajmi.view.notloggedin.register

import pl.mauto24.app.view.base.BaseFragmentView
import com.springer.patryk.korkidajmi.view.base.BasePresenter

interface RegisterContract {
	interface View : BaseFragmentView {
		fun showEmailError(errorId: Int)
		fun showLastNameError(errorId: Int)
		fun showFirstNameError(errorId: Int)
		fun showPasswordError(errorId: Int)
		fun showPasswordRepeatError(errorId: Int)
		fun openLoggedInView()

	}

	interface Presenter : BasePresenter {
		fun onRegisterClicked(email: String, firstName: String, lastName: String, password: String,
							  passwordRepeat: String, isCoach: Boolean)
	}
}