package com.bkplus.scan_qrcode_barcode.ui.splash.onboarding.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.FragmentOnboardFirstBinding

class OnboardFirstFragment : Fragment() {
    private lateinit var binding: FragmentOnboardFirstBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_onboard_first, container, false)
        return binding.root
    }
}
