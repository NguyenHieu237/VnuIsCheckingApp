package com.bkplus.scan_qrcode_barcode.ui.qrcode.ManagerTab.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bkplus.scan_qrcode_barcode.Response.EventData
import com.bkplus.scan_qrcode_barcode.Response.GetEventResponse
import com.bkplus.scan_qrcode_barcode.Service.APIService
import com.bkplus.scan_qrcode_barcode.base.API.CheckingAppAPi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ManagerViewModel @Inject constructor() : ViewModel() {

    private val apiService = APIService()
    val api = apiService.getClient()?.create(CheckingAppAPi::class.java)
    val call: Call<GetEventResponse>? = api?.getEvents(1, 10)


    private val _eventsList = MutableLiveData<List<EventData>?>()
    val eventsList: MutableLiveData<List<EventData>?> get() = _eventsList

    fun getEventFromAPI() {
        val apiService = APIService()
        val api = apiService.getClient()?.create(CheckingAppAPi::class.java)
        val call: Call<GetEventResponse>? = api?.getEvents(1, 10)

        call?.enqueue(object : Callback<GetEventResponse> {
            override fun onResponse(
                call: Call<GetEventResponse>,
                response: Response<GetEventResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse: GetEventResponse? = response.body()

                    apiResponse?.let {
                        _eventsList.postValue(it.data)
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(
                call: Call<GetEventResponse>,
                t: Throwable
            ) {
                println("Network error: ${t.message}")
            }
        })
    }

}
