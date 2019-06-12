package com.springer.patryk.korkidajmi.view.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.NavUtils
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.springer.patryk.korkidajmi.R
import kotlinx.android.synthetic.main.base_content.*
import pl.mauto24.app.view.base.BaseFragment
import pl.mauto24.app.view.base.FragmentWithNavigateUpSupport
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

	val mCurrentFragment: BaseFragment?
		get() {
			val fm: FragmentManager = supportFragmentManager
			return getFragmentAt(fm.backStackEntryCount) as? BaseFragment
		}

	protected abstract val mLayoutResId: Int

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		super.setContentView(mLayoutResId)
		Timber.d("onCreate")
		supportFragmentManager.addOnBackStackChangedListener(this)
	}

	override fun setContentView(layoutResID: Int) {
		Timber.d("setContentView")
		layoutInflater.inflate(layoutResID, fl_content, true)
	}

	override fun onSupportNavigateUp(): Boolean {
		val fragmentWasPopped: Boolean = supportFragmentManager.popBackStackImmediate()
		if (fragmentWasPopped) {
			hideKeyboard()
			return true
		}
		val parentIntent: Intent? = parentActivityIntent
		if (parentIntent != null) {
			NavUtils.navigateUpFromSameTask(this)
		} else {
			finishAffinity()
		}
		hideKeyboard()
		return true
	}

	fun hideKeyboard() {
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
		currentFocus?.let { focusedView ->
			val inputMethodManager: InputMethodManager =
				getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			inputMethodManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
		}
	}

	private fun getFragmentAt(index: Int): Fragment? {
		val fm: FragmentManager = supportFragmentManager
		val indexString = index.toString()
		Timber.d("getFragmentAt idxTag: $indexString in fragmentManger: $fm")
		val result: Fragment? = fm.findFragmentByTag(indexString)
		Timber.d("getFragmentAt (with tag) $index : $result")
		return result
	}

	override fun startActivity(intent: Intent?) {
		super.startActivity(intent)
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
	}

	override fun onBackStackChanged() {
		shouldDisplayHomeUp()
	}

	open fun shouldDisplayHomeUp() {
		var canGoBack: Boolean = supportFragmentManager.backStackEntryCount > 0
		canGoBack = canGoBack || parentActivityIntent != null
		val actionBar: ActionBar? = supportActionBar
		actionBar?.let {
			it.setHomeButtonEnabled(true)
			if (canGoBack) {
				it.setDisplayHomeAsUpEnabled(canGoBack)
			}
		}
		Timber.d("setDisplayHomeAsUpEnabled $canGoBack")
	}

	fun setContent(noveltyFragment: BaseFragment) {
		supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
		setFragment(noveltyFragment, false)
	}

	fun setChildContent(newFragment: BaseFragment) {
		setFragment(newFragment, true)
	}

	open fun setFragment(newFragment: BaseFragment, child: Boolean) {
		val fm = supportFragmentManager
		val fragmentTransaction = fm.beginTransaction()
		fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
				android.R.anim.fade_in, android.R.anim.fade_out)
		var backStackIdx = fm.backStackEntryCount
		if (child) {
			fragmentTransaction.addToBackStack(null)
			backStackIdx++
		}
		val tag = backStackIdx.toString()
		Timber.d("Adding fragment with tag $tag")
		fragmentTransaction.replace(R.id.fl_content, newFragment, tag)
		fragmentTransaction.commit()
		hideKeyboard()
		fm.executePendingTransactions()
	}

	fun addChildContent(newFragment: BaseFragment) {
		addFragment(newFragment, true)
	}

	private fun addFragment(newFragment: BaseFragment, child: Boolean) {
		val fm = supportFragmentManager
		val fragmentTransaction = fm.beginTransaction()
		fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
				android.R.anim.fade_in, android.R.anim.fade_out)
		var backStackIdx = fm.backStackEntryCount
		if (child) {
			fragmentTransaction.addToBackStack(null)
			backStackIdx++
		}
		val tag = backStackIdx.toString()
		Timber.d("Adding fragment with tag $tag")
		fragmentTransaction.add(R.id.fl_content, newFragment, tag)
		fragmentTransaction.commit()
		hideKeyboard()
		fm.executePendingTransactions()
	}

	override fun onBackPressed() {
		Timber.d("onBackPressed")
		if (mCurrentFragment?.mParentView == null) {
			super.onBackPressed()
			return
		} else {
			setContent(mCurrentFragment?.mParentView!!)
			return
		}

		super.onBackPressed()
	}

	fun moveBackInBackstack() {
		val current = mCurrentFragment
		if (current is FragmentWithNavigateUpSupport) {
			val navUp = current as FragmentWithNavigateUpSupport
			val handled = navUp.navigateUp()
			if (handled) {
				return
			}
		}
	}

	override fun onResume() {
		super.onResume()
		hideKeyboard()
	}
}