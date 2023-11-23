package com.bkplus.scan_qrcode_barcode.ui.generator.barcodegenerator

import androidx.lifecycle.ViewModel
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.model.BarcodeTypeEnum
import com.bkplus.scan_qrcode_barcode.model.GeneratorItem
import javax.inject.Inject

class BarcodeViewModel @Inject constructor(): ViewModel() {
    fun makeListItem():ArrayList<GeneratorItem>{
        val items = ArrayList<GeneratorItem>()
        // Barcode
        items.add(GeneratorItem(R.string.ean8,R.drawable.ic_barcode_green,type = BarcodeTypeEnum.EAN_8.type))
        items.add(GeneratorItem(R.string.ean13,R.drawable.ic_barcode_green,type = BarcodeTypeEnum.EAN_13.type))
        items.add(GeneratorItem(R.string.upce,R.drawable.ic_barcode_green,type = BarcodeTypeEnum.UPC_E.type))
        items.add(GeneratorItem(R.string.upca,R.drawable.ic_barcode_green,type = BarcodeTypeEnum.UPC_A.type))
        items.add(GeneratorItem(R.string.code39,R.drawable.ic_barcode_green,type = BarcodeTypeEnum.CODE_39.type))
        items.add(GeneratorItem(R.string.code93,R.drawable.ic_barcode_green,type = BarcodeTypeEnum.CODE_93.type))
        items.add(GeneratorItem(R.string.code128,R.drawable.ic_barcode_green,type = BarcodeTypeEnum.CODE_128.type))
        items.add(GeneratorItem(R.string.itf,R.drawable.ic_barcode_green,type = BarcodeTypeEnum.ITF.type))
        items.add(GeneratorItem(R.string.codabar,R.drawable.ic_barcode_green,type = BarcodeTypeEnum.CODABAR.type))
        items.add(GeneratorItem(R.string.pdf417,R.drawable.ic_pdf_417,type = BarcodeTypeEnum.PDF_417.type))
        items.add(GeneratorItem(R.string.data_matrix,R.drawable.ic_data_matrix,type = BarcodeTypeEnum.DATA_MATRIX.type))
        items.add(GeneratorItem(R.string.aztec,R.drawable.ic_aztec,type = BarcodeTypeEnum.AZTEC.type))
        return items
    }
}