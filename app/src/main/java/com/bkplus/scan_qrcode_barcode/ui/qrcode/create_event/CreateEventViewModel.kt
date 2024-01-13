package com.bkplus.scan_qrcode_barcode.ui.qrcode.create_event

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bkplus.scan_qrcode_barcode.Request.CreateEventRequest
import com.bkplus.scan_qrcode_barcode.Request.OTPRequest
import com.bkplus.scan_qrcode_barcode.Response.AddEventResponse
import com.bkplus.scan_qrcode_barcode.Response.OTPResponse
import com.bkplus.scan_qrcode_barcode.Service.APIService
import com.bkplus.scan_qrcode_barcode.base.API.CheckingAppAPi
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.qrcode.QRCodeManager
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import com.mbridge.msdk.dycreator.viewmodel.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CreateEventViewModel @Inject constructor() : ViewModel() {
    private val apiService = APIService()
    val api = apiService.getClient()?.create(CheckingAppAPi::class.java)

    private val _addEventResponse = MutableLiveData<AddEventResponse>()
    val addEventResponse: LiveData<AddEventResponse>
        get() = _addEventResponse
    fun createEvent( name: String, organizator: String, date: String, address: String) {
        // Create an instance of OTPRequest with the required data
        val eventRequest = CreateEventRequest( name, organizator, date, address)
        val call: Call<AddEventResponse> = api?.CreateEvent(eventRequest) ?: return
        call.enqueue(object : Callback<AddEventResponse> {
            override fun onResponse(call: Call<AddEventResponse>, response: Response<AddEventResponse>) {
                // Handle the API response here
                if (response.isSuccessful) {
                    _addEventResponse.value = response.body()
                } else {
                    print("fail")
                }
            }

            override fun onFailure(call: Call<AddEventResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}