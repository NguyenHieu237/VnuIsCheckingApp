package com.bkplus.scan_qrcode_barcode.ui.qrcode.history.adapter

import com.bkplus.scan_qrcode_barcode.model.QRCodeHistoryItemChild

interface QRCodeHistoryItemListener {
    fun onTapItem(item: QRCodeHistoryItemChild)
    fun onDeleteItem(item: QRCodeHistoryItemChild)
    fun onShareItem(item: QRCodeHistoryItemChild)

}
