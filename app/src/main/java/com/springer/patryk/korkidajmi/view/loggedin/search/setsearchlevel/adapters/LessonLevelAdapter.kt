package com.springer.patryk.korkidajmi.view.loggedin.search.setsearchlevel.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.NamedObject

/**
 * Created by Patryk Springer on 2019-01-14.
 */
class LessonLevelAdapter(private val mContext: Context?, private val mItems: List<NamedObject>) :
		ArrayAdapter<NamedObject>(mContext, R.layout.default_spinner_item, mItems) {


	override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
		return createItemView(position, parent)
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		return createItemView(position, parent)
	}

	override fun isEnabled(position: Int): Boolean {
		return position != 0
	}

	private fun createItemView(position: Int, parent: ViewGroup?): View {
		val view: View = LayoutInflater.from(parent?.context)
				.inflate(R.layout.default_spinner_item, parent, false)
		val tvName: TextView = view.findViewById(android.R.id.text1)
		tvName.text = mItems[position].mName
		if (position == 0) {
			tvName.typeface = Typeface.DEFAULT_BOLD
		} else {
			tvName.typeface = Typeface.DEFAULT
		}

		return view
	}
}