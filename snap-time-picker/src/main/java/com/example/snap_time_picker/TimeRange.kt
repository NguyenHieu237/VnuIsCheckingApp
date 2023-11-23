package com.example.snap_time_picker
import android.os.Parcelable
import com.example.snap_time_picker.TimeValue
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimeRange(
    var start: TimeValue?,
    var end: TimeValue?
) : Parcelable
