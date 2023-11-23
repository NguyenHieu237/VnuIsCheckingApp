package com.bkplus.scan_qrcode_barcode.ui.scanner.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultFragment


class QRResultContactView : BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val txtNameView: TextView by lazy {
        layout.findViewById(R.id.txtName)
    }
    private val txtPhoneView: TextView by lazy {
        layout.findViewById(R.id.txtPhone)
    }
    private val txtMailView: TextView by lazy {
        layout.findViewById(R.id.txtMail)
    }
    private val bottom: LinearLayout by lazy {
        layout.findViewById(R.id.bottom)
    }
    override val layoutId: Int
        get() = R.layout.layout_result_qr_contact

    fun setContent(
        name: String?,
        phone: String?,
        mail: String?
    ) {
        txtNameView.text = name
        txtPhoneView.text = phone
        txtMailView.text = mail
        if (ScanResultFragment.isCreate) {
            bottom.visibility = View.GONE
        }
    }
}
