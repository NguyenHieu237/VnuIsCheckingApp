package com.bkplus.scan_qrcode_barcode.ui.Login

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bkplus.scan_qrcode_barcode.Request.LoginRequest
import com.bkplus.scan_qrcode_barcode.Request.OTPRequest
import com.bkplus.scan_qrcode_barcode.Response.EventData
import com.bkplus.scan_qrcode_barcode.Response.LoginResponse
import com.bkplus.scan_qrcode_barcode.Response.OTPResponse
import com.bkplus.scan_qrcode_barcode.Service.APIService
import com.bkplus.scan_qrcode_barcode.base.API.CheckingAppAPi
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {
    private val apiService = APIService()
    val api = apiService.getClient()?.create(CheckingAppAPi::class.java)

    private val _eventsList = MutableLiveData<List<EventData>?>()
    val eventsList: MutableLiveData<List<EventData>?> get() = _eventsList
    @SuppressLint("SuspiciousIndentation")
    fun makeApiCall(email: String, name: String, id: Int){
    // Create an instance of OTPRequest with the required data
    val otpRequest = OTPRequest(email, name, id)
    val call: Call<OTPResponse> = api?.LoginByOTP(otpRequest) ?: return
        call.enqueue(object : Callback<OTPResponse> {
            override fun onResponse(call: Call<OTPResponse>, response: Response<OTPResponse>) {
                // Handle the API response here
                if (response.isSuccessful) {
                    print("donedone")
                    val data = response.body()
                    // Access data.status, data.message, data.data as needed
                } else {
                  print("fail")
                }
            }

            override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun loginByOTP(otp: Int,email: String, name: String) {
        val otpRequest = LoginRequest(otp,email,name)
        // Make the API call with the OTPRequest as the request body
        val call: Call<LoginResponse> = api?.StudentLogin(otpRequest) ?: return
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Handle the API response here
                if (response.isSuccessful) {
                    val loginResponse: LoginResponse? = response.body()
                    // Access the data from the response
                    val userId = loginResponse?.data?.id
                    val userName = loginResponse?.data?.name
                    val userEmail = loginResponse?.data?.email
                    val userToken = loginResponse?.data?.token
                    val listEvent = loginResponse?.data?.events
                    QRCodePreferences.getPrefsInstance().token = userToken
                    _eventsList.value = listEvent

                } else {
                    print("fail")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}