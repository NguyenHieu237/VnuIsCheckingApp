package com.bkplus.scan_qrcode_barcode.ui.generator.qrcodegenerator

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentToolQrcodeBinding
import com.bkplus.scan_qrcode_barcode.model.GeneratorItem
import com.bkplus.scan_qrcode_barcode.ui.generator.GeneratorViewModel
import com.bkplus.scan_qrcode_barcode.ui.generator.GeneratorAdapter
import com.bkplus.scan_qrcode_barcode.ui.home.HomeNavFragmentDirections

class QRCodeGeneratorFragment(override val layoutId: Int = R.layout.fragment_tool_qrcode) : BaseFragment<FragmentToolQrcodeBinding>() {
    private val viewModel by viewModels<GeneratorViewModel>()
    companion object {
        @JvmStatic
        fun newInstance() =
            QRCodeGeneratorFragment()
    }

    override fun setupUI() {
        binding.listPersonal.adapter = GeneratorAdapter(viewModel.makeListItem(), onTapItemListener = { generatorItem ->
            makeUICreateQRCode(generatorItem = generatorItem)
        })
        binding.listSocial.adapter = GeneratorAdapter(viewModel.makeListItem2(), onTapItemListener = { generatorItem ->
            makeUICreateQRCode(generatorItem = generatorItem)
        })

    }

    private fun makeUICreateQRCode(generatorItem: GeneratorItem) {
        val direction = HomeNavFragmentDirections.actionGoToCreateQRCodeFragment(generatorItem)
        findNavController().navigate(direction)
    }
}