package com.bkplus.scan_qrcode_barcode.ui.scanner.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultFragment


class QRResultTextView : BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val txtTextView: TextView by lazy {
        layout.findViewById(R.id.txtText)
    }
    private val bottom: LinearLayout by lazy {
        layout.findViewById(R.id.bottom)
    }
    override val layoutId: Int
        get() = R.layout.layout_result_qr_text

    fun setContent(
        text: String?
    ) {
        txtTextView.text = text

        init()
    }

    private fun init() {
        layout.findViewById<LinearLayout>(R.id.email).setOnClickListener {
            composeEmail(null, txtTextView.text.toString())
        }
        layout.findViewById<LinearLayout>(R.id.sms).setOnClickListener {
            sms()
        }
        layout.findViewById<LinearLayout>(R.id.copy).setOnClickListener {
            copyToClipboard(txtTextView.text.toString())
        }
        if(ScanResultFragment.isCreate){
            bottom.visibility = View.GONE
        }
    }

    private fun composeEmail(addresses: Array<String?>?, subject: String?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    private fun sms(){
        val uri = Uri.parse("smsto:")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", txtTextView.text.toString())
        context.startActivity(intent)
    }

    private fun copyToClipboard(text: CharSequence) {
        val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context,context.getString(R.string.copied_to_clipboard),Toast.LENGTH_SHORT).show()
    }
}
