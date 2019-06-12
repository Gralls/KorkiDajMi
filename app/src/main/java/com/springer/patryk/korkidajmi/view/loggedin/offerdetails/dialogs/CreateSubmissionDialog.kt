package com.springer.patryk.korkidajmi.view.loggedin.offerdetails.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.Window
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.view.base.BaseDialogFragment
import com.springer.patryk.korkidajmi.view.loggedin.model.UserSubject
import kotlinx.android.synthetic.main.dialog_create_submission.*
import pl.mauto24.app.view.base.BaseFragment

class CreateSubmissionDialog : BaseDialogFragment(), TutorSubjectsAdapter.SubjectClickedListener {

	override val layoutResId: Int
		get() = R.layout.dialog_create_submission

	companion object {
		const val SUBJECTS_KEY = "subjectList"
		const val LEVEL_KEY = "levelId"
		const val OFFER_PRICE_KEY = "offerPrice"
		const val SUBJECT_KEY = "subjectId"
		const val REQUEST_CODE_KEY = "requestCode"
		fun newInstance(subjects: ArrayList<UserSubject>, offerPrice: Float, parent: BaseFragment,
						requestCode: Int): CreateSubmissionDialog {
			val fragment = CreateSubmissionDialog()
			fragment.setTargetFragment(parent, requestCode)
			val bundle = Bundle().apply {
				putInt(REQUEST_CODE_KEY, requestCode)
				putSerializable(SUBJECTS_KEY, subjects)
				putFloat(OFFER_PRICE_KEY, offerPrice)
			}
			fragment.arguments = bundle
			return fragment
		}
	}

	override fun onStart() {
		super.onStart()
		dialog.window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,
				ConstraintLayout.LayoutParams.WRAP_CONTENT)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		rv_create_submission_pricing.layoutManager = LinearLayoutManager(context)
		rv_create_submission_pricing.addItemDecoration(
				DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
		arguments?.let { bundle ->
			val offerPrice = bundle.getFloat(OFFER_PRICE_KEY)
			val list: ArrayList<UserSubject> =
				bundle.getSerializable(SUBJECTS_KEY) as ArrayList<UserSubject>
			val adapter = TutorSubjectsAdapter(offerPrice, this)
			adapter.setDataset(list)
			rv_create_submission_pricing.adapter = adapter
		}
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val dialog = super.onCreateDialog(savedInstanceState)
		dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
		return dialog
	}

	override fun onSubjectClicked(levelId: Int, subjectId: Int) {
		arguments?.let { bundle ->
			val reqCode = bundle.getInt(REQUEST_CODE_KEY)
			val intent = Intent().apply {
				putExtra(LEVEL_KEY, levelId)
				putExtra(SUBJECT_KEY, subjectId)
			}
			targetFragment?.onActivityResult(reqCode, Activity.RESULT_OK, intent)
			dismiss()
		}
	}
}