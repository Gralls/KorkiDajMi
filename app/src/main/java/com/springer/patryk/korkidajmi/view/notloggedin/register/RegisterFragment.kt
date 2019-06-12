package com.springer.patryk.korkidajmi.view.notloggedin.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.springer.patryk.korkidajmi.Application
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.extensions.getStringValue
import com.springer.patryk.korkidajmi.model.extensions.hideErrorsOnEditTextChanged
import com.springer.patryk.korkidajmi.model.network.endpoints.UserApi
import com.springer.patryk.korkidajmi.view.loggedin.LoggedInActivity
import kotlinx.android.synthetic.main.fragment_register.*
import pl.mauto24.app.view.base.BaseFragment

class RegisterFragment : BaseFragment(), RegisterContract.View {
	override val layoutResId: Int
		get() = R.layout.fragment_register

	lateinit var mPresenter: RegisterContract.Presenter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val userApi: UserApi = (mBaseActivity.application as Application).getRetrofitInstance()
				.create(UserApi::class.java)
		mPresenter = RegisterPresenter(this, userApi)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		btn_fragment_register_submit.setOnClickListener {
			val email: String = et_fragment_register_username.getStringValue()
			val firstName: String = et_fragment_register_first_name.getStringValue()
			val lastName: String = et_fragment_register_last_name.getStringValue()
			val password: String = et_fragment_register_password.getStringValue()
			val passwordRepeat: String = et_fragment_register_password_repeat.getStringValue()
			mPresenter.onRegisterClicked(email, firstName, lastName, password, passwordRepeat,
					cb_fragment_register_coach.isChecked)
		}
		til_fragment_register_username.hideErrorsOnEditTextChanged(et_fragment_register_username)
		til_fragment_register_last_name.hideErrorsOnEditTextChanged(et_fragment_register_last_name)
		til_fragment_register_first_name.hideErrorsOnEditTextChanged(
				et_fragment_register_first_name)
		til_fragment_register_password.hideErrorsOnEditTextChanged(et_fragment_register_password)
		til_fragment_register_password_repeat.hideErrorsOnEditTextChanged(
				et_fragment_register_password_repeat)
	}

	override fun showEmailError(errorId: Int) {
		til_fragment_register_username.error = getString(errorId)
	}

	override fun showLastNameError(errorId: Int) {
		til_fragment_register_last_name.error = getString(errorId)
	}

	override fun showFirstNameError(errorId: Int) {
		til_fragment_register_first_name.error = getString(errorId)
	}

	override fun showPasswordError(errorId: Int) {
		til_fragment_register_password.error = getString(errorId)
	}

	override fun showPasswordRepeatError(errorId: Int) {
		til_fragment_register_password_repeat.error = getString(errorId)
	}

	override fun openLoggedInView() {
		startActivity(Intent(context, LoggedInActivity::class.java))
		mBaseActivity.finish()
	}
}