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
import com.bkplus.scan_qrcode_barcode.utils.isWhiteSpaceRepetitive


class QRCodeTextView : BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.setupUI()
    }

    private val viewText: BaseInputDescriptionView by lazy {
        layout.findViewById(R.id.viewText)
    }

    override val layoutId: Int
        get() = R.layout.layout_qrcode_text

    fun setText(text: String?) {
        viewText.setContent(text)
    }

    private fun setupUI() {
        viewText.setHint(context.getString(R.string.qr_code_enter_text))
        viewText.setLine(lines = 14)
        viewText.edtContent.addTextChangedListener(mTextEditorWatcher)
    }

    var onGenerateQRCodeCompletion: ((GenerateQRCodeResult) -> Unit)? = null
    var onEmptyInput: (() -> Unit)? = null
    var onActivatedInput: (() -> Unit)? = null

    fun generateQRCode() {
        if (viewText.getContent().isEmpty()) {
            Toast.makeText(
                this.context,
                context.getString(R.string.qr_code_error_text),
                Toast.LENGTH_SHORT
            ).show()
            return
        } else {
            if (isWhiteSpaceRepetitive(viewText.getContent())) {
                Toast.makeText(
                    this.context,
                    context.getString(R.string.repetitive_white_spaces),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            onGenerateQRCodeCompletion?.invoke(
                QRCodeManager.instance().generateQRCodeText(viewText.getContent())
            )
        }
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //This sets a textview to the current length
            viewText.counter.text = s.length.toString()
            if(viewText.edtContent.text.isNotEmpty()){
                onActivatedInput?.invoke()
            } else {
                onEmptyInput?.invoke()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
}

