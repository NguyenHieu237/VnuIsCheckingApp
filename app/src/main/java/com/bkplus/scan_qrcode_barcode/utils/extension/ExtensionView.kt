package com.bkplus.scan_qrcode_barcode.utils.extension

import android.view.View

inline fun View.hiddenIf(condition: () -> Boolean) : View {
    visibility = if (condition()) {
        View.GONE
    } else {
        View.VISIBLE
    }
    return this
}

fun View.gone() {
    if (visibility != View.GONE)
        visibility = View.GONE
}

fun View.visible() {
    if (visibility != View.VISIBLE)
        visibility = View.VISIBLE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE)
        visibility = View.INVISIBLE
}