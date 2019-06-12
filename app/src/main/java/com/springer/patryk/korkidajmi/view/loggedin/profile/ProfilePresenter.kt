package com.springer.patryk.korkidajmi.view.loggedin.profile

import com.springer.patryk.korkidajmi.model.NamedObject
import com.springer.patryk.korkidajmi.model.User
import com.springer.patryk.korkidajmi.model.helpers.PreferencesHelper
import com.springer.patryk.korkidajmi.model.network.NetResult
import com.springer.patryk.korkidajmi.model.network.endpoints.SubjectApi
import com.springer.patryk.korkidajmi.model.network.endpoints.UserApi
import com.springer.patryk.korkidajmi.view.base.BasePresenterImpl
import com.springer.patryk.korkidajmi.view.loggedin.model.UserSubject
import com.springer.patryk.korkidajmi.view.loggedin.profile.adapters.UserSubjectsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfilePresenter(private val mView: ProfileContract.View, private val mUserApi: UserApi,
					   private val mSubjectApi: SubjectApi) : BasePresenterImpl(mView),
		ProfileContract.Presenter {

	private var mAdapter: UserSubjectsAdapter? = null
	private var mUserSubjectsList: ArrayList<UserSubject> = ArrayList()
	private var mSubjectsList: MutableList<NamedObject> = mutableListOf()
	private var mLevelsList: MutableList<NamedObject> = mutableListOf()
	private var mUser: User? = null
	override fun refreshUserDetails() {
		GlobalScope.launch(Dispatchers.Main) {
			mView.getContext()?.let {
				val userId: Int = PreferencesHelper.getUserInfo(it)?.mId ?: return@launch
				val result = safeApiCall { mUserApi.getUserDetails(userId).await() }
				if (result is NetResult.Success) {
					if (result.data.isSuccessful) {
						mUser = result.data.body()
						mUser?.let { user ->
							mView.updateProfile(user)
							if (!user.mIsCoach) {
								mView.hideTutorFields()
							}
							mUserSubjectsList = user.mSubjectsList ?: ArrayList()
							getAdapter().setDataset(mUserSubjectsList)
						}
					}
				}
			}

		}
	}

	override fun refreshSubjectsInfo() {
		GlobalScope.launch(Dispatchers.Main) {
			val subjects = safeApiCall { mSubjectApi.getSubjects().await() }
			if (subjects is NetResult.Success && subjects.data.isSuccessful) {
				mSubjectsList = subjects.data.body()?.toMutableList() ?: mutableListOf()
			}
			val levels = safeApiCall { mSubjectApi.getSubjectsLevels().await() }
			if (levels is NetResult.Success && levels.data.isSuccessful) {
				mLevelsList = levels.data.body()?.toMutableList() ?: mutableListOf()
			}
			refreshUserDetails()
		}
	}

	override fun getAdapter(): UserSubjectsAdapter {
		if (mAdapter == null) {
			mAdapter = UserSubjectsAdapter(this)
		}
		return mAdapter!!
	}

	override fun onSubjectDelete(position: Int) {
		mUserSubjectsList.removeAt(position)
		getAdapter().notifyItemRemoved(position)
	}

	override fun getSubjects(): List<NamedObject> {
		return mSubjectsList
	}

	override fun getLevels(): List<NamedObject> {
		return mLevelsList
	}

	override fun onSubmitClicked(firstName: String, lastName: String, city: String, address: String,
								 photo: String, description: String) {
		mUser?.let { user ->
			user.mCity = city
			user.mAddress = address
			user.mLastName = lastName
			user.mFirstName = firstName
			user.mPhoto = photo
			user.mDescription = description
			user.mSubjectsList = mUserSubjectsList
			updateUser()
		}
	}

	private fun updateUser() {
		GlobalScope.launch(Dispatchers.Main) {
			val result = safeApiCall { mUserApi.updateUser(mUser?.mId ?: 0, mUser).await() }
			if (result is NetResult.Success) {
				mView.showSnackbar("Profil zaktualizowany")
			}
		}
	}

	override fun onAddNewSubject() {
		mUserSubjectsList.add(UserSubject(mSubjectsList.first(), mLevelsList.first()))
		getAdapter().setDataset(mUserSubjectsList)
	}
}