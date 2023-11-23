package com.bkplus.scan_qrcode_barcode.ui.scanner.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultFragment
import com.google.mlkit.vision.barcode.common.Barcode


class ResultScanBarcodeView : BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.setUpListener()
    }

    override val layoutId: Int
        get() = R.layout.layout_barcode_result_view

    private val tvBarcodeContent: TextView by lazy {
        layout.findViewById(R.id.txtBarcode)
    }
    private val btnCopy: LinearLayout by lazy {
        layout.findViewById(R.id.copy)
    }
    private val btnSearch: LinearLayout by lazy {
        layout.findViewById(R.id.search)
    }
    private val bottom: LinearLayout by lazy {
        layout.findViewById(R.id.bottom)
    }

    fun setBarcode(barcode: Barcode) {
        tvBarcodeContent.text = barcode.displayValue
        if (ScanResultFragment.isCreate) {
            bottom.visibility = View.GONE
        }
    }

    private fun setUpListener() {

        val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        btnCopy.setOnClickListener {
            val clipData = ClipData.newPlainText("label", tvBarcodeContent.text.toString())
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(
                context,
                context.getString(R.string.copied_to_clipboard),
                Toast.LENGTH_SHORT
            ).show()
        }
        btnSearch.setOnClickListener {
            val url = "http://www.google.com/search?q="
            val query: String = tvBarcodeContent.text.toString()
            val finalUrl = url + query
            val uri = Uri.parse(finalUrl)
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
}