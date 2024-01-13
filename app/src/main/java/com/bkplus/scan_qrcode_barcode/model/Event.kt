package com.bkplus.scan_qrcode_barcode.model
data class Event (
    private val id: String? = null,
    private val name: String? = null,
    private val address: String? = null,
    private val oganizer: String? = null,
    private val students: List<Student>
)