package com.bkplus.scan_qrcode_barcode.manager.database

import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.TimeUtils
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class QRCodeTable: RealmObject() {
    @PrimaryKey
    private var id: Int = 0
    private var qrCodeImagePath: String = ""
    private var createAt: String = ""
    private var type: Int = QRCodeType.QRCODE.type
    private var resultType: Int = QRCodeResult.SCAN.type
    private var qrCodeContent: String = ""
    private var qrcodeBarcode: Int = 0
    private var qrCodeBarcodeType: Int = 0

    fun getId(): Int {
        return id
    }

    fun setPath(path: String) {
        this.qrCodeImagePath = path
    }

    fun getPath(): String {
        return this.qrCodeImagePath
    }

    fun setCreateAt(createAt: String) {
        this.createAt = createAt
    }

    fun getCreateAt(): String {
        return this.createAt
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun getType(): Int {
        return this.type
    }

    fun setResultType(type: Int) {
        this.resultType = type
    }

    fun getResultType(): Int {
        return this.resultType
    }

    fun setQRCodeContent(qrCodeContent: String) {
        this.qrCodeContent = qrCodeContent
    }

    fun getQRCodeContent(): String {
        return qrCodeContent
    }
    fun setQRCodeBarCode(qrcodeBarcode: Int) {
        this.qrcodeBarcode = qrcodeBarcode
    }
    fun getQRCodeBarCode(): Int {
        return qrcodeBarcode
    }
    fun setQRCodeBarCodeType(qrCodeBarcodeType: Int) {
        this.qrCodeBarcodeType = qrCodeBarcodeType
    }
    fun getQRCodeBarCodeType(): Int {
        return qrCodeBarcodeType
    }


    fun getTitleForUIHistory(): String {
        if (resultType == QRCodeResult.CREATE.type && qrCodeContent.contains("BEGIN:")) {
            return qrCodeContent.replace("BEGIN:", "")
        }
        return qrCodeContent
    }

    fun getDate(): Date {
        return TimeUtils.getDate(
            timeFormat = TimeUtils.TimeFormat.TimeFormat5,
            time = getCreateAt()
        )
    }
}