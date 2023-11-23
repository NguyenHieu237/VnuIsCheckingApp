package com.bkplus.scan_qrcode_barcode.ui.qrcode.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.Toast
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.qrcode.QRCodeManager
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputFieldView

class QRCodeBarCODABAR : BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.setupUI()
    }

    var onGenerateQRCodeCompletion: ((GenerateQRCodeResult) -> Unit)? = null
    var onEmptyInput: (() -> Unit)? = null
    var onActivatedInput: (() -> Unit)? = null

    private val viewBarCode: BaseInputFieldView by lazy {
        layout.findViewById(R.id.viewNamePhone)
    }
    private val invalid: ImageView by lazy {
        layout.findViewById(R.id.invalid)
    }

    override val layoutId: Int
        get() = R.layout.layout_qrcode_phone

    private fun setupUI() {
        viewBarCode.setInputTypeNumber()
        viewBarCode.setIcon(R.drawable.ic_barcode_green)
        viewBarCode.setHint(context.getString(R.string.hint_codabar))
        viewBarCode.edtContent.addTextChangedListener(mTextEditorWatcher)
    }

    fun generateQRCode() {
        if (viewBarCode.getContent().isEmpty()) {
            Toast.makeText(
                this.context,
                context.getString(R.string.qr_code_error_website),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        onGenerateQRCodeCompletion?.invoke(
            QRCodeManager.instance().generateBarCODABARCode(viewBarCode.getContent())
        )
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (viewBarCode.edtContent.text.isNotEmpty()) {
                onActivatedInput?.invoke()
            } else {
                onEmptyInput?.invoke()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
}