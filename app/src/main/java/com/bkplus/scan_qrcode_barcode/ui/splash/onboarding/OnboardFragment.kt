package com.bkplus.scan_qrcode_barcode.ui.splash.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ads.control.admob.AppOpenManager
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.FragmentOnboardingBinding
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import com.bkplus.scan_qrcode_barcode.ui.splash.onboarding.screen.OnboardFirstFragment
import com.bkplus.scan_qrcode_barcode.ui.splash.onboarding.screen.OnboardSecondFragment
import com.bkplus.scan_qrcode_barcode.ui.splash.onboarding.screen.OnboardThirdFragment

class OnboardFragment : Fragment() {

    private lateinit var onboardBinding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        onboardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding, container, false)
        return onboardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppOpenManager.getInstance().enableAppResume()
        setupViewPager()
    }

    private fun setupViewPager() {
        activity?.let {
            it.window.statusBarColor = ContextCompat.getColor(it, R.color.transparent)

            val decor: View = it.window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        val fragmentList = arrayListOf(
            OnboardFirstFragment(), OnboardSecondFragment(), OnboardThirdFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList, requireActivity().supportFragmentManager, lifecycle
        )

        onboardBinding.viewPager.adapter = adapter

        onboardBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                onboardBinding.pageIndicatorViewBanner.onPageSelected(position)

                when (position) {
                    0 -> onboardBinding.txtNext.text = resources.getString(R.string.next)
                    1 -> onboardBinding.txtNext.text = resources.getString(R.string.next)
                    2 -> onboardBinding.txtNext.text = resources.getString(R.string.get_start)
                }
            }
        })

        onboardBinding.txtNext.setOnClickListener {
            if (onboardBinding.viewPager.currentItem < 2) {
                onboardBinding.viewPager.currentItem =
                    onboardBinding.viewPager.currentItem + 1
            } else {
                QRCodePreferences.getPrefsInstance().isViewedOnboard = true
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_splash_fragment)
                navController.navigate(R.id.openMain)
                requireActivity().finish()
            }
        }
    }

    inner class ViewPagerAdapter(
        list: ArrayList<Fragment>,
        fm: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fm, lifecycle) {
        private val fragmentList = list

        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }
    }
}
