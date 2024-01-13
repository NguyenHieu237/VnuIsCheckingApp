package com.bkplus.scan_qrcode_barcode.base.API

import com.bkplus.scan_qrcode_barcode.Request.CreateEventRequest
import com.bkplus.scan_qrcode_barcode.Request.LoginRequest
import com.bkplus.scan_qrcode_barcode.Request.OTPRequest
import com.bkplus.scan_qrcode_barcode.Response.AddEventResponse
import com.bkplus.scan_qrcode_barcode.Response.CheckinResponse
import com.bkplus.scan_qrcode_barcode.Response.LoginResponse
import com.bkplus.scan_qrcode_barcode.Response.OTPResponse
import com.bkplus.scan_qrcode_barcode.Response.GetEventResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CheckingAppAPi {
    @POST("student/request-otp")
    fun LoginByOTP(@Body request: OTPRequest): Call<OTPResponse>

    @POST("student/login")
    fun StudentLogin(@Body request: LoginRequest): Call<LoginResponse>

    @POST("event/create")
    fun CreateEvent(@Body request: CreateEventRequest): Call<AddEventResponse>

    @POST("checking/check")
    fun checkEvent(
        @Query("event-id") eventId: Int,
        @Header("Authorization") authorization: String
    ): Call<CheckinResponse>

    @GET("/api/v1/event")
     fun getEvents(@Query("page") page: Int, @Query("page-size") pageSize: Int): Call<GetEventResponse>

}