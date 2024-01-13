package com.bkplus.scan_qrcode_barcode.ui.Login

import android.text.InputType
import android.util.Log
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.applovin.impl.sdk.c.b.bi
import com.applovin.impl.sdk.c.b.em
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentLogInBinding
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.QRCodeHistoryViewModel

class LoginFragment : BaseFragment<FragmentLogInBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_log_in

    private val viewModel by activityViewModels<LoginViewModel>()
    override fun setupUI() {
        super.setupUI()
        binding.studentId.setHint("Your Student Id")
        binding.email.setHint("Your Email")
        binding.studentName.setHint("Your Full Name")
        binding.studentOtp.setHint("Otp you just received")
    }

    override fun setupListener() {
        super.setupListener()

        binding.nextBtn.setOnClickListener {
            var name= binding.studentName.getContent()
            var email = binding.email.getContent()
            var id = binding.studentId.getContent().toInt()
            QRCodePreferences.getPrefsInstance().isAdmin = id.toString() == "999999"
           viewModel.makeApiCall(email,name,id)
            binding.inputLayout.visibility = View.GONE
            binding.otpLayout.visibility = View.VISIBLE
        }
        binding.LoginBtn.setOnClickListener {
            var otp = binding.studentOtp.getContent().toInt()
            var name= binding.studentName.getContent()
            var email = binding.email.getContent()
            viewModel.loginByOTP(otp,email, name)
            findNavController().navigate(R.id.gohome)

        }
    }

}