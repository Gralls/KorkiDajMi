package com.springer.patryk.korkidajmi.view.loggedin.search.setsearchlevel.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.NamedObject
import com.springer.patryk.korkidajmi.view.loggedin.search.setsearchlevel.LessonLevelContract
import kotlinx.android.synthetic.main.row_search_subject.view.*
import pl.mauto24.app.view.base.BaseRecyclerViewAdapter

class LessonSubjectsAdapter(private val mPresenter: LessonLevelContract.Presenter) :
		BaseRecyclerViewAdapter<NamedObject, LessonSubjectsAdapter.ViewHolder>() {

	override fun setViewHolder(parent: ViewGroup): ViewHolder {
		val view: View =
			LayoutInflater.from(parent.context).inflate(R.layout.row_search_subject, parent, false)
		return ViewHolder(view)
	}

	override fun bindView(item: NamedObject, viewHolder: ViewHolder) {
		viewHolder.bind(item)
	}

	inner class ViewHolder(private val mItemView: View) : RecyclerView.ViewHolder(mItemView) {
		fun bind(subject: NamedObject) = with(mItemView) {
			val position = adapterPosition
			if (position % 2 == 0) {
				this.setBackgroundColor(
						ContextCompat.getColor(context, R.color.darkSubjectBackgroundColor))
			} else {
				this.setBackgroundColor(
						ContextCompat.getColor(context, R.color.lightSubjectBackgroundColor))
			}
			this.setOnClickListener {
				mPresenter.onSubjectClicked(subject.mId)
			}
			tv_row_search_subject_name.text = subject.mName
		}
	}
}