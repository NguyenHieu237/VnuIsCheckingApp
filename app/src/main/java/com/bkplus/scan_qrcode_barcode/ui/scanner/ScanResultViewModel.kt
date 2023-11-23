package com.bkplus.scan_qrcode_barcode.ui.scanner

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeDAO
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeModel
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.TimeUtils
import com.bkplus.scan_qrcode_barcode.utils.createFileImageShare
import com.bkplus.scan_qrcode_barcode.utils.extension.guardLet
import com.bkplus.scan_qrcode_barcode.utils.storeImage
import com.google.mlkit.vision.barcode.common.Barcode
import java.io.File
import java.util.Date
import javax.inject.Inject

class ScanResultViewModel @Inject constructor(): ViewModel() {
    private var _result: GenerateQRCodeResult? = null
    var barcode: Barcode? = null

    fun setResult(result: GenerateQRCodeResult?) {
        this._result = result
    }

    fun getQRCodeBitmap(): Bitmap? {
        return _result?.qrCodeBitmap
    }

    fun getContent(): String? {
        return _result?.qrCodeContent
    }

    fun getFileToShare(context: Context): File? {
        val result = _result.guardLet { return null }!!
        return createFileImageShare(
            image = result.qrCodeBitmap,
            context = context
        )
    }

    fun saveDataQRCode(context: Context) {
        val result = _result.guardLet { return }!!

        /// Validate
        if (!result.needInsert) { return }

        var qrCodeImagePath = ""
//        if (result.resultType != QRCodeResult.CREATE.type) {
            qrCodeImagePath = storeImage(result.qrCodeBitmap, context = context).guardLet { return }!!
//        }


        ///TEST
//        var dateTest = TimeUtils.getDate(timeFormat = TimeUtils.TimeFormat.TimeFormat5, time = "12/30/2023")

        val model = QRCodeModel(
            type = result.qrCodeType,
            path = qrCodeImagePath,
            createAt = TimeUtils.getTime(
                timeFormat = TimeUtils.TimeFormat.TimeFormat4,
                time = Date()
            ),
            resultType = result.resultType,
            qrCodeContent = result.qrCodeContent,
            qrcodeBarcode = barcode!!.format,
            qrCodeBarcodeType = barcode!!.valueType
        )

        QRCodeDAO.instance.insertDataQRCode(model = model)
    }
}