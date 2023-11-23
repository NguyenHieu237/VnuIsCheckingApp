package com.bkplus.scan_qrcode_barcode.model

import com.ads.control.ads.wrapper.ApNativeAd
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeTable

class QRCodeHistoryItemChild(
    val type: HistoryItemType,
    val itemData: QRCodeTable? = null,
    val itemAds: ApNativeAd? = null
)