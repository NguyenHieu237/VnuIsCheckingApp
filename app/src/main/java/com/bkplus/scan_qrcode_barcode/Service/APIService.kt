package com.bkplus.scan_qrcode_barcode.Service

import com.bkplus.scan_qrcode_barcode.ui.qrcode.create_qrcode.CreateQRCodeFragmentArgs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class APIService {

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit? {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        retrofit = Retrofit.Builder()
            .baseUrl("https://sawfish-funny-duly.ngrok-free.app/api/v1/") // Add a trailing slash at the end
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit
    }
}