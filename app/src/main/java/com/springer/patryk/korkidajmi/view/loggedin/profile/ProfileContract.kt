package com.springer.patryk.korkidajmi.view.loggedin.profile

import com.springer.patryk.korkidajmi.model.NamedObject
import com.springer.patryk.korkidajmi.model.User
import com.springer.patryk.korkidajmi.view.base.BasePresenter
import com.springer.patryk.korkidajmi.view.loggedin.profile.adapters.UserSubjectsAdapter
import pl.mauto24.app.view.base.BaseFragmentView

interface ProfileContract {
	interface View : BaseFragmentView {
		fun updateProfile(user: User)
		fun hideTutorFields()
	}

	interface Presenter : BasePresenter {
		fun refreshSubjectsInfo()
		fun getAdapter(): UserSubjectsAdapter
		fun refreshUserDetails()
		fun onSubjectDelete(position: Int)
		fun getSubjects(): List<NamedObject>
		fun getLevels(): List<NamedObject>
		fun onSubmitClicked(firstName: String, lastName: String, city: String, address: String,
							photo: String, description: String)

		fun onAddNewSubject()
	}
}