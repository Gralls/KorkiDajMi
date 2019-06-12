package com.springer.patryk.korkidajmi.view.loggedin

import android.content.Context

interface LoggedInContract {
	interface View {
		fun getContext(): Context?
		fun updateUser(name: String, isTutor: Boolean)
		fun updateUnreadCount(count: Int)
	}

	interface Presenter {
		fun refreshUser()
		fun refreshSubmissionCount()
		fun refreshCount()
	}
}