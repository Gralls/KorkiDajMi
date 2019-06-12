package com.springer.patryk.korkidajmi.view.loggedin.profile

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.springer.patryk.korkidajmi.Application
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.User
import com.springer.patryk.korkidajmi.model.extensions.getStringValue
import com.springer.patryk.korkidajmi.model.extensions.setVisibility
import com.springer.patryk.korkidajmi.model.helpers.ImageUtil
import com.springer.patryk.korkidajmi.model.network.endpoints.SubjectApi
import com.springer.patryk.korkidajmi.model.network.endpoints.UserApi
import com.springer.patryk.korkidajmi.view.loggedin.LoggedInActivity
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.fragment_profile.*
import pl.mauto24.app.view.base.BaseFragment

class ProfileFragment : BaseFragment(), ProfileContract.View {
	override val layoutResId: Int
		get() = R.layout.fragment_profile
	override val mTitleStringId: Int
		get() = R.string.page_title_profile

	companion object {
		const val PICTURE_REQUEST_CODE: Int = 23816
		const val GALLERY_REQUEST_CODE: Int = 12314
	}

	private lateinit var userApi: UserApi
	private lateinit var subjectApi: SubjectApi

	private val mPresenter: ProfileContract.Presenter by lazy {
		ProfilePresenter(this, userApi, subjectApi)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		userApi = (mBaseActivity.application as Application).getRetrofitInstance()
				.create(UserApi::class.java)
		subjectApi = (mBaseActivity.application as Application).getRetrofitInstance()
				.create(SubjectApi::class.java)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		iv_profile_picture.setOnClickListener {
			PickImageDialog.build(PickSetup().setTitle("Wybierz zdjęcie").setCameraButtonText(
					"Aparat").setGalleryButtonText("Galeria").setCancelText("Anuluj"))
					.setOnPickResult { result ->
						iv_profile_picture.setImageBitmap(result.bitmap)
					}.show(mBaseActivity)
		}

		rv_profile_subjects.layoutManager = LinearLayoutManager(context)
		rv_profile_subjects.adapter = mPresenter.getAdapter()
		rv_profile_subjects.addItemDecoration(
				DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

		btn_profile_new_subject.setOnClickListener { mPresenter.onAddNewSubject() }
		btn_profile_submit.setOnClickListener { onSubmitClicked() }
		mPresenter.refreshSubjectsInfo()
	}

	override fun hideTutorFields() {
		rv_profile_subjects.setVisibility(false)
		btn_profile_new_subject.setVisibility(false)
	}

	private fun onSubmitClicked() {
		val firstName = et_profile_first_name.getStringValue()
		val lastName = et_profile_last_name.getStringValue()
		val address = et_profile_address.getStringValue()
		val city = et_profile_city.getStringValue()
		val description = et_profile_description.getStringValue()
		val photoDrawable = iv_profile_picture.drawable
		var photo = ""
		if (photoDrawable is BitmapDrawable) {
			photo = ImageUtil.convert(photoDrawable.bitmap)
		}
		mPresenter.onSubmitClicked(firstName, lastName, city, address, photo, description)
	}

	override fun updateProfile(user: User) {
		tv_profile_email.text = user.mEmail
		et_profile_description.setText(user.mDescription ?: "")
		et_profile_first_name.setText(user.mFirstName)
		et_profile_last_name.setText(user.mLastName)
		et_profile_address.setText(user.mAddress ?: "")
		et_profile_city.setText(user.mCity ?: "")
		user.mPhoto?.let {
			if (it.isBlank()) {
				return@let
			}
			iv_profile_picture.setImageBitmap(ImageUtil.convert(it))
		}
	}
}