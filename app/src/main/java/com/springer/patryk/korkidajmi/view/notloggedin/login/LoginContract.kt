package com.springer.patryk.korkidajmi.view.notloggedin.login

import pl.mauto24.app.view.base.BaseFragmentView
import com.springer.patryk.korkidajmi.view.base.BasePresenter

interface LoginContract {

	interface View : BaseFragmentView {
		fun openRegisterView()
		fun openLoggedInView()
		fun showLoginError(errorId: Int)
		fun showPasswordError(errorId: Int)
	}

	interface Presenter : BasePresenter {
		fun onLoginButtonClicked(userName: String, password: String)
		fun onNoAccountClicked()
	}
}