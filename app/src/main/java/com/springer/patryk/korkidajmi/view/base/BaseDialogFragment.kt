package com.springer.patryk.korkidajmi.view.base

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.springer.patryk.korkidajmi.R
import timber.log.Timber
import java.util.*

abstract class BaseDialogFragment : DialogFragment(), BaseDialogFragmentView {

	protected abstract val layoutResId: Int
	private var mFragmentSnackbars: ArrayList<Snackbar>? = null
	private lateinit var mView: View
	private var mDialog: AlertDialog? = null

	val mBaseActivity: BaseActivity
		get() = activity as BaseActivity

	override fun onAttach(context: Context) {
		Timber.d("onAttach")
		super.onAttach(context)
		if (context !is BaseActivity) {
			throw IllegalArgumentException(
					"BaseFragment must be attached to BaseActivity. " + context.javaClass.name + " does not extends BaseActivity")
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		Timber.d("onCreate")
		super.onCreate(savedInstanceState)
		mFragmentSnackbars = ArrayList()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		Timber.d("onCreateView")
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
		val view = inflater.inflate(layoutResId, container, false)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		mView = view
		super.onViewCreated(view, savedInstanceState)
	}

	override fun onPause() {
		Timber.d("onPause")
		for (snackbar in mFragmentSnackbars!!) {
			snackbar.dismiss()
		}
		super.onPause()
	}

	override fun showToast(msgResId: Int) {
		if (!isAdded) {
			return
		}
		showToast(getString(msgResId))
	}

	override fun showToast(message: String) {
		if (!isAdded) {
			return
		}
		Toast.makeText(context, message, Toast.LENGTH_LONG).show()
	}

	/**
	 * @see .showFragmentSnackbar
	 */
	override fun showFragmentSnackbar(msgResId: Int) {
		if (!isAdded) {
			return
		}
		showFragmentSnackbar(getString(msgResId))
	}

	/**
	 * @see .showFragmentSnackbar
	 */
	fun showFragmentSnackbar(msgResId: Int, durationMs: Int) {
		if (!isAdded) {
			return
		}
		showFragmentSnackbar(getString(msgResId), durationMs)
	}

	/**
	 * @see .showFragmentSnackbar
	 */
	override fun showFragmentSnackbar(msg: String) {
		if (!isAdded) {
			return
		}
		showFragmentSnackbar(msg, null, null)
	}

	/**
	 * @see .showFragmentSnackbar
	 */
	fun showFragmentSnackbar(msg: String, durationMs: Int) {
		if (!isAdded) {
			return
		}
		showFragmentSnackbar(msg, null, null, durationMs)
	}

	/**
	 * @see .showFragmentSnackbar
	 */
	fun showFragmentSnackbar(msgResId: Int, actionMsgResId: Int,
							 actionCallback: View.OnClickListener) {
		if (!isAdded) {
			return
		}
		showFragmentSnackbar(getString(msgResId), getString(actionMsgResId), actionCallback,
				Snackbar.LENGTH_LONG)
	}

	/**
	 * @see .showFragmentSnackbar
	 */
	fun showFragmentSnackbar(msgResId: Int, actionMsgResId: Int,
							 actionCallback: View.OnClickListener, durationMs: Int) {
		if (!isAdded) {
			return
		}
		showFragmentSnackbar(getString(msgResId), getString(actionMsgResId), actionCallback,
				durationMs)
	}

	/**
	 * Shows snackbar that will be dismissed on fragment replacement
	 */
	fun showFragmentSnackbar(msg: String, actionMsg: String?,
							 actionCallback: View.OnClickListener?) {
		if (!isAdded) {
			return
		}
		showFragmentSnackbar(msg, actionMsg, actionCallback, Snackbar.LENGTH_LONG)
	}

	/**
	 * Shows snackbar that will be dismissed on fragment replacement
	 */
	fun showFragmentSnackbar(msg: String, actionMsg: String?, actionCallback: View.OnClickListener?,
							 durationMs: Int) {
		if (!isAdded) {
			return
		}
		val snackbar =
			Snackbar.make(mBaseActivity.findViewById(android.R.id.content), msg, durationMs)
		with(snackbar.view) {
			val snackbarTextId = android.support.design.R.id.snackbar_text
			val textView = this.findViewById(snackbarTextId) as TextView
			textView.setTextColor(Color.WHITE)
			this.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))

		}
		if (actionCallback != null) {
			snackbar.setAction(actionMsg, actionCallback).show()
		}
		snackbar.addCallback(object : Snackbar.Callback() {
			override fun onDismissed(snackbar: Snackbar?, event: Int) {
				super.onDismissed(snackbar, event)
				mFragmentSnackbars!!.remove(snackbar)
			}
		})
		mFragmentSnackbars!!.add(snackbar)
		snackbar.show()
	}

	/**
	 * @see .showSnackbar
	 */
	override fun showSnackbar(msgResId: Int) {
		if (!isAdded) {
			return
		}
		showSnackbar(getString(msgResId))
	}

	/**
	 * @see .showSnackbar
	 */
	override fun showSnackbar(msgResId: Int, durationMs: Int) {
		if (!isAdded) {
			return
		}
		showSnackbar(getString(msgResId), durationMs)
	}

	/**
	 * @see .showSnackbar
	 */
	override fun showSnackbar(msg: String) {
		if (!isAdded) {
			return
		}
		showSnackbar(msg, null, null)
	}

	/**
	 * @see .showSnackbar
	 */
	fun showSnackbar(msg: String, durationMs: Int) {
		if (!isAdded) {
			return
		}
		showSnackbar(msg, null, null, durationMs)
	}

	/**
	 * @see .showSnackbar
	 */
	fun showSnackbar(msgResId: Int, actionMsgResId: Int, actionCallback: View.OnClickListener) {
		if (!isAdded) {
			return
		}
		showSnackbar(getString(msgResId), getString(actionMsgResId), actionCallback)
	}

	/**
	 * @see .showSnackbar
	 */
	fun showSnackbar(msgResId: Int, actionMsgResId: Int, actionCallback: View.OnClickListener,
					 durationMs: Int) {
		if (!isAdded) {
			return
		}
		showSnackbar(getString(msgResId), getString(actionMsgResId), actionCallback, durationMs)
	}

	/**
	 * @see .showSnackbar
	 */
	fun showSnackbar(msg: String, actionMsg: String?, actionCallback: View.OnClickListener?) {
		if (!isAdded) {
			return
		}
		showSnackbar(msg, actionMsg, actionCallback, Snackbar.LENGTH_LONG)
	}

	/**
	 * Shows snackbar that will live longer then fragment
	 */
	fun showSnackbar(msg: String, actionMsg: String?, actionCallback: View.OnClickListener?,
					 durationMs: Int) {
		if (!isAdded) {
			return
		}
		val snackbar = Snackbar.make(view!!, msg, durationMs)
		with(snackbar.view) {
			val snackbarTextId = android.support.design.R.id.snackbar_text
			val textView = this.findViewById(snackbarTextId) as TextView
			textView.setTextColor(Color.WHITE)
			this.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))

		}
		if (actionCallback != null) {
			snackbar.setAction(actionMsg, actionCallback).show()
		}
		snackbar.show()
	}

	override fun showNetIndicator() {
		if (mDialog == null) {
			val builder: AlertDialog.Builder = AlertDialog.Builder(context)
			val alert =
				LayoutInflater.from(context).inflate(R.layout.dialog_net_indicator, null, false)
			builder.setView(alert)
			builder.setCancelable(false)
			mDialog = builder.create()
			mDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		}
		mDialog?.show()
	}

	override fun hideNetIndicator() {
		mDialog?.hide()
	}
}