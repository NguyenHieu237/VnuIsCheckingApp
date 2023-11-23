package com.bkplus.scan_qrcode_barcode.manager.database

enum class QRCodeType(val type: Int) {
    BARCODE(1),
    QRCODE(2);

}

enum class QRCodeResult(val type: Int) {
    CREATE(1),
    SCAN(2)
}

class QRCodeModel(
    val type: Int,
    val path: String,
    val createAt: String,
    val resultType: Int = QRCodeResult.SCAN.type,
    val qrCodeContent: String = "",
    val qrcodeBarcode: Int,
    val qrCodeBarcodeType: Int,
)