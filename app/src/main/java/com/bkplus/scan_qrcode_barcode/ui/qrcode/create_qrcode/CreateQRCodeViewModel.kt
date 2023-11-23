package com.bkplus.scan_qrcode_barcode.ui.qrcode.create_qrcode

import androidx.lifecycle.ViewModel
import com.bkplus.scan_qrcode_barcode.model.GeneratorItem
import javax.inject.Inject

class CreateQRCodeViewModel @Inject constructor(): ViewModel() {
    private var _generatorItem: GeneratorItem? = null

    fun setItem(item: GeneratorItem?) {
        this._generatorItem = item
    }

    fun getGeneratorItem(): GeneratorItem? {
        return _generatorItem
    }
}