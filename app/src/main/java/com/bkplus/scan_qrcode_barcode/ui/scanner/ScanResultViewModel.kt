package com.bkplus.scan_qrcode_barcode.ui.scanner

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bkplus.scan_qrcode_barcode.Response.CheckinResponse
import com.bkplus.scan_qrcode_barcode.Service.APIService
import com.bkplus.scan_qrcode_barcode.base.API.CheckingAppAPi
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeDAO
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeModel
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.TimeUtils
import com.bkplus.scan_qrcode_barcode.utils.createFileImageShare
import com.bkplus.scan_qrcode_barcode.utils.extension.guardLet
import com.bkplus.scan_qrcode_barcode.utils.storeImage
import com.google.mlkit.vision.barcode.common.Barcode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Date
import javax.inject.Inject

class ScanResultViewModel @Inject constructor(): ViewModel() {
    private var _result: GenerateQRCodeResult? = null
    var barcode: Barcode? = null

    private val _checkEventResponse = MutableLiveData<CheckinResponse>()
    val checkEventResponse: LiveData<CheckinResponse>
        get() = _checkEventResponse

    private val apiService = APIService()
    val api = apiService.getClient()?.create(CheckingAppAPi::class.java)

    fun setResult(result: GenerateQRCodeResult?) {
        this._result = result
    }

    fun getQRCodeBitmap(): Bitmap? {
        return _result?.qrCodeBitmap
    }

    fun getContent(): String? {
        return _result?.qrCodeContent
    }

    fun getFileToShare(context: Context): File? {
        val result = _result.guardLet { return null }!!
        return createFileImageShare(
            image = result.qrCodeBitmap,
            context = context
        )
    }

    fun saveDataQRCode(context: Context) {
        val result = _result.guardLet { return }!!

        /// Validate
        if (!result.needInsert) { return }

        var qrCodeImagePath = ""
//        if (result.resultType != QRCodeResult.CREATE.type) {
            qrCodeImagePath = storeImage(result.qrCodeBitmap, context = context).guardLet { return }!!
//        }


        ///TEST
//        var dateTest = TimeUtils.getDate(timeFormat = TimeUtils.TimeFormat.TimeFormat5, time = "12/30/2023")

        val model = QRCodeModel(
            type = result.qrCodeType,
            path = qrCodeImagePath,
            createAt = TimeUtils.getTime(
                timeFormat = TimeUtils.TimeFormat.TimeFormat4,
                time = Date()
            ),
            resultType = result.resultType,
            qrCodeContent = result.qrCodeContent,
            qrcodeBarcode = barcode!!.format,
            qrCodeBarcodeType = barcode!!.valueType
        )

        QRCodeDAO.instance.insertDataQRCode(model = model)
    }

    fun checkEvent(eventId: Int, token: String) {
        val authorizationHeader = "Bearer $token"
        val call: Call<CheckinResponse> = api?.checkEvent(eventId, authorizationHeader) ?:return

        call.enqueue(object : Callback<CheckinResponse> {
            override fun onResponse(call: Call<CheckinResponse>, response: Response<CheckinResponse>) {
                if (response.isSuccessful) {
                    _checkEventResponse.value = response.body()
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<CheckinResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}