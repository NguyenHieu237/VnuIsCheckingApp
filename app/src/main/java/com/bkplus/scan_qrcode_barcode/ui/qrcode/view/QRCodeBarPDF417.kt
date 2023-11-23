package com.bkplus.scan_qrcode_barcode.ui.qrcode.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.Toast
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.qrcode.QRCodeManager
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputDescriptionView

class QRCodeBarPDF417 : BaseCustomViewLinearLayout {
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

    private val viewText: BaseInputDescriptionView by lazy {
        layout.findViewById(R.id.viewText)
    }

    override val layoutId: Int
        get() = R.layout.layout_qrcode_text

    private fun setupUI() {
        viewText.setLine(lines = 14)
        viewText.setHint(context.getString(R.string.hint_pdf417))
        viewText.edtContent.addTextChangedListener(mTextEditorWatcher)
    }

    fun generateQRCode() {
        if (QRCodeManager.isValidTextInput(viewText.getContent())) {
            val result = QRCodeManager.instance().generateBarPDF417Code(
                value = viewText.getContent()
            )
            onGenerateQRCodeCompletion?.invoke(result)
        } else {
            Toast.makeText(
                this.context,
                context.getString(R.string.invalid_form_417),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (viewText.edtContent.text.isNotEmpty()) {
                onActivatedInput?.invoke()
            } else {
                onEmptyInput?.invoke()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
}