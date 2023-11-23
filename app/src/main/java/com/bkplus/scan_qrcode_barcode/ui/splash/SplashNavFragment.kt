package com.bkplus.scan_qrcode_barcode.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.FragmentSplashNavBinding

class SplashNavFragment : Fragment() {

    private lateinit var splashNavBinding: FragmentSplashNavBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        splashNavBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_splash_nav, container, false)
        return splashNavBinding.root
    }
}
