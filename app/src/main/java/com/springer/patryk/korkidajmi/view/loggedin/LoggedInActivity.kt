package com.springer.patryk.korkidajmi.view.loggedin

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.springer.patryk.korkidajmi.Application
import com.springer.patryk.korkidajmi.R
import com.springer.patryk.korkidajmi.model.helpers.PreferencesHelper
import com.springer.patryk.korkidajmi.model.network.endpoints.SubmissionApi
import com.springer.patryk.korkidajmi.view.base.BaseActivity
import com.springer.patryk.korkidajmi.view.loggedin.mylessons.MyLessonsFragment
import com.springer.patryk.korkidajmi.view.loggedin.mylessons.createlesson.CreateLessonFragment
import com.springer.patryk.korkidajmi.view.loggedin.profile.ProfileFragment
import com.springer.patryk.korkidajmi.view.loggedin.search.lessonlist.LessonListFragment
import com.springer.patryk.korkidajmi.view.loggedin.search.setsearchlevel.LessonLevelFragment
import com.springer.patryk.korkidajmi.view.loggedin.submission.SubmissionFragment
import com.springer.patryk.korkidajmi.view.notloggedin.NotLoggedInActivity
import kotlinx.android.synthetic.main.activity_logged_in.*
import kotlinx.android.synthetic.main.drawer_header.view.*

class LoggedInActivity : LoggedInContract.View, BaseActivity(),
		NavigationView.OnNavigationItemSelectedListener {


	override val mLayoutResId: Int
		get() = R.layout.activity_logged_in
	lateinit var mDrawerToggle: ActionBarDrawerToggle
	private lateinit var mHeaderViewHolder: NavHeaderViewHolder
	public lateinit var mPresenter: LoggedInContract.Presenter
	private var mNotificationView: View? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setSupportActionBar(tb_main_toolbar)

		val toolbar: ActionBar? = supportActionBar
		mPresenter = LoggedInPresenter(this,
				(application as Application).getRetrofitInstance().create(
						SubmissionApi::class.java))
		toolbar?.let {
			it.setDisplayHomeAsUpEnabled(true)
			mDrawerToggle = object : ActionBarDrawerToggle(this, dl_drawer_menu, tb_main_toolbar,
					R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
				override fun onDrawerClosed(drawerView: View) {
					invalidateOptionsMenu()
				}

				override fun onDrawerOpened(drawerView: View) {
					invalidateOptionsMenu()
				}
			}
			mDrawerToggle.isDrawerIndicatorEnabled = true
			dl_drawer_menu.addDrawerListener(mDrawerToggle)
			mDrawerToggle.syncState()
		}
		mDrawerToggle.setToolbarNavigationClickListener { onBackPressed() }
		nv_drawer_navigation.setNavigationItemSelectedListener(this)
		nv_drawer_navigation.setCheckedItem(R.id.menu_calendar)
		mHeaderViewHolder = NavHeaderViewHolder(nv_drawer_navigation.getHeaderView(0))
	}

	override fun onPostCreate(savedInstanceState: Bundle?) {
		super.onPostCreate(savedInstanceState)
		setContent(MyLessonsFragment())
	}

	override fun shouldDisplayHomeUp() {
		var canGoBack: Boolean = supportFragmentManager.backStackEntryCount > 0
		canGoBack = canGoBack || parentActivityIntent != null
		val actionBar: ActionBar? = supportActionBar
		actionBar?.let {
			mDrawerToggle.isDrawerIndicatorEnabled = !canGoBack

			dl_drawer_menu.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
			if (canGoBack) {
				dl_drawer_menu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
			}
		}
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.menu_calendar -> {
				if (mCurrentFragment !is MyLessonsFragment) {
					setContent(MyLessonsFragment())
					nv_drawer_navigation.setCheckedItem(R.id.menu_calendar)
				}
			}
			R.id.menu_logout -> {
				PreferencesHelper.setUserInfo(this, null)
				startActivity(Intent(this, NotLoggedInActivity::class.java))
				finish()
			}
			R.id.menu_profile -> {
				if (mCurrentFragment !is ProfileFragment) {
					setContent(ProfileFragment())
					nv_drawer_navigation.setCheckedItem(R.id.menu_calendar)
				}
			}
			R.id.menu_add_lesson -> {
				mCurrentFragment?.let {
					addChildContent(CreateLessonFragment.newInstance(null, it))
				}
			}
			R.id.menu_search -> {
				if (mCurrentFragment !is LessonLevelFragment) {
					setContent(LessonLevelFragment())
					nv_drawer_navigation.setCheckedItem(R.id.menu_search)
				}
			}
		}
		dl_drawer_menu.closeDrawers()
		return true
	}

	override fun onBackStackChanged() {
		super.onBackStackChanged()
		mCurrentFragment?.onResume()
		val menuId = when (mCurrentFragment) {
			is ProfileFragment -> R.id.menu_profile
			is MyLessonsFragment -> R.id.menu_calendar
			is LessonListFragment -> R.id.menu_search
			else -> -1
		}
		nv_drawer_navigation.setCheckedItem(menuId)
	}

	override fun onConfigurationChanged(newConfig: Configuration) {
		super.onConfigurationChanged(newConfig)
		mDrawerToggle.onConfigurationChanged(newConfig)
	}

	override fun onResume() {
		super.onResume()
		mPresenter.refreshUser()
	}

	override fun getContext(): Context? = this
	override fun updateUser(name: String, isTutor: Boolean) {
		mHeaderViewHolder.bind(name)
		if (!isTutor) {
			nv_drawer_navigation.menu.findItem(R.id.menu_add_lesson).isVisible = false
		}
	}

	override fun onBackPressed() {
		if (dl_drawer_menu.isDrawerOpen(GravityCompat.START)) {
			dl_drawer_menu.closeDrawer(GravityCompat.START)
		} else {
			if (supportFragmentManager.backStackEntryCount > 0) {
				supportFragmentManager.popBackStack()
				return
			} else if (mCurrentFragment !is MyLessonsFragment) {
				setContent(MyLessonsFragment())
				nv_drawer_navigation.setCheckedItem(R.id.menu_calendar)
				return
			}
			super.onBackPressed()
		}
	}

	fun setToolbarTitle(titleId: Int) {
		tb_main_toolbar.title = getString(titleId)
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {

		if (mCurrentFragment is MyLessonsFragment) {
			menuInflater.inflate(R.menu.lessons_menu, menu)
			val menuItem: MenuItem? = menu?.findItem(R.id.menu_lessons_notifications)
			mNotificationView = menuItem?.actionView
			mNotificationView?.setOnClickListener {
				setChildContent(SubmissionFragment())
			}
		} else {
			menuInflater.inflate(R.menu.toolbar_menu, menu)
			val menuItem: MenuItem? = menu?.findItem(R.id.toolbar_menu_notifications)
			mNotificationView = menuItem?.actionView
			mNotificationView?.setOnClickListener {
				setChildContent(SubmissionFragment())
			}
		}
		mPresenter.refreshSubmissionCount()
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (mCurrentFragment is MyLessonsFragment) {
			when (item?.itemId) {
				R.id.menu_lessons_calendar -> (mCurrentFragment as MyLessonsFragment).mPresenter.onCalendarViewSelected()
				R.id.menu_lessons_list -> (mCurrentFragment as MyLessonsFragment).mPresenter.onListViewSelected()
			}
		}
		return true
	}

	override fun updateUnreadCount(count: Int) {
		val notificationCount: TextView? = this?.findViewById(R.id.tv_unread_notification_count)
		notificationCount?.text = count.toString()
	}

	class NavHeaderViewHolder(private val headerView: View) {
		fun bind(userName: String) = with(headerView) {
			tv_nav_header_username.text = "$userName!"
		}
	}
}