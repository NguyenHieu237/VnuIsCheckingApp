package com.bkplus.scan_qrcode_barcode.model

data class Student(
    private val id: String? = null,
    private val fullName: String? = null,
    private val email: String? = null,
    private val deviceToken: String? = null,
    private val events : List<Event>
)

