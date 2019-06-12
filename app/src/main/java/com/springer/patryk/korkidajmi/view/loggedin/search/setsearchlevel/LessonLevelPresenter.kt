package com.springer.patryk.korkidajmi.view.loggedin.search.setsearchlevel

import com.springer.patryk.korkidajmi.model.NamedObject
import com.springer.patryk.korkidajmi.model.network.NetResult
import com.springer.patryk.korkidajmi.model.network.endpoints.SubjectApi
import com.springer.patryk.korkidajmi.view.base.BasePresenterImpl
import com.springer.patryk.korkidajmi.view.loggedin.search.setsearchlevel.adapters.LessonSubjectsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LessonLevelPresenter(private val mView: LessonLevelContract.View,
						   private val mSubjectApi: SubjectApi) : LessonLevelContract.Presenter,
		BasePresenterImpl(mView) {

	private val mAdapter: LessonSubjectsAdapter by lazy { LessonSubjectsAdapter(this) }
	private var mSubjectsList: ArrayList<NamedObject> = ArrayList()
	private var mLevelsList: ArrayList<NamedObject> = ArrayList()
	private var mClickedSubjectId: Int = -1
	override fun refreshSubjectsList() {
		GlobalScope.launch(Dispatchers.Main) {
			val subjects = safeApiCall { mSubjectApi.getSubjects().await() }
			if (subjects is NetResult.Success && subjects.data.isSuccessful) {
				mSubjectsList = ArrayList(subjects.data.body())
				mAdapter.setDataset(mSubjectsList)
			}
			val levels = safeApiCall { mSubjectApi.getSubjectsLevels().await() }
			if (levels is NetResult.Success && levels.data.isSuccessful) {
				mLevelsList = ArrayList(levels.data.body())
				mLevelsList.add(0, NamedObject(-1, "Wybierz poziom"))
				mView.setLevelAdapter(mLevelsList)
			}
		}
	}

	override fun getAdapter(): LessonSubjectsAdapter = mAdapter

	override fun onSubjectClicked(subjectId: Int) {
		mClickedSubjectId = subjectId
		mView.showLevels()
	}

	override fun onLevelClicked(levelId: Int) {
		if (mClickedSubjectId != -1) {
			mView.openListView(mClickedSubjectId, levelId)
		}
	}

	override fun detach() {
		mClickedSubjectId = -1
	}
}