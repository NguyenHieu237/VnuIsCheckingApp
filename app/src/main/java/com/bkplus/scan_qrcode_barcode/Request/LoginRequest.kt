package com.bkplus.scan_qrcode_barcode.Request

data class LoginRequest (
    var otp: Int,
    var email : String,
    var name: String,
)