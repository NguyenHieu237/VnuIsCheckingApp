package com.bkplus.scan_qrcode_barcode.model

import java.util.*

class QRCodeHistoryItem(
    val date: Date,
    var items: List<QRCodeHistoryItemChild>
) {
    fun addItem(item: QRCodeHistoryItemChild) {
        items += item
    }
}