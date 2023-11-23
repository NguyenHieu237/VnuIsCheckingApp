package com.bkplus.scan_qrcode_barcode.ui.generator.barcodegenerator

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentToolBarcodeBinding
import com.bkplus.scan_qrcode_barcode.model.GeneratorItem
import com.bkplus.scan_qrcode_barcode.ui.home.HomeNavFragmentDirections

class BarcodeGeneratorFragment (override val layoutId: Int = R.layout.fragment_tool_barcode) : BaseFragment<FragmentToolBarcodeBinding>() {
    private val viewModel by viewModels<BarcodeViewModel>()

    companion object {
        @JvmStatic
        fun newInstance() =
            BarcodeGeneratorFragment()
    }

    override fun setupUI() {
        binding.listBarcode.adapter = BarcodeAdapter(viewModel.makeListItem(),onTapItemListener = { generatorItem ->
            makeUICreateQRCode(generatorItem = generatorItem)
        })
    }

    private fun makeUICreateQRCode(generatorItem: GeneratorItem) {
        val direction = HomeNavFragmentDirections.actionGoToCreateQRCodeFragment(generatorItem)
        findNavController().navigate(direction)
    }

}