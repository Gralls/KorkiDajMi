package com.springer.patryk.korkidajmi.view.loggedin.profile.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.NamedObject
import com.springer.patryk.korkidajmi.model.extensions.getFloatValue
import com.springer.patryk.korkidajmi.view.loggedin.model.UserSubject
import com.springer.patryk.korkidajmi.view.loggedin.profile.ProfileContract
import kotlinx.android.synthetic.main.row_subject.view.*
import pl.mauto24.app.view.base.BaseRecyclerViewAdapter

class UserSubjectsAdapter(private val mPresenter: ProfileContract.Presenter) :
		BaseRecyclerViewAdapter<UserSubject, UserSubjectsAdapter.ViewHolder>() {


	val subjectsAdapter: ArrayAdapter<NamedObject> by lazy {
		ArrayAdapter(context, R.layout.default_spinner_item, mPresenter.getSubjects())
	}
	val levelsAdapter: ArrayAdapter<NamedObject> by lazy {
		ArrayAdapter(context, R.layout.default_spinner_item, mPresenter.getLevels())
	}
	lateinit var context: Context
	override fun setViewHolder(parent: ViewGroup): ViewHolder {
		context = parent.context
		val view: View =
			LayoutInflater.from(parent.context).inflate(R.layout.row_subject, parent, false)
		return ViewHolder(view)
	}

	override fun bindView(item: UserSubject, viewHolder: ViewHolder) {
		viewHolder.bind(item)
	}

	inner class ViewHolder(private val mItemView: View) : RecyclerView.ViewHolder(mItemView) {
		fun bind(item: UserSubject) = with(mItemView) {
			iv_row_subject_remove.setOnClickListener { mPresenter.onSubjectDelete(adapterPosition) }
			sp_row_subject_subject.adapter = subjectsAdapter
			sp_row_subject_level.adapter = levelsAdapter
			sp_row_subject_subject.setSelection(subjectsAdapter.getPosition(item.mSubject))
			sp_row_subject_level.setSelection(levelsAdapter.getPosition(item.mLevel))
			et_row_subject_additional_price.setText(item.additionalPrice.toInt().toString())
			val textWatcher: TextWatcher = object : TextWatcher {
				override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int,
											   after: Int) {
				}

				override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				}

				override fun afterTextChanged(s: Editable?) {
					item.additionalPrice = et_row_subject_additional_price.getFloatValue()
				}
			}
			et_row_subject_additional_price.removeTextChangedListener(textWatcher)
			et_row_subject_additional_price.addTextChangedListener(textWatcher)
			sp_row_subject_subject.onItemSelectedListener =
					object : AdapterView.OnItemSelectedListener {
						override fun onNothingSelected(p0: AdapterView<*>?) {
						}

						override fun onItemSelected(adapterView: AdapterView<*>?, p1: View?, i: Int,
													p3: Long) {
							val selectedSubject: NamedObject =
								adapterView?.selectedItem as NamedObject
							item.mSubject = selectedSubject
						}
					}
			sp_row_subject_level.onItemSelectedListener =
					object : AdapterView.OnItemSelectedListener {
						override fun onNothingSelected(p0: AdapterView<*>?) {
						}

						override fun onItemSelected(adapterView: AdapterView<*>?, p1: View?, i: Int,
													p3: Long) {
							val selectedLevel: NamedObject =
								adapterView?.selectedItem as NamedObject
							item.mLevel = selectedLevel
						}
					}
		}
	}
}