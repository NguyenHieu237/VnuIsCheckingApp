package com.bkplus.scan_qrcode_barcode.Response

data class OTPResponse (
    val status: Int,
    val message: String,
    val data: Any?
)