package com.bkplus.scan_qrcode_barcode.manager.database

import io.realm.Realm

class QRCodeDAO {
    companion object {
        val instance: QRCodeDAO = QRCodeDAO()
    }

    private val _realm: Realm = Realm.getDefaultInstance()

    fun insertDataQRCode(
        model: QRCodeModel
    ) {
        _realm.beginTransaction()
        val qrCodeTable = _realm.createObject(QRCodeTable::class.java, generateId())
        qrCodeTable.setPath(model.path)
        qrCodeTable.setCreateAt(model.createAt)
        qrCodeTable.setType(model.type)
        qrCodeTable.setResultType(model.resultType)
        qrCodeTable.setQRCodeContent(model.qrCodeContent)
        qrCodeTable.setQRCodeBarCode(model.qrcodeBarcode)
        qrCodeTable.setQRCodeBarCodeType(model.qrCodeBarcodeType)
        _realm.commitTransaction()
    }

    fun getListQRCode(): List<QRCodeTable> {
        val items = _realm.where(QRCodeTable::class.java).findAll()
        return items.toList()
    }

    /// Find max id in list qrcode local
    private fun maxId(): Int {
        return if (getListQRCode().isEmpty()) 0 else getListQRCode().map { it.getId() }.max()
    }

    private fun generateId(): Int {
        return maxId() + 1
    }

    fun deleteAll() {
        _realm.executeTransaction { r: Realm ->
            r.deleteAll()
        }
    }
    fun deleteItem(id: Int) {
        val items = _realm.where(QRCodeTable::class.java)
            .equalTo("id",id)
            .findFirst()

        _realm.executeTransaction {
            items?.deleteFromRealm()
        }
    }
}
