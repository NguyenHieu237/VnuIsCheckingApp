package com.bkplus.scan_qrcode_barcode.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ads.control.admob.AppOpenManager
import com.bkplus.scan_qrcode_barcode.Constants.MAX_MENU
import com.bkplus.scan_qrcode_barcode.Constants.MENU_CREATE
import com.bkplus.scan_qrcode_barcode.Constants.MENU_HISTORY
import com.bkplus.scan_qrcode_barcode.Constants.MENU_MANAGER
import com.bkplus.scan_qrcode_barcode.Constants.MENU_SCAN
import com.bkplus.scan_qrcode_barcode.Constants.MENU_SETTING
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentHomeNavBinding
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import com.bkplus.scan_qrcode_barcode.ui.generator.GeneratorFragment
import com.bkplus.scan_qrcode_barcode.ui.qrcode.ManagerTab.ListEventsFragment
import com.bkplus.scan_qrcode_barcode.ui.qrcode.create_event.CreateEventFragment
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.QRCodeHistoryFragment
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.StudentHistory.StudentHistoryFragment
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScannerFragment
import com.bkplus.scan_qrcode_barcode.ui.settings.SettingsFragment
import com.google.android.material.navigation.NavigationBarView

class HomeNavFragment : BaseFragment<FragmentHomeNavBinding>() {

    private var mScannerFragment: ScannerFragment? = null
    private var mCreateEventFragment: CreateEventFragment? = null
    private var mHistoryFragment: StudentHistoryFragment? = null
    private var mSettingsFragment: SettingsFragment? = null
    private var mEventsFragment: ListEventsFragment? = null
    private val currentFragment = MutableLiveData(-1)
    private val mOnNavigationItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->
            currentFragment.postValue(item.itemId)
            when (item.itemId) {
                R.id.menu_app_lock -> {
                    binding.viewPager2.currentItem = MENU_SCAN
                    return@OnItemSelectedListener true
                }

                R.id.menu_file_vault -> {
                    binding.viewPager2.currentItem = MENU_CREATE
                    return@OnItemSelectedListener true
                }

                R.id.menu_theme -> {
                    binding.viewPager2.currentItem = MENU_HISTORY
                    return@OnItemSelectedListener true
                }

                R.id.menu_settings -> {
                    binding.viewPager2.currentItem = MENU_SETTING
                    return@OnItemSelectedListener true
                }

                R.id.menu_manager -> {
                    binding.viewPager2.currentItem = MENU_MANAGER
                    return@OnItemSelectedListener true
                }

            }
            false
        }

    override val layoutId: Int
        get() = R.layout.fragment_home_nav

    override fun setupUI() {
        initiateNavigation()
    }

    private fun initiateNavigation() {
        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        binding.viewPager2.offscreenPageLimit = 2
        binding.viewPager2.adapter = pagerAdapter
        binding.bottomNavigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener)
        if(QRCodePreferences.getPrefsInstance().isAdmin) {
            binding.bottomNavigation.menu.findItem( R.id.menu_theme).isVisible = false
        }
        else{
            binding.bottomNavigation.menu.findItem( R.id.menu_manager).isVisible = false
            binding.bottomNavigation.menu.findItem( R.id.menu_file_vault).isVisible = false
        }
        binding.viewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentFragment.postValue(position)
                when (position) {
                    MENU_SCAN -> {
                        binding.bottomNavigation.menu.findItem(R.id.menu_app_lock).isChecked =
                            true
                    }

                    MENU_CREATE -> {
                        binding.bottomNavigation.menu.findItem(R.id.menu_file_vault).isChecked =
                            true
                    }

                    MENU_HISTORY -> {
                        binding.bottomNavigation.menu.findItem(R.id.menu_theme).isChecked =
                            true
                    }

                    MENU_SETTING -> {
                        binding.bottomNavigation.menu.findItem(R.id.menu_settings).isChecked =
                            true
                    }

                    MENU_MANAGER -> {
                        binding.bottomNavigation.menu.findItem(R.id.menu_manager).isChecked =
                            true
                    }
                }
            }
        })
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount() = MAX_MENU
        override fun createFragment(position: Int): Fragment =
            handleOnNavigationItemSelected(position)
    }

    private fun handleOnNavigationItemSelected(itemId: Int) = when (itemId) {
        MENU_SCAN -> getFragmentForIndex(MENU_SCAN)
        MENU_CREATE -> getFragmentForIndex(MENU_CREATE)
        MENU_HISTORY -> getFragmentForIndex(MENU_HISTORY)
        MENU_MANAGER -> getFragmentForIndex(MENU_MANAGER)
        else -> getFragmentForIndex(MENU_SETTING)
    }

    private fun initFragmentAt(position: Int): Fragment {
        when (position) {
            MENU_SCAN -> mScannerFragment = ScannerFragment.newInstance()
            MENU_CREATE -> mCreateEventFragment = CreateEventFragment.newInstance()
            MENU_HISTORY -> mHistoryFragment = StudentHistoryFragment.newInstance()
            MENU_MANAGER -> mEventsFragment = ListEventsFragment.newInstance()
            else -> mSettingsFragment = SettingsFragment.newInstance()
        }
        return handleOnNavigationItemSelected(position)
    }

    private fun getFragmentForIndex(index: Int) = when (index) {
        MENU_SCAN -> mScannerFragment ?: initFragmentAt(index)
        MENU_CREATE -> mCreateEventFragment ?: initFragmentAt(index)
        MENU_HISTORY -> mHistoryFragment ?: initFragmentAt(index)
        MENU_MANAGER -> mEventsFragment ?: initFragmentAt(index)
        else -> mSettingsFragment ?: initFragmentAt(index)
    }
}
