package com.springer.patryk.korkidajmi.view.loggedin.search.setsearchlevel

import com.springer.patryk.korkidajmi.model.NamedObject
import com.springer.patryk.korkidajmi.view.base.BasePresenter
import com.springer.patryk.korkidajmi.view.loggedin.search.setsearchlevel.adapters.LessonSubjectsAdapter
import pl.mauto24.app.view.base.BaseFragmentView

interface LessonLevelContract {
	interface View : BaseFragmentView {
		fun setLevelAdapter(levels: List<NamedObject>)
		fun showLevels()
		fun openListView(subjectId: Int, levelId: Int)
	}

	interface Presenter : BasePresenter {
		fun refreshSubjectsList()
		fun getAdapter(): LessonSubjectsAdapter
		fun onSubjectClicked(subjectId: Int)
		fun onLevelClicked(levelId: Int)
		fun detach()
	}
}