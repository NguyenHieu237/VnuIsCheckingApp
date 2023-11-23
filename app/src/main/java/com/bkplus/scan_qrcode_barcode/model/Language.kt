package com.bkplus.scan_qrcode_barcode.model

data class Language(
    val codeLanguage: String,
    val nameLanguageRes: Int?,
    val imageRes: Int?,
    var isSelected: Boolean = false
)