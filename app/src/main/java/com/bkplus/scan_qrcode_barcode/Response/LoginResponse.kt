package com.bkplus.scan_qrcode_barcode.Response

import android.service.autofill.UserData
import com.bkplus.scan_qrcode_barcode.model.Event

data class LoginResponse(
    val status: Int,
    val message: String,
    val data: StudentData
)

data class StudentData(
    val id: Int,
    val name: String,
    val email: String,
    val events: List<EventData>, // Change the type to match the actual type of events
    val token: String
)