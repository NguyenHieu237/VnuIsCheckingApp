package com.bkplus.scan_qrcode_barcode.model

enum class BarcodeTypeEnum(val type: Int) {
    EAN_8(11),
    EAN_13(12),
    UPC_E(13),
    UPC_A(14),
    CODE_39(15),
    CODE_93(16),
    CODE_128(17),
    ITF(18),
    CODABAR(19),
    PDF_417(20),
    DATA_MATRIX(21),
    AZTEC(22);

    companion object{
        fun instance(type: Int) = when (type) {
            11 -> EAN_8
            12 -> EAN_13
            13 -> UPC_E
            14 -> UPC_A
            15 -> CODE_39
            16 -> CODE_93
            17 -> CODE_128
            18 -> ITF
            19 -> CODABAR
            20 -> PDF_417
            21 -> DATA_MATRIX
            else -> AZTEC
        }
    }




}