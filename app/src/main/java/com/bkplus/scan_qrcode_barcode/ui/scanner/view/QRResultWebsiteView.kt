package com.bkplus.scan_qrcode_barcode.ui.scanner.view

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.content.ClipboardManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultFragment
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultViewModel
import com.google.mlkit.vision.barcode.common.Barcode

class QRResultWebsiteView : BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val txtWebsiteView: TextView by lazy {
        layout.findViewById(R.id.txtWebsite)
    }
    private val bottom: LinearLayout by lazy {
        layout.findViewById(R.id.bottom)
    }
    override val layoutId: Int
        get() = R.layout.layout_result_qr_website

    fun setBarcode(barcode: Barcode){
        txtWebsiteView.text = barcode.url?.url

        init()
    }

    private fun init() {
        layout.findViewById<LinearLayout>(R.id.open).setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(txtWebsiteView.text.toString()))
            context.startActivity(browserIntent)
        }
        layout.findViewById<LinearLayout>(R.id.copy).setOnClickListener {
            copyToClipboard(txtWebsiteView.text.toString())
        }
        if(ScanResultFragment.isCreate){
            bottom.visibility = View.GONE
        }
    }

    private fun copyToClipboard(text: CharSequence){
        val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label",text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context,context.getString(R.string.copied_to_clipboard),Toast.LENGTH_SHORT).show()
    }
}
