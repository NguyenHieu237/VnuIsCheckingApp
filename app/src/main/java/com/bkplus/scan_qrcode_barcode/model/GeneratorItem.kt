package com.bkplus.scan_qrcode_barcode.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

@Parcelize
class GeneratorItem(
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int,
    val type: Int
): Parcelable