package com.bkplus.scan_qrcode_barcode.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
class BarcodeItem(
    @StringRes val name: Int,
    @DrawableRes val icon: Int,
    val type: Int
): Parcelable