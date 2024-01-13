package com.bkplus.scan_qrcode_barcode.Response

data class AddEventResponse(
    val status: Int,
    val message: String,
    val data: EventData
)

data class GetEventResponse(
    val status: Int,
    val message: String,
    val data: List<EventData>
)

data class EventData(
    val id: Int,
    val name: String,
    val date: String,
    val organizer: String,
    val expiry: String,
    val students: List<StudentData> // Change the type to match the actual type of students
)