package pl.mauto24.app.view.base

import android.app.Activity
import android.content.Context

interface BaseFragmentView {
	fun getActivity(): Activity?

	fun getContext(): Context?

	fun showToast(msgResId: Int)

	fun showToast(message: String)

	fun showFragmentSnackbar(msgResId: Int)

	fun showFragmentSnackbar(msg: String)

	fun showSnackbar(msgResId: Int)

	fun showSnackbar(msgResId: Int, durationMs: Int)

	fun showSnackbar(msg: String)

	fun showNetIndicator()
	fun hideNetIndicator()
}