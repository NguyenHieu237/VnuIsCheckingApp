package com.bkplus.scan_qrcode_barcode.ui.scanner.gallery

import java.util.*


class Album {
    var id = 0
    var name: String? = null
    var coverUri: String? = null
    var albumPhotos: Vector<Picture>? = null
        get() {
            if (field == null) {
                field = Vector<Picture>()
            }
            return field
        }
}

