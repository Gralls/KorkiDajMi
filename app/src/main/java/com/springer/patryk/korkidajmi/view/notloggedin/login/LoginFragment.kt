package com.springer.patryk.korkidajmi.view.notloggedin.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.springer.patryk.korkidajmi.Application
import com.springer.patryk.korkidajmi.BuildConfig
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.extensions.getStringValue
import com.springer.patryk.korkidajmi.model.extensions.hideErrorsOnEditTextChanged
import com.springer.patryk.korkidajmi.model.network.endpoints.UserApi
import com.springer.patryk.korkidajmi.view.loggedin.LoggedInActivity
import com.springer.patryk.korkidajmi.view.notloggedin.register.RegisterFragment
import kotlinx.android.synthetic.main.fragment_login.*
import pl.mauto24.app.view.base.BaseFragment

class LoginFragment : BaseFragment(), LoginContract.View {
	override val layoutResId: Int
		get() = R.layout.fragment_login

	lateinit var mPresenter: LoginContract.Presenter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val userApi: UserApi = (mBaseActivity.application as Application).getRetrofitInstance()
				.create(UserApi::class.java)
		mPresenter = LoginPresenter(this, userApi)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		btn_fragment_login_submit.setOnClickListener {
			val userName: String = et_fragment_login_username.getStringValue()
			val password: String = et_fragment_login_password.getStringValue()
			mPresenter.onLoginButtonClicked(userName, password)
		}
		btn_fragment_login_no_account.setOnClickListener {
			mPresenter.onNoAccountClicked()
		}
		til_fragment_login_username.hideErrorsOnEditTextChanged(et_fragment_login_username)
		til_fragment_login_password.hideErrorsOnEditTextChanged(et_fragment_login_password)
		if(BuildConfig.DEBUG){
			et_fragment_login_password.setText("demoTest1")
			et_fragment_login_username.setText("test@test.pl")

		}
	}

	override fun openRegisterView() {
		mBaseActivity.setChildContent(RegisterFragment())
	}

	override fun openLoggedInView() {
		startActivity(Intent(context, LoggedInActivity::class.java))
		mBaseActivity.finish()
	}

	override fun showLoginError(errorId: Int) {
		til_fragment_login_username.error = getString(errorId)
	}

	override fun showPasswordError(errorId: Int) {
		til_fragment_login_password.error = getString(errorId)
	}
}