package com.bkplus.scan_qrcode_barcode.ui.generator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentGeneratorBinding
import com.bkplus.scan_qrcode_barcode.model.GeneratorItem
import com.bkplus.scan_qrcode_barcode.ui.generator.barcodegenerator.BarcodeGeneratorFragment
import com.bkplus.scan_qrcode_barcode.ui.generator.qrcodegenerator.QRCodeGeneratorFragment
import com.bkplus.scan_qrcode_barcode.ui.home.HomeNavFragmentDirections
import com.bkplus.scan_qrcode_barcode.utils.NetworkUtil
import com.bkplus.scan_qrcode_barcode.utils.RemoteUtils

class GeneratorFragment: BaseFragment<FragmentGeneratorBinding>() {
    private lateinit var _adapter: GeneratorAdapter
    private val viewModel by viewModels<GeneratorViewModel>()

    private var mQRFragment: QRCodeGeneratorFragment? = null
    private var mBarFragment: BarcodeGeneratorFragment? = null

    override val layoutId: Int
        get() = R.layout.fragment_generator

    companion object {
        fun newInstance(): GeneratorFragment {
            val args = Bundle()
            val fragment = GeneratorFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupUI() {
        setupViewpager()
        setupRecyclerView()
        loadAdsBanner()
    }

    private fun setupRecyclerView() {
        _adapter = GeneratorAdapter(viewModel.makeListItem(), onTapItemListener = { generatorItem ->
            makeUICreateQRCode(generatorItem = generatorItem)
        })
    }

    // TODO Load ads
    private fun loadAdsBanner() {
        /// Validate
        if (!RemoteUtils.isShowQRCodeMenuBanner ||
            !NetworkUtil.isInternetAvailable(requireContext()))
        {
            return
        }
//
//        binding.ctlAdsBannerManager.visible()
//        adBannerView = AdView(requireContext())
//        adBannerView?.let {
//            binding.adViewContainer.addView(it)
//            val adRequest = AdRequest.Builder().build()
//            it.adUnitId = BuildConfig.QRCode_Menu_Banner
//            it.setAdSize(adBannerSize)
//            it.adListener = object : AdListener() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    binding.ctlAdsBannerManager.gone()
//                }
//
//                override fun onAdLoaded() {
//                    binding.adViewContainer.visible()
//                    binding.icShimmerBanner.root.gone()
//                }
//            }
//            it.loadAd(adRequest)
//        }
    }

    // TODO Navigation
    private fun makeUICreateQRCode(generatorItem: GeneratorItem) {
        val direction = HomeNavFragmentDirections.actionGoToCreateQRCodeFragment(generatorItem)
        findNavController().navigate(direction)
    }

    private fun setupViewpager(){
        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        binding.pager2.offscreenPageLimit = 2
        binding.pager2.adapter = pagerAdapter
        binding.toolQr.setOnClickListener {clickMenuQR()}
        binding.toolBar.setOnClickListener {clickMenuBar()}

        binding.pager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        clickMenuQR()
                    }
                    1 -> {
                        clickMenuBar()
                    }
                }
            }
        })
    }
    private fun clickMenuQR() {
        binding.toolQr.setTextColor(resources.getColor(R.color.colorPrimary,null))
        binding.bottomLine1.visibility = View.VISIBLE
        binding.toolBar.setTextColor(resources.getColor(R.color.neutral_50,null))
        binding.bottomLine2.visibility = View.INVISIBLE

        binding.pager2.currentItem = 0
    }

    private fun clickMenuBar() {
        binding.toolBar.setTextColor(resources.getColor(R.color.colorPrimary,null))
        binding.bottomLine1.visibility = View.INVISIBLE
        binding.toolQr.setTextColor(resources.getColor(R.color.neutral_50,null))
        binding.bottomLine2.visibility = View.VISIBLE

        binding.pager2.currentItem = 1
    }
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount() = 2
        override fun createFragment(position: Int): Fragment =
            handleOnNavigationItemSelected(position)
    }

    private fun handleOnNavigationItemSelected(itemId: Int) = when (itemId) {
        1 -> getFragmentForIndex(1)
        else -> getFragmentForIndex(0)
    }

    private fun initFragmentAt(position: Int): Fragment {
        when (position) {
            1 -> mBarFragment = BarcodeGeneratorFragment.newInstance()
            else -> mQRFragment = QRCodeGeneratorFragment.newInstance()
        }
        return handleOnNavigationItemSelected(position)
    }

    private fun getFragmentForIndex(index: Int) = when (index) {
        1 -> mBarFragment ?: initFragmentAt(index)
        else -> mQRFragment ?: initFragmentAt(index)
    }
}