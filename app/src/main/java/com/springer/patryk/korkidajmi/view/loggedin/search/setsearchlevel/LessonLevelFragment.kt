package com.springer.patryk.korkidajmi.view.loggedin.search.setsearchlevel

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.springer.patryk.korkidajmi.Application
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.NamedObject
import com.springer.patryk.korkidajmi.model.network.endpoints.SubjectApi
import com.springer.patryk.korkidajmi.view.loggedin.search.lessonlist.LessonListFragment
import com.springer.patryk.korkidajmi.view.loggedin.search.setsearchlevel.adapters.LessonLevelAdapter
import kotlinx.android.synthetic.main.fragment_lessons_level.*
import pl.mauto24.app.view.base.BaseFragment

class LessonLevelFragment : BaseFragment(), LessonLevelContract.View {


	override val layoutResId: Int
		get() = R.layout.fragment_lessons_level
	override val mTitleStringId: Int
		get() = R.string.page_title_search_lessons
	private val mPresenter: LessonLevelContract.Presenter by lazy {
		LessonLevelPresenter(this,
				(mBaseActivity.application as Application).getRetrofitInstance().create(
						SubjectApi::class.java))
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		rv_set_lessons_level.adapter = mPresenter.getAdapter()
		rv_set_lessons_level.layoutManager = LinearLayoutManager(context)
	}

	override fun onResume() {
		super.onResume()
		mPresenter.refreshSubjectsList()
	}

	override fun setLevelAdapter(levels: List<NamedObject>) {
		val spLessonLevel: Spinner = view?.findViewById(R.id.sp_set_lessons_level) ?: return
		spLessonLevel.adapter = LessonLevelAdapter(context,levels)

		spLessonLevel.setSelection(0)
		spLessonLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(p0: AdapterView<*>?) {
			}

			override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
				val selectedLevel: NamedObject = p0?.selectedItem as NamedObject
				mPresenter.onLevelClicked(selectedLevel.mId)
			}
		}
	}

	override fun onPause() {
		super.onPause()
		mPresenter.detach()
	}

	override fun showLevels() {
		sp_set_lessons_level.performClick()
	}

	override fun openListView(subjectId: Int, levelId: Int) {
		mBaseActivity.setChildContent(LessonListFragment.newInstance(subjectId, levelId))
	}
}