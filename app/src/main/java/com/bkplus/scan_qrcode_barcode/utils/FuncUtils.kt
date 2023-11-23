package com.bkplus.scan_qrcode_barcode.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputFieldView


fun onClickDelete(b : BaseInputFieldView){
    b.imgIconEnd.setOnClickListener { b.edtContent.text.clear() }
}
fun restrictEmailInput(e: EditText): Boolean {
    val textString: String = e.text.toString()
    if (textString[0] == '.' ||
        textString.last() == '.' ||
        isDotRepetitive(textString) ||
        textString.substringBefore("@").last() == '.'
    )
        return true
    return false
}

fun isWhiteSpaceRepetitive(string: String): Boolean {
    for (i in string.indices) {
        if (i < string.lastIndex) {
            if (string[i] == ' ' && string[i + 1] == ' ')
                return true
        }
    }
    return false
}

fun isDotRepetitive(string: String): Boolean {
    for (i in string.indices) {
        if (i < string.lastIndex) {
            if (string[i] == '.' && string[i + 1] == '.')
                return true
        }
    }
    return false
}

fun isValidEmail(target: CharSequence): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun haveWhiteSpace(string: String): Boolean {
    for (i in string.indices) {
        if (string[i] == ' ')
            return true
    }
    return false
}

fun checkSocialMedia(url: String?): String {
    if (url != null) {
        if (url.contains("www.facebook.com", false)) return "Facebook"
        if (url.contains("twitter.com", false)) return "Twitter"
        if (url.contains("www.instagram.com", false)) return "Instagram"
        if (url.contains("m.me", false)) return "Messenger"
        if (url.contains("www.youtube.com", false)) return "Youtube"
        if (url.contains("www.linkedin.com", false)) return "Linkedin"
        if (url.contains("telegram.me", false)) return "Telegram"
        if (url.contains("paypal.me", false)) return "Paypal"
    }
    return "Website"
}

fun checkUrlIcon(context: Context, url: String?): Drawable {
    if (url != null) {
        if (url.contains("www.facebook.com", false)) return context.resources.getDrawable(
            R.drawable.ic_facebook,
            null
        )
        if (url.contains("twitter.com", false)) return context.resources.getDrawable(
            R.drawable.ic_twitter,
            null
        )
        if (url.contains("www.instagram.com", false)) return context.resources.getDrawable(
            R.drawable.ic_instagram,
            null
        )
        if (url.contains("m.me", false)) return context.resources.getDrawable(
            R.drawable.ic_messenger,
            null
        )
        if (url.contains("www.youtube.com/", false)) return context.resources.getDrawable(
            R.drawable.ic_youtube,
            null
        )
        if (url.contains("www.linkedin.com", false)) return context.resources.getDrawable(
            R.drawable.ic_linkedin,
            null
        )
        if (url.contains("telegram.me", false)) return context.resources.getDrawable(
            R.drawable.ic_telegram,
            null
        )
        if (url.contains("paypal.me", false)) return context.resources.getDrawable(
            R.drawable.ic_paypal,
            null
        )
    }
    return context.resources.getDrawable(
        R.drawable.ic_website,
        null
    )
}

fun checkIfUrlInput(s: String): Boolean {
    if (s.length < 8) return false
    return true
}