package com.bkplus.scan_qrcode_barcode.ui.qrcode.history

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.ads.control.ads.wrapper.ApNativeAd
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeDAO
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeTable
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.model.HistoryItemType
import com.bkplus.scan_qrcode_barcode.model.QRCodeHistoryItem
import com.bkplus.scan_qrcode_barcode.model.QRCodeHistoryItemChild
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.adapter.QRCodeHistoryAdapter
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.TimeUtils
import com.bkplus.scan_qrcode_barcode.utils.RemoteUtils
import com.bkplus.scan_qrcode_barcode.utils.createFileImageShare
import com.bkplus.scan_qrcode_barcode.utils.getBitmapFromPath
import com.bkplus.scan_qrcode_barcode.utils.extension.guardLet
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.File
import javax.inject.Inject

class QRCodeHistoryViewModel @Inject constructor() : ViewModel() {
    lateinit var _adapter: QRCodeHistoryAdapter
    private val _qrCodeHistoryItemsSubject: PublishSubject<List<QRCodeHistoryItem>> =
        PublishSubject.create()
    private val _loadItemSubject: PublishSubject<GenerateQRCodeResult> = PublishSubject.create()
    private var _nativeAds: ApNativeAd? = null

    fun getQRCodeHistoryItemsObservable(): Observable<List<QRCodeHistoryItem>> {
        return _qrCodeHistoryItemsSubject
    }

    fun loadItemCompletionObservable(): Observable<GenerateQRCodeResult> {
        return _loadItemSubject
    }

    fun setNativeAds(nativeAd: ApNativeAd) {
        /// Validate
        if (this._nativeAds != null) {
            return
        }

        /// Save data
        this._nativeAds = nativeAd

        /// Reload data
        this.startFetchData()
    }

    fun startFetchData() {
        /// Query DB
        val items = QRCodeDAO.instance.getListQRCode()
        val itemsFormatted = formatListQRCode(items = items)

        /// Binding
        _qrCodeHistoryItemsSubject.onNext(itemsFormatted)
    }

    private fun formatListQRCode(items: List<QRCodeTable>): List<QRCodeHistoryItem> {
        var listFormatted: List<QRCodeHistoryItem> = arrayListOf()
        var indexFixed = 0
        var indexRepeat = 0

        val itemsSorted = items.sortedBy { it.getDate() }.reversed()

        itemsSorted.forEach { qrCodeTable ->
            val date = TimeUtils.getDate(
                timeFormat = TimeUtils.TimeFormat.TimeFormat5,
                time = qrCodeTable.getCreateAt()
            )

            /// Counting
            indexFixed += 1
            if (indexRepeat > 0) {
                indexRepeat += 1
            }
            if (indexFixed == 3) {
                indexRepeat += 1
            }

            listFormatted.find {
                it.date == date
            }?.let {
                /// Add item history
                it.addItem(
                    item = QRCodeHistoryItemChild(
                        type = HistoryItemType.ITEM,
                        itemData = qrCodeTable,
                        itemAds = _nativeAds
                    )
                )

                /// Add item ads if need
                if ((indexFixed == 2 || (indexRepeat % 5 == 0 && indexRepeat > 0)) && RemoteUtils.isShowNativeHistoryList) {
                    it.addItem(
                        item = QRCodeHistoryItemChild(
                            type = HistoryItemType.ADS,
                            itemData = null,
                            itemAds = _nativeAds
                        )
                    )
                }
            } ?: kotlin.run {
                /// Add item history
                val item = QRCodeHistoryItem(
                    date = date,
                    items = arrayListOf(
                        QRCodeHistoryItemChild(
                            type = HistoryItemType.ITEM,
                            itemData = qrCodeTable,
                            itemAds = _nativeAds
                        )
                    )
                )

                /// Add item ads if need
                if ((indexFixed == 2 || (indexRepeat % 5 == 0 && indexRepeat > 0)) && RemoteUtils.isShowNativeHistoryList) {
                    item.addItem(
                        item = QRCodeHistoryItemChild(
                            type = HistoryItemType.ADS,
                            itemData = null,
                            itemAds = _nativeAds
                        )
                    )
                }

                listFormatted = listFormatted + item
            }
        }

        return listFormatted
    }

    fun loadItemHistory(item: QRCodeTable) {
        val qrCodeBitmap: Bitmap =
            if (item.getResultType() == QRCodeResult.CREATE.type) {
//                QRCodeManager.instance().generateQRCodeAny(content = item.getQRCodeContent())
                getBitmapFromPath(path = item.getPath()).guardLet { return }!!
            } else {
                getBitmapFromPath(path = item.getPath()).guardLet { return }!!
            }

        val result = GenerateQRCodeResult(
            qrCodeBitmap = qrCodeBitmap,
            qrCodeContent = item.getQRCodeContent(),
            resultType = item.getResultType(),
            qrCodeType = item.getType(),
            needInsert = false
        )

        /// Binding
        _loadItemSubject.onNext(result)
    }
}