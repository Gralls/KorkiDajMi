package com.springer.patryk.korkidajmi.view.notloggedin

import android.content.Intent
import android.os.Bundle
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.helpers.PreferencesHelper
import com.springer.patryk.korkidajmi.view.base.BaseActivity
import com.springer.patryk.korkidajmi.view.loggedin.LoggedInActivity
import com.springer.patryk.korkidajmi.view.notloggedin.login.LoginFragment

class NotLoggedInActivity : BaseActivity() {
	override val mLayoutResId: Int
		get() = R.layout.activity_not_logged_in

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent(LoginFragment())
		if (PreferencesHelper.getUserInfo(this) != null) {
			startActivity(Intent(this, LoggedInActivity::class.java))
			finish()
		}
	}
}
