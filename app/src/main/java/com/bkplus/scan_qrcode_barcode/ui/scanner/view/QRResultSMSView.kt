package com.bkplus.scan_qrcode_barcode.ui.scanner.view

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.content.ClipboardManager
import android.net.Uri
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultFragment
import com.google.mlkit.vision.barcode.common.Barcode
import android.R.id.message
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar


class QRResultSMSView : BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val txtContentView: TextView by lazy {
        layout.findViewById(R.id.txtContent)
    }
    private val txtPhoneView: TextView by lazy {
        layout.findViewById(R.id.txtPhone)
    }
    private val bottom: LinearLayout by lazy {
        layout.findViewById(R.id.bottom)
    }
    override val layoutId: Int
        get() = R.layout.layout_result_qr_sms

    fun setContent(
        phone: String?,
        content: String?,
    ) {
        txtContentView.text = content
        txtPhoneView.text = phone
        init()
    }

    private fun init() {
        layout.findViewById<LinearLayout>(R.id.sms).setOnClickListener {
            sendSMS()
        }
        layout.findViewById<LinearLayout>(R.id.call).setOnClickListener {
            call()
        }
        layout.findViewById<LinearLayout>(R.id.copy).setOnClickListener {
            copyToClipboard(
                txtPhoneView.text.toString() +
                        txtContentView.text.toString()
            )
        }
       if(ScanResultFragment.isCreate){
           bottom.visibility = View.GONE
       }

    }

    private fun call() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + txtPhoneView.text.toString())
        context.startActivity(intent)
    }

    private fun sendSMS() {
        val intent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "sms:" + txtPhoneView.text.toString()
            )
        )
        intent.putExtra(txtContentView.text.toString(), message)
        context.startActivity(intent)
    }

    private fun copyToClipboard(text: CharSequence) {
        val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context,context.getString(R.string.copied_to_clipboard),Toast.LENGTH_SHORT).show()
    }

}
